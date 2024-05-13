package kdg.be.Controller;

import kdg.be.AfterTenOClockError;
import kdg.be.Modellen.DTO.ControllerDTO.ClientDTO;
import kdg.be.Modellen.DTO.ControllerDTO.LoyaltyClassDTO;
import kdg.be.Modellen.DTO.ControllerDTO.OrderDTO;
import kdg.be.Modellen.Enums.OrderStatus;
import kdg.be.Services.ClientService;
import kdg.be.Services.LoyalityClassService;
import kdg.be.Services.OrderService;
import kdg.be.Services.ProductService;
import kdg.be.Modellen.Client;
import kdg.be.Modellen.Order;
import kdg.be.Modellen.Test;
import kdg.be.RabbitMQ.RabbitSender;
import kdg.be.testjes.ClientNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class ClientController {
    private final ClientService clientService;
    private final OrderService orderService;
    private final ProductService productService;
    private final LoyalityClassService loyalityClassService;
    private final RabbitSender rabbitSender;
    Logger logger = LoggerFactory.getLogger(ClientController.class);

    public ClientController(ClientService clientService, OrderService orderService, ProductService productService, LoyalityClassService loyalityClassService
            , RabbitSender rabbitSender) {

        this.clientService = clientService;
        this.orderService = orderService;
        this.productService = productService;
        this.loyalityClassService = loyalityClassService;
        this.rabbitSender = rabbitSender;

    }


    @GetMapping("/test")
    public ResponseEntity<Test> testRoute() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
//, @RequestHeader(HttpHeaders.AUTHORIZATION) String language
    @GetMapping(value = "/client")
    public ResponseEntity<ClientDTO> GetCustomer() throws ClientNotFoundException {
     Client client= clientService.getClientByJwt(SecurityContextHolder.getContext());

            ClientDTO clientDTO = new ClientDTO(client);
            return ResponseEntity.status(HttpStatus.OK).body(clientDTO);
        }

    //Klant wordt automatish aangemaakt als token binnekomt, maar email niet gekend is.

    @DeleteMapping(value = "/client")
    //Postman ==> raw ==>1 (selectbox = json)
    public ResponseEntity<HttpStatus> DeleteCustomer() {

        if (clientService.doesClientExist(SecurityContextHolder.getContext())) {

            Client klant = clientService.getClientByJwt(SecurityContextHolder.getContext());

            clientService.removeClient(klant.getClientId());
            return ResponseEntity.status(HttpStatus.OK).build();

        }
        //Op deze manier krijgt de user een mooie foutmelding
         throw new ResponseStatusException(HttpStatus.NOT_FOUND,"user does not exist");// ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

    @PutMapping(value = "/client", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> UpdateClient(@RequestBody Client geupdateklant) {

        if (clientService.doesClientExist(SecurityContextHolder.getContext())) {
            Client client = clientService.getClientByJwt(SecurityContextHolder.getContext());


            client.setClientType(geupdateklant.getClientType());
            //TODO is dit ook een update?
            Client updatedClient = clientService.makeClient(client);
            ClientDTO updatedClientDTO = new ClientDTO(updatedClient);
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedClientDTO);


        }
        else{

              throw new ResponseStatusException(HttpStatus.NOT_FOUND,"user does not exist");// ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
    }

    //TODO Splits deze methode op in kleinere
    // in het body bijvoorbeeld:  {"klantId":1,"order":{"1":3}}
    //Json [] voor een lijst en {} voor een map of object

    //TODO ordervalidatie
    @PostMapping(value = "/order", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> PlaceOrder(@RequestBody Order order) {

        Client client = clientService.getClientByJwt(SecurityContextHolder.getContext());
            Order createdOrder = orderService.CreateOrder(client, order.getProducts());
        if(createdOrder.getProducts().size()==0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "this order does not contain any products, the format is {\"products\":{\"productId\":quantity,\"productId2\":quantity2");

        }

            clientService.addOrderToClient(client, createdOrder);
            orderService.AddOrderToClient(client, createdOrder);
        createdOrder.setClient(null);
        System.out.println(createdOrder.getProducts().size());

    if (orderService.BeforeTenOClock()) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new OrderDTO(createdOrder));
    } else {
        order.getRemarks().add("orders placed after 10.0 pm. will be delivered the next day");
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(new OrderDTO(createdOrder));





    }

    @PostMapping("/order/history/{OrderId}")
    public ResponseEntity<OrderDTO> RepeatPreviousOrder(@PathVariable Long OrderId)  {

        Optional<Order> orderOptional = orderService.getOrderById(OrderId);
        Client client = clientService.getClientByJwt(SecurityContextHolder.getContext());

        if (orderOptional.isPresent() ){
            Order order=orderOptional.get();

            if(order.getClient().equals(client)){

                Order repeatedOrder = orderService.RepeatOrder(client, order);
                return ResponseEntity.status(HttpStatus.CREATED).body(new OrderDTO(repeatedOrder));



            }
            else{

               return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() ;


            }


        }
        else{
return ResponseEntity.status(HttpStatus.NOT_FOUND).build() ;

        }




    }

    @PatchMapping("/order/confirm/{orderId}")
    // werkt met http://localhost:8080/api/order/bevestig/1
//E is geen order als je de database aanmaakt
    public ResponseEntity<OrderDTO> confirmOrder(@PathVariable long orderId) {
System.out.println("confirm");

        Client client = clientService.getClientByJwt(SecurityContextHolder.getContext());

        Optional<Order> orderOptional = orderService.getOrderById(orderId);
if(orderOptional.isPresent()){

    Order order = orderOptional.get();


    if(order.getClient().equals(client)) {


        if (!orderOptional.get().getOrderStatus().equals(OrderStatus.Bevestigd)) {


            order.setOrderStatus(OrderStatus.Bevestigd);


                client.setPoints((int) Math.round(client.getPoints() + order.getTotalPrice()) / 10);


            Order pricedOrder = orderService.setPriceInfo(order);
            orderService.saveOrder(pricedOrder);
            pricedOrder.setClient(null);
            rabbitSender.SendOrderToBaker(pricedOrder);
            return ResponseEntity.ok().body(new OrderDTO(pricedOrder));


        } else {


            return ResponseEntity.status(409).build();

        }
    }
    else{

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }



}
else{

    return  ResponseEntity.notFound().build();

}






    }

    @PatchMapping("/order/cancel/{orderId}")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable(required = true) long orderId) {


        if (orderService.BeforeTenOClock()) {
            Optional<Order> orderOptional = orderService.getOrderById(orderId);
            if (orderOptional.isPresent()) {
                Order order = orderOptional.get();
                if (order.getOrderStatus() != OrderStatus.Bevestigd) {
                    order.setOrderStatus(OrderStatus.Geannulleerd);
                    this.orderService.saveOrder(order);
                    return ResponseEntity.ok().body(new OrderDTO(order));
                } else {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,"Orders that are confirmed, can't n cancelled");
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"This order could not been found");
            }
        } else {

            throw new ResponseStatusException(HttpStatus.CONFLICT,"Orders can't be cancelled after 10 O'Clock");
        }

    }

    @GetMapping("/loyality")
    public ResponseEntity<LoyaltyClassDTO> ObtainLoyalityInfo() {

        Client client=clientService.getClientByJwt(SecurityContextHolder.getContext());


            return ResponseEntity.ok().body(new LoyaltyClassDTO(loyalityClassService.getLoyaltyClass(client.getPoints())));
        }


    //TODO eigenlijke sorteren werkt nog niet.
    //Schrijf in de klantenrepository een methode die een Sort accepteert

        @GetMapping("/order")
        public ResponseEntity<List<Order>> GetOrders( @RequestParam Optional<LocalDate> before, @RequestParam Optional<LocalDate> after,@RequestParam  Optional<Boolean>confirmed,Optional<Boolean>cancelled) {

            Client client = clientService.getClientByJwt(SecurityContextHolder.getContext());


                return ResponseEntity.status(HttpStatus.OK).body(client.getClientOrders().stream().toList());

    }

    @GetMapping(value = "/order",params = "current")
    public ResponseEntity<List<Order>> GetOrdersWithoutTwoDates( @RequestParam  Boolean current ,@RequestParam  Optional<Boolean>confirmed,Optional<Boolean>cancelled) {

        Client client = clientService.getClientByJwt(SecurityContextHolder.getContext());










        return ResponseEntity.status(HttpStatus.OK).body(client.getClientOrders().stream().toList());


    }



    @GetMapping("/testRabbit")
    public void TestRabbit() {

        Order order = orderService.getAllOrders().stream().toList().get(0);
        this.rabbitSender.SendOrderToBaker(order);
    }

 /*   @RabbitListener(queues = "newRecepiesQueue")
    public void ReceiveNerecepy(ProductFromBakery test){

System.out.println(test.getName());
        System.out.println(test.getProductId());
        System.out.println(test.getProductState());


    }*/


}
