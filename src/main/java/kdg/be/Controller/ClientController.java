package kdg.be.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import kdg.be.Modellen.Client;
import kdg.be.Modellen.DTO.ControllerDTO.ClientDTO;
import kdg.be.Modellen.DTO.ControllerDTO.LoyaltyClassDTO;
import kdg.be.Modellen.DTO.ControllerDTO.OrderDTO;
import kdg.be.Modellen.DTO.ControllerDTO.PurchaseOrderDTO;
import kdg.be.Modellen.Enums.ClientType;
import kdg.be.Modellen.Enums.OrderStatus;
import kdg.be.Modellen.Order;
import kdg.be.Modellen.Product;
import kdg.be.Modellen.ProductState;
import kdg.be.RabbitMQ.RabbitSender;
import kdg.be.Services.*;
import kdg.be.Xml.PurchaseOrder;
import org.antlr.v4.runtime.misc.NotNull;
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
        this.filterService = filterService;
    }

    @GetMapping(value = "/client")
    public ResponseEntity<ClientDTO> getCustomer() {
        Client client = clientService.getClientByJwt(SecurityContextHolder.getContext());
        ClientDTO clientDTO = new ClientDTO(client);
        logger.info("User " + clientDTO.getClientId() + " returned");
        return ResponseEntity.status(HttpStatus.OK).body(clientDTO);
    }


    @DeleteMapping(value = "/client")
    public ResponseEntity<HttpStatus> deleteCustomer() {
        if (clientService.doesClientExist(SecurityContextHolder.getContext())) {
            Client client = clientService.getClientByJwt(SecurityContextHolder.getContext());
            logger.info("Start to delete user: " + client.getClientId());
            clientService.removeClient(client.getClientId());
            logger.info("User deleted");
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        logger.error("User not found during delete");
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user does not exist");
    }

    @PutMapping(value = "/client", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDTO> updateClient(@RequestBody Client update) {
        Client client = clientService.getClientByJwt(SecurityContextHolder.getContext());
        Client updatedClient = clientService.updateClient(client, update);
        ClientDTO updatedClientDTO = new ClientDTO(updatedClient);
        logger.info("Client updated: " + client.clientId);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedClientDTO);
    }


    @PostMapping(value = "/order", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> placeOrder(@RequestBody Order order) {
        Client client = clientService.getClientByJwt(SecurityContextHolder.getContext());
        Order createdOrder = orderService.createOrder(client, order.getProducts());
        if (createdOrder.getProducts().isEmpty()) {
            logger.warn("Empty order was submitted.");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "this order does not contain any products, the format is {\"products\":{\"productId\":quantity,\"productId2\":quantity2");
        }
        logger.info("Order placed: " + order.getOrderId() + " with products: " + order.getProducts());
        clientService.addOrderToClient(client, createdOrder);
        orderService.addOrderToClient(client, createdOrder);
        createdOrder.setClient(null);
        logger.debug("" + createdOrder.getProducts().size());

        if (orderService.BeforeTenOClock()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(new OrderDTO(createdOrder));
        } else {
            order.getRemarks().add("orders placed after 10.0 pm. will be delivered the next day");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new OrderDTO(createdOrder));
    }

    @PostMapping("/order/history/{OrderId}")
    public ResponseEntity<OrderDTO> repeatPreviousOrder(@PathVariable Long OrderId) {
        Optional<Order> orderOptional = orderService.getOrderById(OrderId);
        Client client = clientService.getClientByJwt(SecurityContextHolder.getContext());
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (order.getClient().equals(client)) {
                Order repeatedOrder = orderService.repeatOrder(client, order);
                logger.info("Order repeated: " + repeatedOrder.getOrderId());
                return ResponseEntity.status(HttpStatus.CREATED).body(new OrderDTO(repeatedOrder));
            } else {
                logger.warn("Client trying to repeat order of other client");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            logger.info("Order not found to repeat");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/order/confirm/{orderId}")
    public ResponseEntity<OrderDTO> confirmOrder(@PathVariable long orderId) {
        Client client = clientService.getClientByJwt(SecurityContextHolder.getContext());
        Optional<Order> orderOptional = orderService.getOrderById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (order.getClient().equals(client)) {
                if (!orderOptional.get().getOrderStatus().equals(OrderStatus.CONFIRMED)) {
                    order.setOrderStatus(OrderStatus.CONFIRMED);
                    client.setPoints((int) Math.round(client.getPoints() + order.getTotalPrice()) / 10);
                    Order pricedOrder = orderService.setPriceInfo(order);
                    orderService.saveOrder(pricedOrder);
                    pricedOrder.setClient(null);
                    rabbitSender.SendOrderToBaker(pricedOrder);
                    logger.info("Order has been confirmed and sent to bakery: " + pricedOrder.getOrderId());
                    return ResponseEntity.ok().body(new OrderDTO(pricedOrder));
                } else {
                    logger.info("Order already confirmed");
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "This order is already confirmed");
                }
            } else {
                logger.warn("User not authorized for this order");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            logger.info("Order not found");
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "This order could not been found");
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
                    logger.info("Order has been cancelled: " + order.getOrderId());
                    return ResponseEntity.ok().body(new OrderDTO(order));
                } else {
                    logger.info("Order cannot be cancelled anymore: " + order.getOrderId());
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Orders that are confirmed, can't be cancelled");
                }
            } else {
                logger.info("Order could not be found: " + orderId);
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "This order could not been found");
            }
        } else {
            logger.info("Orders cannot be cancelled after 10 O'Clock: " + orderId);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Orders can't be cancelled after 10 O'Clock");
        }
    }

    @GetMapping("/loyalty")
    public ResponseEntity<LoyaltyClassDTO> obtainLoyaltyInfo() {
        Client client = clientService.getClientByJwt(SecurityContextHolder.getContext());
        logger.info("Loyalty info for client provided: " + client.getClientId() + " " + loyaltyClassService.getLoyaltyClass(client.getPoints()));
        return ResponseEntity.ok().body(new LoyaltyClassDTO(loyaltyClassService.getLoyaltyClass(client.getPoints())));
    }


    @GetMapping("/order")
    public ResponseEntity<List<OrderDTO>> getOrders(@RequestParam Optional<LocalDate> before, @RequestParam Optional<LocalDate> after, @RequestParam Optional<Boolean> confirmed, Optional<Boolean> cancelled, Optional<Boolean> notconfirmed) {
        Client client = clientService.getClientByJwt(SecurityContextHolder.getContext());
        List<Order> orders = client.getOrders().stream().toList();
        orders = this.filterService.filterOnState(orders, confirmed, cancelled, notconfirmed);
        orders = filterService.dateFilter(orders, before, after);
        logger.info("Orders requested for client: " + client.getClientId());
        return ordersToOrderDTOResponse(orders);
    }

    @NotNull
    private ResponseEntity<List<OrderDTO>> ordersToOrderDTOResponse(List<Order> orders) {
        List<OrderDTO> pricedOrders = new ArrayList<>();
        orders.forEach(o -> {
            OrderDTO orderDTO = new OrderDTO(orderService.setPriceInfo(o));
            orderDTO.setClient(null);
            pricedOrders.add(orderDTO);
        });
        return ResponseEntity.status(HttpStatus.OK).body(pricedOrders);
    }

    @GetMapping(value = "/order", params = "current")
    public ResponseEntity<List<OrderDTO>> getOrdersWithoutTwoDates(@RequestParam Boolean current, @RequestParam Optional<Boolean> confirmed, Optional<Boolean> cancelled, Optional<Boolean> notconfirmed) {
        Client client = clientService.getClientByJwt(SecurityContextHolder.getContext());
        List<Order> orders = client.getOrders().stream().toList();
        orders = this.filterService.filterOnState(orders, confirmed, cancelled, notconfirmed);
        if (current.equals(true)) {
            orders = orders.stream().filter(o -> o.getOrderDate().isAfter(LocalDate.now().minusDays(1))).collect(Collectors.toList());
        }
        logger.info("Orders requested without date for client: " + client.getClientId());
        return ordersToOrderDTOResponse(orders);
    }


    @GetMapping(value = "/products")
    public List<Product> getMenu() {
        logger.info("Menu sent");
        return this.productService.getAllProductsByProductState(ProductState.FINAL);
    }

    @PostMapping(value = "/xml", consumes = MediaType.APPLICATION_XML_VALUE)
    public PurchaseOrderDTO receiveOrderBatch(@RequestBody PurchaseOrder purchaseOrder) throws JsonProcessingException {
        if (purchaseOrder.getAccount().clientType == ClientType.B2B) {
            rabbitSender.SendPurchaseOrderToBaker(purchaseOrder);
            logger.info("Order batch sent to bakery");
            return new PurchaseOrderDTO(purchaseOrder);
        } else {
            logger.warn("User not authorized for OrderBatch");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping("/**")
    public ResponseEntity<HttpStatusCode> notFound() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "the requested url does not exist");
    }
}
