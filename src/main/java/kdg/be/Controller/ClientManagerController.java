package kdg.be.Controller;

import jakarta.annotation.security.RolesAllowed;
import kdg.be.Services.FilterService;
import kdg.be.Services.LoyalityClassService;
import kdg.be.Services.ProductService;
import kdg.be.Modellen.*;
import kdg.be.Repositories.ClientRepository;
import kdg.be.Repositories.OrderRepository;
import kdg.be.Repositories.ProductRepository;
import kdg.be.testjes.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/internal")
public class ClientManagerController {

    private final LoyalityClassService loyalityClassService;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    private final ProductService productService;
    private final OrderRepository orderRepository;

    private final FilterService filterService;

    public ClientManagerController(LoyalityClassService loyalityClassService, ProductService productService, ClientRepository clientRepository, ProductRepository productRepository
            , OrderRepository orderRepository, FilterService filterService) {
        this.loyalityClassService = loyalityClassService;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.filterService = filterService;
    }

    @GetMapping("/loyality")
    @RolesAllowed("clientmanager")
    public List<LoyalityClass> ShowLoyalityClasses() {
        return loyalityClassService.findAll();


    }

    @PostMapping("/loyalty/create")
    @RolesAllowed("clientmanager")
    public List<LoyalityClass> CreateLoyalityClass(@RequestBody LoyalityClass loyalityClass) {

        loyalityClassService.save(loyalityClass);

        return loyalityClassService.findAll();


    }

    @GetMapping("/customers")
    @RolesAllowed("clientmanager")
    public List<Client> AllCustomers() {

        return clientRepository.findAll();


    }

    @GetMapping("/products/products")
   // @RolesAllowed("clientmanager")
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

   Optional<Product> updatedProduct=productService.UpdatePriceAndActivate((long) productId, (float) price);
       if(updatedProduct.isPresent()){

           return updatedProduct.get();
       }
       else{
           throw new ResponseStatusException(HttpStatus.NO_CONTENT,"This order could not been found");
       }



    }

    @GetMapping("/orders/all")
    @RolesAllowed("clientmanager")
    public List<Order> AllOrders() {

        return orderRepository.findAll();


    }

    @GetMapping("/report")
    @RolesAllowed("clientmanager")

    public List<Order> getReport(@RequestParam Optional<List<Long>> clientIds, @RequestParam Optional<List<Long>> productIds, @RequestParam Optional<LocalDate> before, @RequestParam Optional<LocalDate> after) {
        List<Order> orders = orderRepository.findAll();
        orders = filterService.dateFilter(orders, before, after);
        orders = filterService.productFilter(orders, productIds);
        orders = filterService.clientsFilter(orders, clientIds);
        return orders;
    }

}
