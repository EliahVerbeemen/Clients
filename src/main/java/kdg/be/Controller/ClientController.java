package kdg.be.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import kdg.be.Modellen.*;
import kdg.be.Modellen.DTO.ControllerDTO.ClientDTO;
import kdg.be.Modellen.DTO.ControllerDTO.LoyaltyClassDTO;
import kdg.be.Modellen.DTO.ControllerDTO.OrderDTO;
import kdg.be.Modellen.DTO.ControllerDTO.PurchaseOrderDTO;
import kdg.be.Modellen.Enums.ClientType;
import kdg.be.Modellen.Enums.OrderStatus;
import kdg.be.Services.*;
import kdg.be.RabbitMQ.RabbitSender;
import kdg.be.Xml.PurchaseOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

//TODO batch
@RestController
@RequestMapping("/api")
public class ClientController {
    private final ClientService clientService;
    private final OrderService orderService;
    private final ProductService productService;
    private final LoyaltyClassService loyaltyClassService;
    private final RabbitSender rabbitSender;

    private final FilterService filterService;
    Logger logger = LoggerFactory.getLogger(ClientController.class);

    public ClientController(ClientService clientService, OrderService orderService, ProductService productService, LoyaltyClassService loyaltyClassService
            , RabbitSender rabbitSender, FilterService filterService) {

        this.clientService = clientService;
        this.orderService = orderService;
        this.productService = productService;
        this.loyaltyClassService = loyaltyClassService;
        this.rabbitSender = rabbitSender;
        this.filterService=filterService;
    }


//, @RequestHeader(HttpHeaders.AUTHORIZATION) String language
    @GetMapping(value = "/client")
    public ResponseEntity<ClientDTO> GetCustomer()  {
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


        Client client = clientService.getClientByJwt(SecurityContextHolder.getContext());

            Client updatedClient = clientService.updateClient(client,geupdateklant);



            ClientDTO updatedClientDTO = new ClientDTO(updatedClient);
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedClientDTO);


        }






    @PostMapping(value = "/order", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> PlaceOrder(@RequestBody Order order) {

        Client client = clientService.getClientByJwt(SecurityContextHolder.getContext());
            Order createdOrder = orderService.createOrder(client, order.getProducts());
        if(createdOrder.getProducts().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "this order does not contain any products, the format is {\"products\":{\"productId\":quantity,\"productId2\":quantity2");

        }

            clientService.addOrderToClient(client, createdOrder);
            orderService.addOrderToClient(client, createdOrder);
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

                Order repeatedOrder = orderService.repeatOrder(client, order);
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


    public ResponseEntity<OrderDTO> confirmOrder(@PathVariable long orderId) {


        Client client = clientService.getClientByJwt(SecurityContextHolder.getContext());

        Optional<Order> orderOptional = orderService.getOrderById(orderId);
if(orderOptional.isPresent()){

    Order order = orderOptional.get();


    if(order.getClient().equals(client)) {


        if (!orderOptional.get().getOrderStatus().equals(OrderStatus.CONFIRMED)) {


            order.setOrderStatus(OrderStatus.CONFIRMED);


                client.setPoints((int) Math.round(client.getPoints() + order.getTotalPrice()) / 10);


            Order pricedOrder = orderService.setPriceInfo(order);
            orderService.saveOrder(pricedOrder);
            pricedOrder.setClient(null);
            rabbitSender.SendOrderToBaker(pricedOrder);
            return ResponseEntity.ok().body(new OrderDTO(pricedOrder));


        } else {


            throw new ResponseStatusException(HttpStatus.CONFLICT,"This order is already confirmed");


        }
    }
    else{

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }



}
else{

    throw new ResponseStatusException(HttpStatus.NO_CONTENT,"This order could not been found");

}






    }

    @PatchMapping("/order/cancel/{orderId}")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable(required = true) long orderId) {


        if (orderService.BeforeTenOClock()) {
            Optional<Order> orderOptional = orderService.getOrderById(orderId);
            if (orderOptional.isPresent()) {
                Order order = orderOptional.get();
                if (order.getOrderStatus() != OrderStatus.CONFIRMED) {
                    order.setOrderStatus(OrderStatus.CANCELLED);
                    this.orderService.saveOrder(order);
                    return ResponseEntity.ok().body(new OrderDTO(order));
                } else {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,"Orders that are confirmed, can't n cancelled");
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT,"This order could not been found");
            }
        } else {

            throw new ResponseStatusException(HttpStatus.CONFLICT,"Orders can't be cancelled after 10 O'Clock");
        }

    }

    @GetMapping("/loyality")
    public ResponseEntity<LoyaltyClassDTO> ObtainLoyalityInfo() {

        Client client=clientService.getClientByJwt(SecurityContextHolder.getContext());


            return ResponseEntity.ok().body(new LoyaltyClassDTO(loyaltyClassService.getLoyaltyClass(client.getPoints())));
        }




        @GetMapping("/order")
        public ResponseEntity<List<OrderDTO>> GetOrders( @RequestParam Optional<LocalDate> before, @RequestParam Optional<LocalDate> after,@RequestParam  Optional<Boolean>confirmed,Optional<Boolean>cancelled,Optional<Boolean>notconfirmed) {

            Client client = clientService.getClientByJwt(SecurityContextHolder.getContext());
           List<Order>orders= client.getOrders().stream().toList();
            orders=    this.filterService.filterOnState(orders,confirmed,cancelled,notconfirmed);
             orders=  filterService.dateFilter(orders,before,after);
            List<OrderDTO>pricedOrders=new ArrayList<>();
             orders.forEach(o-> {
                 OrderDTO orderDTO = new OrderDTO(orderService.setPriceInfo(o));
                orderDTO.setClient(null);
                 pricedOrders.add(orderDTO);

             });

            return ResponseEntity.status(HttpStatus.OK).body(pricedOrders);


    }

    @GetMapping(value = "/order",params = "current")
    public ResponseEntity<List<OrderDTO>> GetOrdersWithoutTwoDates( @RequestParam  Boolean current ,@RequestParam  Optional<Boolean>confirmed,Optional<Boolean>cancelled,Optional<Boolean>notconfirmed) {

        Client client = clientService.getClientByJwt(SecurityContextHolder.getContext());

        List<Order>orders=client.getOrders().stream().toList();
        orders=    this.filterService.filterOnState(orders,confirmed,cancelled,notconfirmed);

if(current.equals(true)){orders=orders.stream().filter(o -> o.getOrderDate().isAfter(LocalDate.now().minusDays(1))).collect(Collectors.toList());}



        List<OrderDTO>pricedOrders=new ArrayList<>();
        orders.forEach(o-> {
            OrderDTO orderDTO = new OrderDTO(orderService.setPriceInfo(o));
            orderDTO.setClient(null);
            pricedOrders.add(orderDTO);

        });


        return ResponseEntity.status(HttpStatus.OK).body(pricedOrders);


    }






 @GetMapping(value = "/products")
 //Postman ==> raw ==>1 (selectbox = json)
 public List<Product> GetMenu() {
     return this.productService.getAllProductsByProductState(ProductState.FINAL);
 }

    @PostMapping(value = "/xml",consumes = MediaType.APPLICATION_XML_VALUE)

    public PurchaseOrderDTO ReceiveOrderbatch(@RequestBody PurchaseOrder purchaseOrder) throws JsonProcessingException {

        if (purchaseOrder.getAccount().clientType == ClientType.B2B) {

            rabbitSender.SendPurchaseOrderToBaker(purchaseOrder);

            return new PurchaseOrderDTO(purchaseOrder);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

    }
    @RequestMapping("/**")
    public ResponseEntity<HttpStatusCode> notFound(){


       throw new ResponseStatusException(HttpStatus.NOT_FOUND,"the requested url does not exist");

    }



}
