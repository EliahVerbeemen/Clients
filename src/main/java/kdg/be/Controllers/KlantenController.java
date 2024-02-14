package kdg.be.Controllers;

import kdg.be.Modellen.Klant;
import kdg.be.Modellen.Loyaliteitsklasse;
import kdg.be.Modellen.Order;
import kdg.be.Repositories.KlantRepository;
import kdg.be.Repositories.OrderRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RestController
public class KlantenController {

    private KlantRepository KlantRepository;

    private OrderRepository OrderRepository;


    public KlantenController(KlantRepository klantRepository, OrderRepository orderRepository) {

        this.KlantRepository = klantRepository;
        this.OrderRepository = orderRepository;


    }

    @PostMapping(value = "/klant", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Klant> CreateKlant(@RequestBody Klant klant) {
        System.out.println(klant.getPuntenAantal());
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

    @PostMapping(value = "/order", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> PlaatsOrder(@RequestBody Order order) {

        Optional<Klant> klant = KlantRepository.findById(order.getKlant().getKlantNumber());

        if (klant.isPresent()) {
        order.setBestellingsStatus(Order.BestellingStatus.Niet_bevestigd);
        if(LocalTime.now().isBefore(LocalTime.of(22,0)));


        //TODO stuur AMPQ signaal naar batch van vandaag

        else{

            //TODO stuur AMPQ signaal naar batch van morgen


            }
        return ResponseEntity.status(HttpStatus.CREATED).body(order);

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<Order> HerhaalBestelling(@RequestBody Long OrderId) {

        Optional<Order> optioneleOrder = this.OrderRepository.findByOrderNumber(OrderId);
        if (optioneleOrder.isPresent()) {
            Order vroegerOrder=optioneleOrder.get();
            Order nieuwOrder=new Order(vroegerOrder.getKlant(),LocalDate.now(),vroegerOrder.getProducten(), Order.BestellingStatus.Niet_bevestigd);
           Order nieuwOrderMetPrijs= SetPrijsinfo(nieuwOrder);
            this.OrderRepository.save(nieuwOrderMetPrijs);
            return ResponseEntity.status(HttpStatus.CREATED).body(nieuwOrderMetPrijs);


        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }


    }

    @PatchMapping
    public ResponseEntity<Order> BevestigOrder(long orderId){

        Optional<Order> optionerelOrder=this.OrderRepository.findById(orderId);
        if(optionerelOrder.isPresent()){

            Order order=optionerelOrder.get();


            order.setBestellingsStatus(Order.BestellingStatus.Bevestigd);
            Order orderMetPrijs=SetPrijsinfo(order);
            this.OrderRepository.save(orderMetPrijs);
            return ResponseEntity.ok().body(orderMetPrijs);


        }


        else{


            return ResponseEntity.notFound().build();


        }




    }

    @PatchMapping("/order")
    public ResponseEntity<Order> AnnuleerOrder(long orderId){
        Optional<Order> optionerelOrder=this.OrderRepository.findById(orderId);
        if(optionerelOrder.isPresent()){


            Order order=optionerelOrder.get();
            if(order.getBestellingsStatus()!= Order.BestellingStatus.Bevestigd) {
                order.setBestellingsStatus(Order.BestellingStatus.Geannulleerd);
                return ResponseEntity.ok().body(order);

            }
            else{
                return ResponseEntity.status(405).build();


            }



        }
        else{

            return ResponseEntity.notFound().build();

        }


    }


 //Niet vergeten om wat validatie te doen. Orders zonder producten bvb
    //Spring voorziet default annotaties
    private Order SetPrijsinfo(Order order){
              AtomicReference<Double> prijsZonderKorting= new AtomicReference<>((double) 0);


        Loyaliteitsklasse loyaliteitsklasse=VerkrijgKlasse(order.getKlant());
        double korting=loyaliteitsklasse.getKorting();

order.getProducten().forEach((product,aantal)-> prijsZonderKorting.updateAndGet(v ->  (v + product.getPrijs() * aantal)));
order.setKorting(prijsZonderKorting.get()*korting);
order.setTotaalprijs(prijsZonderKorting.get()-prijsZonderKorting.get()*korting);
return order;

    }



    private Loyaliteitsklasse VerkrijgKlasse(@RequestBody Klant klant) {

   return Klant.LoyliteitsKlassen.get((int) (Klant.LoyliteitsKlassen.stream().filter(e->e.getMinimumPuntenAantal()< klant.getPuntenAantal()).count()-1)) ;


    }



@GetMapping("/Klant/loyaliteit")
public ResponseEntity<Loyaliteitsklasse> VerkrijgLoyaliteitsNiveau(@RequestBody Long klantId){

 Optional<Klant> optioneleKlant=  this.KlantRepository.findById(klantId);

    if(optioneleKlant.isPresent()){
        Klant klant=optioneleKlant.get();
        return ResponseEntity.ok().body(VerkrijgKlasse(klant));

    }
    else{
        return ResponseEntity.notFound().build();
    }


}










//Blijkbaar is er ook een exists by id methode
//Er is over nadenken hoe dat we dit kunnen doen
@GetMapping("/order")
public ResponseEntity<List<Order>> GetBestellingen(@RequestBody Long klantId,@RequestParam Optional<String> datum,@RequestParam Optional<String> status){

    Optional<Klant> optioneleKlant=KlantRepository.findById(klantId);
    if(optioneleKlant.isPresent()){
        Klant klant=optioneleKlant.get();

    List<Order>orders=new ArrayList<>();
        if(datum.isPresent()){
Sort sort=Sort.by(Sort.Direction.ASC,"bestelDatum");
orders=OrderRepository.findByKlant_KlantNumber(klantId,sort);

            return ResponseEntity.ok().body(orders);
    }
        else{

            return ResponseEntity.ok().body(OrderRepository.findByKlant_KlantNumber(klantId,Sort.by("besteldatum")));
        }

}
    else{
        return ResponseEntity.notFound().build();
    }
}


}
