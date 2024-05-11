package kdg.be.Controllers;

import kdg.be.AfterTenOClockError;
import kdg.be.Modellen.DTO.ControllerDTO.ClientDTO;
import kdg.be.Modellen.DTO.ControllerDTO.LoyaltyClassDTO;
import kdg.be.Modellen.DTO.ControllerDTO.OrderDTO;
import kdg.be.Modellen.Enums.OrderStatus;
import kdg.be.Services.ClientService;
import kdg.be.Services.LoyaltyClassService;
import kdg.be.Services.OrderService;
import kdg.be.Services.ProductService;
import kdg.be.Modellen.Client;
import kdg.be.Modellen.DTO.ClientWithProducts;
import kdg.be.Modellen.LoyaltyClass;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class ClientController {
    private final ClientService clientService;
    private final OrderService orderService;
    private final ProductService productService;
    private final LoyaltyClassService loyaltyClassService;
    private final RabbitSender rabbitSender;
    Logger logger = LoggerFactory.getLogger(ClientController.class);

    public ClientController(ClientService clientService, OrderService orderService, ProductService productService, LoyaltyClassService loyaltyClassService
            , RabbitSender rabbitSender) {

        this.clientService = clientService;
        this.orderService = orderService;
        this.productService = productService;
        this.loyaltyClassService = loyaltyClassService;
        this.rabbitSender = rabbitSender;

    }


    @PostMapping("/test")
    public ResponseEntity<Test> testRoute(@RequestBody Test test) {
        return ResponseEntity.status(HttpStatus.CREATED).body(test);
    }

    @GetMapping(value = "/client")
    public ResponseEntity<ClientDTO> GetCustomer(@RequestBody Long id) throws ClientNotFoundException {

        Optional<Client> client = clientService.getClientById(id);
        if (client.isPresent()) {
            ClientDTO clientDTO = new ClientDTO(client.get());
            return ResponseEntity.status(HttpStatus.OK).body(clientDTO);
        } else
            throw new ClientNotFoundException();//return ResponseEntity<>.status(HttpStatus.NOT_FOUND).body(ClientNotFoundException.class);


    }

    @PostMapping(value = "/client")
    public ResponseEntity<ClientDTO> CreateCustomer(@RequestBody Client klant) {
        Client newClient = this.clientService.makeClient(klant);
        ClientDTO cLientDTO = new ClientDTO(newClient);
        return ResponseEntity.status(HttpStatus.CREATED).body(cLientDTO);
    }

    @DeleteMapping(value = "/client")
    //Postman ==> raw ==>1 (selectbox = json)
    public HttpStatus DeleteCustomer(@RequestBody Long klantId) {
        Optional<Client> klant = clientService.getClientById(klantId);
        if (klant.isPresent()) {

            clientService.removeClient(klantId);
            return HttpStatus.OK;
        } else {
            return HttpStatus.NOT_FOUND;
        }
    }

    @PutMapping(value = "/client", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> UpdateClient(@RequestBody Client geupdateklant) {
        logger.debug(geupdateklant.toString());
        Optional<Client> oudeklant = this.clientService.getClientById(geupdateklant.getClientId());
        if (oudeklant.isPresent()) {
            Client klant = oudeklant.get();
            klant.setClientType(geupdateklant.getClientType());
            Client updatedClient = clientService.makeClient(klant);
            ClientDTO updatedClientDTO = new ClientDTO(updatedClient);
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedClientDTO);
        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
    }

    //TODO Splits deze methode op in kleinere
    // in het body bijvoorbeeld:  {"klantId":1,"order":{"1":3}}
    //Json [] voor een lijst en {} voor een map of object
    @PostMapping(value = "/order", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> PlaceOrder(@RequestBody ClientWithProducts clientWithProducts) {

        Optional<Client> optioneleKlant = clientService.getClientById(clientWithProducts.getClientId());
        if (optioneleKlant.isPresent()) {
            Client klant = optioneleKlant.get();
            Order order = orderService.CreateOrder(klant, clientWithProducts.getOrder());

            //TODO stuur AMPQ signaal naar batch (methode is al geshreven deserialisatie is misschien nog
            //foutief

            //   Client clientWithOrder = orderMgr.AddOrderToClient(klant, order);
            //     clientMgr.makeOrUpdateClient(clientWithOrder);
            clientService.addOrderToClient(klant, order);
            orderService.AddOrderToClient(klant, order);
            //Validatie geen lege bestellingen of totaalprijze van nul
            if (orderService.BeforeTenOClock()) {
                order.setClient(null);
                return ResponseEntity.status(HttpStatus.CREATED).body(new OrderDTO(order));
            } else {
                order.getRemarks().add("orders placed after 10.0 pm. will be delivered the next day");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(new OrderDTO(order));

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/order/history/{OrderId}")
    public ResponseEntity<OrderDTO> RepeatPreviousOrder(@RequestBody Long klantId, @PathVariable Long OrderId) {

        Optional<Order> orderOptional = orderService.getOrderById(OrderId);
        Optional<Client> clientOptional = clientService.getClientById(klantId);
        System.out.println(klantId);
        if (orderOptional.isPresent() && clientOptional.isPresent()) {
            Order previousOrder = orderOptional.get();
            Client klant = clientOptional.get();
            Order order = orderService.RepeatOrder(klant, previousOrder);
            return ResponseEntity.status(HttpStatus.CREATED).body(new OrderDTO(order));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/order/confirm/{orderId}")
    // werkt met http://localhost:8080/api/order/bevestig/1
//E is geen order als je de database aanmaakt
    public ResponseEntity<OrderDTO> confirmOrder(@PathVariable long orderId) {

        Optional<Order> orderOptional = orderService.getOrderById(orderId);
        //TODO andere foutmelding als order reeds bevestigd is
        if (orderOptional.isPresent() && orderOptional.get().getOrderStatus().equals(OrderStatus.Niet_bevestigd)) {

            Order order = orderOptional.get();
            logger.debug(order.getClient().clientId.toString());

            order.setOrderStatus(OrderStatus.Bevestigd);
            Optional<Client> clientOptional = clientService.getClientById(order.getClient().getClientId());
            if (clientOptional.isPresent()) {
                Client klant = clientOptional.get();
                klant.setPoints((int) Math.round(klant.getPoints() + order.getTotalPrice()) / 10);
            }

            Order pricedOrder = orderService.setPriceInfo(order);
            orderService.saveOrder(pricedOrder);
            orderService.getAllOrders().forEach(e -> logger.info(String.valueOf(e.getTotalPrice())));
            pricedOrder.setClient(null);
            rabbitSender.SendOrderToBaker(pricedOrder);
            return ResponseEntity.ok().body(new OrderDTO(pricedOrder));
        } else {
            return ResponseEntity.notFound().build();
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
                    return ResponseEntity.ok().body(new OrderDTO(order));
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

    @GetMapping("/client/loyalty")
    public ResponseEntity<LoyaltyClassDTO> ObtainLoyalityInfo(@RequestBody Long klantId) {
        Optional<Client> optioneleKlant = clientService.getClientById(klantId);

        if (optioneleKlant.isPresent()) {
            Client klant = optioneleKlant.get();
            return ResponseEntity.ok().body(new LoyaltyClassDTO(loyaltyClassService.getLoyaltyClass(klant.getPoints())));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //TODO eigenlijke sorteren werkt nog niet.
    //Schrijf in de klantenrepository een methode die een Sort accepteert
    @GetMapping("/order")
    public ResponseEntity<List<OrderDTO>> GetOrders(@RequestBody Long klantId, @RequestParam Optional<String> datum, @RequestParam Optional<String> status) {

        Optional<Client> clientOptional = clientService.getClientById(klantId);
        if (clientOptional.isPresent()) {
            Client klant = clientOptional.get();
            List<Order> orders;
            if (datum.isPresent()) {
                Sort sort = Sort.by(Sort.Direction.ASC, "bestelDatum");
                orders = orderService.getAllOrdersByClientWithSort(klantId, sort);
                List<OrderDTO> orderDTOList = new ArrayList<>();
                orders.forEach(order -> {
                    orderDTOList.add(new OrderDTO(order));
                });

                return ResponseEntity.ok().body(orderDTOList);
            } else {
                List <Order> clientOrdersNoSort = klant.getOrders().stream().toList();
                List<OrderDTO> orderDTOListNoSort = new ArrayList<>();
                clientOrdersNoSort.forEach(order -> {
                    orderDTOListNoSort.add(new OrderDTO(order));
                });
                // WERKT @Fetch it eager
                return ResponseEntity.ok().body(orderDTOListNoSort);
            }

        } else {
            return ResponseEntity.notFound().build();
        }
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
