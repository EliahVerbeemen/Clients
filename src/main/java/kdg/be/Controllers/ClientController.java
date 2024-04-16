package kdg.be.Controllers;

import kdg.be.AfterTenOClockError;
import kdg.be.Managers.ClientManager;
import kdg.be.Managers.LoyaltyClassManager;
import kdg.be.Managers.OrderManager;
import kdg.be.Managers.ProductManager;
import kdg.be.Modellen.Client;
import kdg.be.Modellen.DTO.ClientWithProducts;
import kdg.be.Modellen.DTO.ProductFromBakery;
import kdg.be.Modellen.LoyalityClasses;
import kdg.be.Modellen.Order;
import kdg.be.Modellen.Test;
import kdg.be.RabbitMQ.RabbitSender;
import kdg.be.testjes.ClientNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
//Elke oute begint met api (conventie voor api's)
@RequestMapping("/api")
public class ClientController {
    Logger logger = LoggerFactory.getLogger(ClientController.class);
    //Remove ClientRepo once ClientManager is done
    private final ClientManager clientMgr;
    private final OrderManager orderMgr;
    private final ProductManager productMgr;
    private final LoyaltyClassManager loyaltyclassMgr;
    private final RabbitSender rabbitSender;

    public ClientController(ClientManager clientManager, OrderManager orderManager, ProductManager productManager, LoyaltyClassManager loyaltyclassMgr
            , RabbitSender rabbitSender) {

        this.clientMgr = clientManager;
        this.orderMgr = orderManager;
        this.productMgr = productManager;
        this.loyaltyclassMgr = loyaltyclassMgr;
        this.rabbitSender = rabbitSender;

    }

    private static boolean BeforeTenOClock() {
        LocalTime hour = LocalTime.now();
        return hour.isBefore(LocalTime.of(22, 0, 0));

    }

    @PostMapping("/test")
    public ResponseEntity<Test> testRoute(@RequestBody Test test) {
        return ResponseEntity.status(HttpStatus.CREATED).body(test);
    }

    //Dus deze route bereik je via /api/klant
    //
    @GetMapping(value="/klant")
  public ResponseEntity<Client> GetCustomer(@RequestBody Long id) throws ClientNotFoundException {

        Optional<Client> client=clientMgr.getClientById(id);
        if(client.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(client.get());
        }
        else throw new ClientNotFoundException();//return ResponseEntity<>.status(HttpStatus.NOT_FOUND).body(ClientNotFoundException.class);



    }






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
      logger.debug(geupdateklant.toString());
        Optional<Client> oudeklant = this.clientMgr.getClientById(geupdateklant.getClientId());
        if (oudeklant.isPresent()) {
            Client klant = oudeklant.get();
            klant.setClientType(geupdateklant.getClientType());
            Client updatedClient = clientMgr.makeClient(klant);
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedClient);
        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
    }

    //TODO Splits deze methode op in kleinere
    // in het body bijvoorbeeld:  {"klantId":1,"order":{"1":3}}
    //Json [] voor een lijst en {} voor een map of object
    @PostMapping(value = "/order", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> PlaceOrder(@RequestBody ClientWithProducts clientWithProducts) {

        Optional<Client> optioneleKlant = clientMgr.getClientById(clientWithProducts.getClientId());
        if (optioneleKlant.isPresent()) {
            Client klant = optioneleKlant.get();
            Order order = orderMgr.CreateOrder(klant, clientWithProducts.getOrder());

            //TODO stuur AMPQ signaal naar batch (methode is al geshreven deserialisatie is misschien nog
            //foutief

         //   Client clientWithOrder = orderMgr.AddOrderToClient(klant, order);
        //     clientMgr.makeOrUpdateClient(clientWithOrder);
            clientMgr.addOrderToClient(klant,order);
            orderMgr.AddOrderToClient(klant,order);
            //Validatie geen lege bestellingen of totaalprijze van nul
            if (BeforeTenOClock()) {
                order.setClient(null);
                return ResponseEntity.status(HttpStatus.CREATED).body(order);
            } else {
                order.getRemarks().add("orders placed after 10.0 pm. will be delivered the next day");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(order);

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/order/history/{OrderId}")
    public ResponseEntity<Order> RepeatPreviousOrder(@RequestBody Long klantId, @PathVariable Long OrderId) {

        Optional<Order> orderOptional = orderMgr.getOrderById(OrderId);
        Optional<Client> clientOptional = clientMgr.getClientById(klantId);
        System.out.println(klantId);
        if (orderOptional.isPresent() && clientOptional.isPresent()) {
            Order previousOrder = orderOptional.get();
            Client klant = clientOptional.get();
            Order order = orderMgr.RepeatOrder(klant, previousOrder);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/order/confirm/{orderId}")
    // werkt met http://localhost:8080/api/order/bevestig/1
//E is geen order als je de database aanmaakt
    public ResponseEntity<Order> confirmOrder(@PathVariable long orderId) {

        Optional<Order> orderOptional = orderMgr.getOrderById(orderId);
        //TODO andere foutmelding als order reeds bevestigd is
        if (orderOptional.isPresent()&&orderOptional.get().getOrderStatus().equals(Order.OrderStatus.Niet_bevestigd)) {

            Order order = orderOptional.get();
            logger.debug(order.getClient().clientId.toString());

            order.setOrderStatus(Order.OrderStatus.Bevestigd);
            Optional<Client> clientOptional = clientMgr.getClientById(order.getClient().getClientId());
            if (clientOptional.isPresent()) {
                Client klant = clientOptional.get();
                klant.setPoints((int) Math.round(klant.getPoints() + order.getTotalPrice()) / 10);
            }

            Order pricedOrder = orderMgr.setPriceInfo(order);
            orderMgr.saveOrder(pricedOrder);
            orderMgr.getAllOrders().forEach(e -> logger.info(String.valueOf(e.getTotalPrice())));
            pricedOrder.setClient(null);
            rabbitSender.SendOrderToBaker(pricedOrder);
            return ResponseEntity.ok().body(pricedOrder);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/order/cancel/{orderId}")
    public ResponseEntity<Order> cancelOrder(@PathVariable(required = true) long orderId) {
        if (BeforeTenOClock()) {
            Optional<Order> orderOptional = orderMgr.getOrderById(orderId);
            if (orderOptional.isPresent()) {
                Order order = orderOptional.get();
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

    @GetMapping("/Klant/loyaliteit")
    public ResponseEntity<LoyalityClasses> ObtainLoyalityInfo(@RequestBody Long klantId) {
        Optional<Client> optioneleKlant = clientMgr.getClientById(klantId);

        if (optioneleKlant.isPresent()) {
            Client klant = optioneleKlant.get();
            return ResponseEntity.ok().body(loyaltyclassMgr.getLoyaltyClass(klant.getPoints()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //TODO eigenlijke sorteren werkt nog niet.
    //Schrijf in de klantenrepository een methode die een Sort accepteert
    @GetMapping("/order")
    public ResponseEntity<List<Order>> GetOrders(@RequestBody Long klantId, @RequestParam Optional<String> datum, @RequestParam Optional<String> status) {

        Optional<Client> clientOptional = clientMgr.getClientById(klantId);
        if (clientOptional.isPresent()) {
            Client klant = clientOptional.get();
            List<Order> orders = new ArrayList<>();
            if (datum.isPresent()) {
                Sort sort = Sort.by(Sort.Direction.ASC, "bestelDatum");
                orders = orderMgr.getAllOrdersByClientWithSort(klantId, sort);

                return ResponseEntity.ok().body(orders);
            } else {

                // WERKT @Fetch it eager
                return ResponseEntity.ok().body(klant.getOrders().stream().toList());
            }

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/testRabbit")
    public void TestRabbit() {

        Order order = orderMgr.getAllOrders().stream().toList().get(0);
        this.rabbitSender.SendOrderToBaker(order);
    }

 /*   @RabbitListener(queues = "newRecepiesQueue")
    public void ReceiveNerecepy(ProductFromBakery test){

System.out.println(test.getName());
        System.out.println(test.getProductId());
        System.out.println(test.getProductState());


    }*/
}
