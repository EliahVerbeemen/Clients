package kdg.be.Controllers;

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

private LoyaltyClassManager loyaltyClassManager;
private ClientRepository clientRepository;
private ProductRepository productRepository;

private ProductManager productManager;
private OrderRepository orderRepository;

  public ClientManagerController(LoyaltyClassManager loyaltyClassManager,ProductManager productManager, ClientRepository clientRepository, ProductRepository productRepository
  , OrderRepository orderRepository){
      this.loyaltyClassManager = loyaltyClassManager;
this.clientRepository = clientRepository;
this.productRepository=productRepository;
this.orderRepository=orderRepository;
this.productManager=productManager;
  }

  @GetMapping("/loyality")
public List<LoyalityClasses> ShowLoyalityClasses(){

    return loyaltyClassManager.findAll();


    }

    @PostMapping("/loyality/create")
    public List<LoyalityClasses> CreateLoyalityClass(@RequestBody LoyalityClasses loyalityClasses){

        loyaltyClassManager.save(loyalityClasses);

        return loyaltyClassManager.findAll();


    }
    @GetMapping("/customers")
    public List<Client> AllCustomers(){

        return clientRepository.findAll();


    }
    @GetMapping("/products/products")
    public List<Product> AllProducs(){

       return productRepository.findAll();


    }
    @GetMapping("/products/new")
    public List<Product> AllNewProducs(){

        return productRepository.findProductsBy_productState(ProductState.Nieuw);


    }
    @GetMapping("/products/price/{productId}")
    public Product SetPriceOfProduct(@PathVariable int productId,@RequestBody double price) throws ProductNotFoundException {




        return productManager.UpdatePriceAndActivate((long) productId, (float) price);


    }
    @GetMapping("/orders/all")
    public List<Order> AllOrders(){

        return orderRepository.findAll();


    }

}
