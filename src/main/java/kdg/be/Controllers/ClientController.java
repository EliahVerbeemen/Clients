package kdg.be.Controllers;

import kdg.be.AfterTenOClockError;
import kdg.be.Managers.ClientManager;
import kdg.be.Managers.LoyalityClassManager;
import kdg.be.Modellen.*;
import kdg.be.Modellen.DTO.ClientWithProdcuts;
import kdg.be.RabbitMQ.RabbitSender;
import kdg.be.Repositories.ClientRepository;
import kdg.be.Repositories.OrderRepository;
import kdg.be.Repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


@RestController
//Elke oute begint met api (conventie voor api's)
@RequestMapping("/api")
public class ClientController {
    //Remove ClientRepo once ClientManager is done
    private ClientRepository ClientRepository;
    private ClientManager clientMgr;
    private OrderRepository OrderRepository;
    private ProductRepository productRepository;
    private LoyalityClassManager loyalityClassManager;
    private RabbitSender rabbitSender;
    Logger logger = LoggerFactory.getLogger(ClientController.class);

    public ClientController(ClientRepository clientrepo, ClientManager clientManager, OrderRepository orderRepository, ProductRepository productRepository, LoyalityClassManager loyalityClassManager
            , RabbitSender rabbitSender) {

        this.ClientRepository = clientrepo;
        this.clientMgr = clientManager;
        this.OrderRepository = orderRepository;
        this.productRepository = productRepository;
        this.loyalityClassManager = loyalityClassManager;
        this.rabbitSender = rabbitSender;

    }

    @PostMapping("/test")
    public ResponseEntity<Test> testRoute(@RequestBody Test test) {
        return ResponseEntity.status(HttpStatus.CREATED).body(test);
    }

    //Dus deze route bereik je via /api/klant
    //
    @PostMapping(value = "/klant")
    public ResponseEntity<Client> CreateCustomer(@RequestBody Client klant) {
        Client newClient = this.clientMgr.makeClient(klant);
        return ResponseEntity.status(HttpStatus.CREATED).body(newClient);
    }

    @DeleteMapping(value = "/klant")
    //Postman ==> raw ==>1 (selectbox = json)
    public HttpStatus DeleteCustomer(@RequestBody Long klantId) {
        Optional<Client> klant = clientMgr.getClientById(klantId);
        if (klant.isPresent()) {
            clientMgr.removeClient(klantId);
            return HttpStatus.OK;
        } else {
            return HttpStatus.NOT_FOUND;
        }
    }

    @PutMapping(value = "/klant", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Client> UpdateClient(@RequestBody Client geupdateklant) {
        Optional<Client> oudeklant = this.clientMgr.getClientById(geupdateklant.getClientId());
        if (oudeklant.isPresent()) {
            Client klant = oudeklant.get();
            klant.setClientType(geupdateklant.getClientType());
            Client updatedClient = clientMgr.makeOrUpdateClient(klant);
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedClient);
        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
    }

    //TODO Splits deze methode op in kleinere
    // in het body bijvoorbeeld:  {"klantId":1,"order":{"1":3}}
    //Json [] voor een lijst en {} voor een map of object
    @PostMapping(value = "/order", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> PlaatsOrder(@RequestBody ClientWithProdcuts clientWithProdcuts) {

        Optional<Client> optioneleKlant = ClientRepository.findById(clientWithProdcuts.getClientId());
        if (optioneleKlant.isPresent()) {
            Client klant = optioneleKlant.get();

            //Maak een volledige kopie (deep copy) van de
            Order order = new Order();
            order.setOrderStatus(Order.OrderStatus.Niet_bevestigd);
            order.setOrderDate(LocalDate.now());

            Map<Product, Integer> productenVoorDezeBestelling = new HashMap<>();
            AtomicReference<Double> totaalZonderKorting = new AtomicReference<>(0d);
            clientWithProdcuts.getOrder().forEach((productId, aantal) ->
            {
                Optional<Product> optioneelproduct = this.productRepository.findById(productId);
                if (optioneelproduct.isPresent()) {
                    Product product = optioneelproduct.get();
                    productenVoorDezeBestelling.put(product, aantal);
                    totaalZonderKorting.updateAndGet(v -> (product.getPrice() * aantal));
                    order.getProducts().put(productId, aantal);
                } else {
                    //Maak de klant er op attent dat we 1 van zijn producten niet kenden;
                    order.getRemarks().add("Chosen product is not available: " + optioneelproduct.toString());

                }
            });

            ;


            //TODO stuur AMPQ signaal naar batch (methode is al geshreven deserialisatie is misschien nog
            //foutief


            order.setTotalPrice(totaalZonderKorting.get());
            order.setClient(klant);
            klant.getOrders().add(order);
            this.OrderRepository.save(order);
            this.ClientRepository.save(klant);
            //Validatie geen lege bestellingen of totaalprijze van nul
            if (BeforeTenOClock()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(order);
            } else {
                order.getRemarks().add("orders placed after 10.0 pm. will be delivered the next day");
            }

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @PostMapping("/order/history/{OrderId}")
    public ResponseEntity<Order> HerhaalBestelling(@RequestBody Long klantId, @PathVariable Long OrderId) {

        Optional<Order> optioneleOrder = this.OrderRepository.findByOrderNumber(OrderId);
        Optional<Client> optioneleklant = this.ClientRepository.findById(klantId);
        System.out.println(klantId);
        if (optioneleOrder.isPresent() && optioneleklant.isPresent()) {
            Order vroegerOrder = optioneleOrder.get();
            HashMap<Long, Integer> newProdutMap = new HashMap<>();
            newProdutMap.putAll(vroegerOrder.getProducts());
            Order nieuwOrder = new Order(vroegerOrder.getClient(), LocalDate.now(), newProdutMap, Order.OrderStatus.Niet_bevestigd);
            nieuwOrder.setOrderId(null);
            Order nieuwOrderMetPrijs = SetPrijsinfo(nieuwOrder);
            Client klant = optioneleklant.get();
            nieuwOrderMetPrijs.setClient(klant);
            klant.getOrders().add(nieuwOrderMetPrijs);
            this.OrderRepository.save(nieuwOrderMetPrijs);
            return ResponseEntity.status(HttpStatus.CREATED).body(nieuwOrderMetPrijs);


        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }


    }

    @PatchMapping("/order/confirm/{orderId}")
    // werkt met http://localhost:8080/api/order/bevestig/1
//E is geen order als je de database aanmaakt
    public ResponseEntity<Order> BevestigOrder(@PathVariable long orderId) {

        Optional<Order> optionerelOrder = this.OrderRepository.findById(orderId);
        if (optionerelOrder.isPresent()) {

            Order order = optionerelOrder.get();
            logger.debug(order.getClient().clientId.toString());

            order.setOrderStatus(Order.OrderStatus.Bevestigd);
            Optional<Client> optioneleKlant = this.ClientRepository.findById(order.getClient().getClientId());
            if (optioneleKlant.isPresent()) {
                Client klant = optioneleKlant.get();
                klant.setPoints((int) Math.round(klant.getPoints() + order.getTotalPrice()) / 10);

            }

            Order orderMetPrijs = SetPrijsinfo(order);
            this.OrderRepository.save(orderMetPrijs);
            this.OrderRepository.findAll().forEach(e -> logger.info(String.valueOf(e.getTotalPrice())));


            return ResponseEntity.ok().body(orderMetPrijs);


        } else {


            return ResponseEntity.notFound().build();


        }


    }

    @PatchMapping("/order/cancel/{orderId}")
    public ResponseEntity<Order> AnnuleerOrder(@PathVariable(required = true) long orderId) {
        if (BeforeTenOClock()) {
            Optional<Order> optionerelOrder = this.OrderRepository.findById(orderId);
            if (optionerelOrder.isPresent()) {


                Order order = optionerelOrder.get();
                if (order.getOrderStatus() != Order.OrderStatus.Bevestigd) {
                    order.setOrderStatus(Order.OrderStatus.Geannulleerd);
                    return ResponseEntity.ok().body(order);

                } else {
                    return ResponseEntity.status(405).build();


                }


            } else {

                return ResponseEntity.notFound().build();

            }
        } else {
            //Todo custom errormessages @ControllerAdvice
            throw new AfterTenOClockError();
        }

    }


    //Niet vergeten om wat validatie te doen. Orders zonder producten bvb
    //Spring voorziet default annotaties
    private Order SetPrijsinfo(Order order) {
        AtomicReference<Double> prijsZonderKorting = new AtomicReference<>((double) 0);
        double absoluteKorting = 0.0;
        Client klant = order.getClient();


        System.out.println(order.getProducts().size());

        LoyalityClasses loyalityClasses = VerkrijgKlasse(klant.getPoints());
        order.getProducts().forEach((productKey, quantity) -> {
            Optional<Product> product = this.productRepository.findById(productKey);
            product.ifPresent(value -> prijsZonderKorting.updateAndGet(v -> v + value.getPrice() * quantity));


        });
        System.out.println(prijsZonderKorting);
        absoluteKorting = prijsZonderKorting.get() * loyalityClasses.getReduction();
        order.setDiscount(absoluteKorting);
        order.setTotalPrice(prijsZonderKorting.get() - absoluteKorting);

        return order;

    }


    private LoyalityClasses VerkrijgKlasse(int puntenAantal) {


        Optional<LoyalityClasses> optionalClass = loyalityClassManager.findAll().stream().filter(e -> e.getMinimumPoints() < puntenAantal).max(Comparator.comparingInt(LoyalityClasses::getMinimumPoints));
        return optionalClass.orElseGet(() -> loyalityClassManager.findAll().stream().min((e, k) -> e.getMinimumPoints()).orElse(new LoyalityClasses()));

    }


    @GetMapping("/Klant/loyaliteit")
    public ResponseEntity<LoyalityClasses> ObtainLoyalityInfo(@RequestBody Long klantId) {

        Optional<Client> optioneleKlant = this.ClientRepository.findById(klantId);

        if (optioneleKlant.isPresent()) {
            Client klant = optioneleKlant.get();
            return ResponseEntity.ok().body(VerkrijgKlasse(klant.getPoints()));

        } else {
            return ResponseEntity.notFound().build();
        }


    }


    //TODO eigenlijke sorteren werkt nog niet.
    //Schrijf in de klantenrepository een methode die een Sort accepteert
    @GetMapping("/order")
    public ResponseEntity<List<Order>> GetOrders(@RequestBody Long klantId, @RequestParam Optional<String> datum, @RequestParam Optional<String> status) {

        Optional<Client> optioneleKlant = ClientRepository.findById(klantId);
        if (optioneleKlant.isPresent()) {
            Client klant = optioneleKlant.get();

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

    private static boolean BeforeTenOClock() {
        LocalTime hour = LocalTime.now();
        return hour.isBefore(LocalTime.of(22, 0, 0));

    }

    @GetMapping("/testRabbit")
    public void TestRabbit() {

        Order order = this.OrderRepository.findAll().get(0);
        this.rabbitSender.SendOrderToBaker(order);
    }
}
