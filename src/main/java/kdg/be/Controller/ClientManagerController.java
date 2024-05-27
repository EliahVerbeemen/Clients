package kdg.be.Controller;

import jakarta.annotation.security.RolesAllowed;
import kdg.be.Modellen.*;
import kdg.be.Modellen.DTO.ControllerDTO.ClientDTO;
import kdg.be.Services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/internal")
public class ClientManagerController {
    private final LoyaltyClassService loyaltyClassService;
    private final ClientService clientService;
    private final ProductService productService;
    private final FilterService filterService;
    private final OrderService orderService;
    private final Logger logger = LoggerFactory.getLogger(ClientManagerController.class);

    public ClientManagerController(LoyaltyClassService loyaltyClassService, ClientService clientService, ProductService productService, FilterService filterService, OrderService orderService) {
        this.loyaltyClassService = loyaltyClassService;
        this.clientService = clientService;
        this.productService = productService;
        this.filterService = filterService;
        this.orderService = orderService;
    }

    @GetMapping("/loyalty")
    @RolesAllowed("clientmanager")
    public List<LoyaltyClass> showLoyaltyClasses() {
        logger.info("Loyalty classes returned");
        return loyaltyClassService.findAll();
    }

    @PostMapping("/loyalty/create")
    @RolesAllowed("clientmanager")
    public List<LoyaltyClass> createLoyaltyClass(@RequestBody LoyaltyClass loyaltyClass) {
        loyaltyClassService.createLoyaltyClass(loyaltyClass);
        logger.info("Loyalty Class created: " + loyaltyClass.getName());
        return loyaltyClassService.findAll();
    }

    @GetMapping("/customers")
    @RolesAllowed("clientmanager")
    public List<Client> getAllClients() {
        logger.info("List of clients returned");
        return clientService.getAllClients();
    }

    @GetMapping("/products/products")
    @RolesAllowed("clientmanager")
    public List<Product> getAllProducts() {
        logger.info("List of products returned");
        return productService.getAllProducts();
    }

    @GetMapping("/products/new")
    @RolesAllowed("clientmanager")
    public List<Product> getNewProducts() {
        logger.info("List of NEW products returned");
        return productService.getProductsByState(ProductState.NEW);
    }

    @PutMapping("/products/price/{productId}")
    @RolesAllowed("clientmanager")
    public Product setPriceOfProduct(@PathVariable int productId, @RequestBody double price) {
        Optional<Product> updatedProduct = productService.updatePriceAndActivate((long) productId, (float) price);
        if (updatedProduct.isPresent()) {
            logger.info("Product has been updated: " + productId);
            return updatedProduct.get();
        } else {
            logger.warn("Product to update with ID " + productId + " could not be found.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This product could not been found");
        }
    }

    @GetMapping("/customer/b2b")
    @RolesAllowed("clientmanager")
    public ClientDTO clientToB2B(String username) {
        logger.info("Client with username " + username + " has been updated to B2B");
        return new ClientDTO(clientService.updateToB2B(username));
    }


    @GetMapping("/orders/all")
    @RolesAllowed("clientmanager")
    public List<Order> getAllOrders() {
        logger.info("All order returned");
        return orderService.getAllOrders();
    }

    @GetMapping("/report")
    @RolesAllowed("clientmanager")
    public List<Order> getReport(@RequestParam Optional<List<Long>> clientIds, @RequestParam Optional<List<Long>> productIds, @RequestParam Optional<LocalDate> before, @RequestParam Optional<LocalDate> after) {
        List<Order> orders = orderService.getAllOrders();
        orders = filterService.dateFilter(orders, before, after);
        orders = filterService.productFilter(orders, productIds);
        orders = filterService.clientsFilter(orders, clientIds);
        logger.info("Order report generated.");
        return orders;
    }

}
