package kdg.be.Controllers;

import kdg.be.Managers.LoyalityClassManager;
import kdg.be.Modellen.*;
import kdg.be.Modellen.DTO.CustomerWithProducts;
import kdg.be.Repositories.KlantRepository;
import kdg.be.Repositories.OrderRepository;
import kdg.be.Repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api")
public class CustomerController {
    private KlantRepository KlantRepository;
    private OrderRepository OrderRepository;
    private ProductRepository productRepository;

    private LoyalityClassManager loyalityClassManager;

    Logger logger= LoggerFactory.getLogger(CustomerController.class);

    public CustomerController(KlantRepository klantRepository, OrderRepository orderRepository, ProductRepository productRepository, LoyalityClassManager loyalityClassManager) {

        this.KlantRepository = klantRepository;
        this.OrderRepository = orderRepository;
        this.productRepository=productRepository;
        this.loyalityClassManager=loyalityClassManager;

    }


    @PostMapping("/test")
    public ResponseEntity<Test>testRoute(@RequestBody Test test){

        return  ResponseEntity.status(HttpStatus.CREATED).body(test);

    }

    @PostMapping(value = "/klant")
    public ResponseEntity<Klant> CreateKlant(@RequestBody Klant klant) {

        this.KlantRepository.save(klant);
        return ResponseEntity.status(HttpStatus.CREATED).body(klant);


    }

    @DeleteMapping(value = "/klant")
    public HttpStatus DeleteKlant(@RequestBody Long klantId) {

        Optional<Klant> klant = KlantRepository.findById(klantId);
        if (klant.isPresent()) {

            KlantRepository.deleteKlantByKlantNumber(klantId);
            return HttpStatus.OK;
        } else {
            return HttpStatus.NOT_FOUND;
        }
    }


    @PutMapping(value = "/klant", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Klant> UpdateKlant(@RequestBody Klant geupdateklant) {
        Optional<Klant> oudeklant = this.KlantRepository.findById(geupdateklant.getKlantNumber());
        if (oudeklant.isPresent()) {
            Klant klant = oudeklant.get();
            klant.setKlanttype(geupdateklant.getKlanttype());
            KlantRepository.save(klant);
            return ResponseEntity.status(HttpStatus.CREATED).body(klant);
        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
    }

    //TODO Splits deze methode op in kleinere
    //{"klantId":1,"order":{"1":3}}
    @PostMapping(value = "/order", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> PlaatsOrder(@RequestBody CustomerWithProducts customerWithProducts) {

        Optional<Klant> optioneleKlant = KlantRepository.findById(customerWithProducts.getKlantId());
        if (optioneleKlant.isPresent()) {
            Klant klant=optioneleKlant.get();

            //Maak een volledige kopie (deep copy) van de
            Order order=new Order();
            order.setBestellingsStatus(Order.BestellingStatus.Niet_bevestigd);
            order.setBestelDatum(LocalDate.now());

           Map<Product,Integer> productenVoorDezeBestelling=new HashMap<>();
           AtomicReference<Double> totaalZonderKorting= new AtomicReference<>(0d);
            customerWithProducts.getOrder().forEach((productId, aantal)->
            {
              Optional<Product> optioneelproduct= this.productRepository.findById(productId);
              if(optioneelproduct.isPresent()){
                  Product product=optioneelproduct.get();
                    productenVoorDezeBestelling.put(product,aantal);
                  totaalZonderKorting.updateAndGet(v -> ( product.getPrijs() * aantal));
                  order.getProducs().put(productId,aantal);
              }
              else{
                  //Maak de klant er op attent dat we 1 van zijn producten niet kenden;
              }
            });

            if (LocalTime.now().isBefore(LocalTime.of(22, 0))) ;


                //TODO stuur AMPQ signaal naar batch van vandaag

            else {

                //TODO stuur AMPQ signaal naar batch van morgen


            }
            order.setTotaalprijs(totaalZonderKorting.get());
            order.setKlant(klant);
            klant.getOrders().add(order);
            this.OrderRepository.save(order);
            this.KlantRepository.save(klant);
            //Validatie geen lege bestellingen of totaalprijze van nul
            return ResponseEntity.status(HttpStatus.CREATED).body(order);

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/order/history/{OrderId}")
    public ResponseEntity<Order> HerhaalBestelling(@RequestBody Long klantId,@PathVariable Long OrderId) {

        Optional<Order> optioneleOrder = this.OrderRepository.findByOrderNumber(OrderId);
        Optional<Klant> optioneleklant = this.KlantRepository.findById(klantId);
System.out.println(klantId);
        if (optioneleOrder.isPresent()&& optioneleklant.isPresent()) {
            Order vroegerOrder = optioneleOrder.get();
            HashMap<Long,Integer> newProdutMap=new HashMap<>();
            newProdutMap.putAll(vroegerOrder.getProducs());
            Order nieuwOrder = new Order(vroegerOrder.getKlant(), LocalDate.now(), newProdutMap, Order.BestellingStatus.Niet_bevestigd);
         nieuwOrder.setOrderNumber(null);
            Order nieuwOrderMetPrijs = SetPrijsinfo(nieuwOrder);
            Klant klant=optioneleklant.get();
            nieuwOrderMetPrijs.setKlant(klant);
            klant.getOrders().add(nieuwOrderMetPrijs);
            this.OrderRepository.save(nieuwOrderMetPrijs);
            return ResponseEntity.status(HttpStatus.CREATED).body(nieuwOrderMetPrijs);


        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }


    }
//TODO hier moet de klant zijn punten krijgen
    @PatchMapping("/order/confirm/{orderId}")
    // werkt met http://localhost:8080/api/order/bevestig/1
    //Maak wel eerst een order
    public ResponseEntity<Order> BevestigOrder(@PathVariable long orderId) {

        Optional<Order> optionerelOrder = this.OrderRepository.findById(orderId);
        if (optionerelOrder.isPresent()) {

            Order order = optionerelOrder.get();
            logger.debug(order.getKlant().klantNumber.toString());

            order.setBestellingsStatus(Order.BestellingStatus.Bevestigd);
            Optional<Klant> optioneleKlant=   this.KlantRepository.findById( order.getKlant().getKlantNumber());
            if(optioneleKlant.isPresent()){
                Klant klant=optioneleKlant.get();
                klant.setPoints((int)Math.round(klant.getPoints()+order.getTotaalprijs())/10);

            }

            Order orderMetPrijs = SetPrijsinfo(order);
            this.OrderRepository.save(orderMetPrijs);
            this.OrderRepository.findAll().forEach(e->logger.info(String.valueOf(e.getTotaalprijs())));




            return ResponseEntity.ok().body(orderMetPrijs);


        } else {


            return ResponseEntity.notFound().build();


        }


    }

    @PatchMapping("/order/cancel/{orderId}")
    public ResponseEntity<Order> AnnuleerOrder(@PathVariable(required = true) long orderId) {
        Optional<Order> optionerelOrder = this.OrderRepository.findById(orderId);
        if (optionerelOrder.isPresent()) {


            Order order = optionerelOrder.get();
            if (order.getBestellingsStatus() != Order.BestellingStatus.Bevestigd) {
                order.setBestellingsStatus(Order.BestellingStatus.Geannulleerd);
                return ResponseEntity.ok().body(order);

            } else {
                return ResponseEntity.status(405).build();


            }


        } else {

            return ResponseEntity.notFound().build();

        }


    }


    //Niet vergeten om wat validatie te doen. Orders zonder producten bvb
    //Spring voorziet default annotaties
    private Order SetPrijsinfo(Order order) {
        AtomicReference<Double> prijsZonderKorting = new AtomicReference<>((double) 0);
        double absoluteKorting=0.0;
        Klant klant=order.getKlant();


   System.out.println(order.getProducs().size());

        LoyalityClasses loyalityClasses=VerkrijgKlasse(klant.getPoints());
    order.getProducs().forEach((productKey,quantity)->{
     Optional<Product> product=  this.productRepository.findById(productKey);
        product.ifPresent(value -> prijsZonderKorting.updateAndGet(v -> v + value.getPrijs() * quantity));


    });
        System.out.println(prijsZonderKorting);
     absoluteKorting=prijsZonderKorting.get()*loyalityClasses.getReduction();
       order.setKorting(absoluteKorting);
        order.setTotaalprijs(prijsZonderKorting.get()-absoluteKorting);

        return order;

    }


    private LoyalityClasses VerkrijgKlasse(int puntenAantal)  {


Optional<LoyalityClasses> optionalClass= loyalityClassManager.findAll().stream().filter(e -> e.getMinimumPoints() < puntenAantal).max(Comparator.comparingInt(LoyalityClasses::getMinimumPoints));
    return optionalClass.orElseGet(() -> loyalityClassManager.findAll().stream().min((e, k) -> e.getMinimumPoints()).orElse(new LoyalityClasses()));

    }


    @GetMapping("/Klant/loyaliteit")
    public ResponseEntity<LoyalityClasses> VerkrijgLoyaliteitsNiveau(@RequestBody Long klantId) throws IOException {

        Optional<Klant> optioneleKlant = this.KlantRepository.findById(klantId);

        if (optioneleKlant.isPresent()) {
            Klant klant = optioneleKlant.get();
            return ResponseEntity.ok().body(VerkrijgKlasse(klant.getPoints()));

        } else {
            return ResponseEntity.notFound().build();
        }


    }


    //Blijkbaar is er ook een exists by id methode
//Er is over nadenken hoe dat we dit kunnen doen
    @GetMapping("/order")
    public ResponseEntity<List<Order>> GetOrders(@RequestBody Long klantId, @RequestParam Optional<String> datum, @RequestParam Optional<String> status) {

        Optional<Klant> optioneleKlant = KlantRepository.findById(klantId);
        if (optioneleKlant.isPresent()) {
            Klant klant = optioneleKlant.get();

            List<Order> orders = new ArrayList<>();
            if (datum.isPresent()) {
                Sort sort = Sort.by(Sort.Direction.ASC, "bestelDatum");
                orders = OrderRepository.findByKlant_KlantNumber(klantId, sort);

                return ResponseEntity.ok().body(orders);
            } else {

        // WERKT @Fetch it eager
                return ResponseEntity.ok().body(klant.getOrders().stream().toList());
            }

        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
