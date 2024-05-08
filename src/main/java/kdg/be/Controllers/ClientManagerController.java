package kdg.be.Controllers;

import jakarta.annotation.security.RolesAllowed;
import kdg.be.Managers.LoyaltyClassManager;
import kdg.be.Managers.ProductManager;
import kdg.be.Modellen.*;
import kdg.be.Repositories.ClientRepository;
import kdg.be.Repositories.OrderRepository;
import kdg.be.Repositories.ProductRepository;
import kdg.be.testjes.ProductNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClientManagerController {

    private final LoyaltyClassManager loyaltyClassManager;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    private final ProductManager productManager;
    private final OrderRepository orderRepository;

    public ClientManagerController(LoyaltyClassManager loyaltyClassManager, ProductManager productManager, ClientRepository clientRepository, ProductRepository productRepository
            , OrderRepository orderRepository) {
        this.loyaltyClassManager = loyaltyClassManager;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.productManager = productManager;
    }

    @GetMapping("/loyality")
    @RolesAllowed("clientmanager")
    public List<LoyalityClasses> ShowLoyalityClasses() {

        return loyaltyClassManager.findAll();


    }

    @PostMapping("/loyality/create")
    @RolesAllowed("clientmanager")
    public List<LoyalityClasses> CreateLoyalityClass(@RequestBody LoyalityClasses loyalityClasses) {

        loyaltyClassManager.save(loyalityClasses);

        return loyaltyClassManager.findAll();


    }

    @GetMapping("/customers")
    @RolesAllowed("clientmanager")
    public List<Client> AllCustomers() {

        return clientRepository.findAll();


    }

    @GetMapping("/products/products")
    @RolesAllowed("clientmanager")
    public List<Product> AllProducts() {

        return productRepository.findAll();


    }

    @GetMapping("/products/new")
    @RolesAllowed("clientmanager")
    public List<Product> AllNewProducs() {

        return productRepository.findProductsBy_productState(ProductState.Nieuw);


    }

    @PutMapping("/products/price/{productId}")
    @RolesAllowed("clientmanager")
    public Product SetPriceOfProduct(@PathVariable int productId, @RequestBody double price) throws ProductNotFoundException {


        return productManager.UpdatePriceAndActivate((long) productId, (float) price);


    }

    @GetMapping("/orders/all")
    @RolesAllowed("clientmanager")
    public List<Order> AllOrders() {

        return orderRepository.findAll();


    }

        @GetMapping("/report")
    @RolesAllowed("clientmanager")

    public List<Order> getReport(@RequestParam Optional<List<Long>>clientIds,@RequestParam Optional<List<Long>>productIds,@RequestParam Optional<LocalDate>before,@RequestParam Optional<LocalDate>after){
     List<Order> orders=  orderRepository.findAll();
        orders=filterService.dateFilter(orders,before,after);
orders=filterService.productFilter(orders,productIds);
orders=filterService.clientsFilter(orders,clientIds);
return orders;
    }

}
