package kdg.be.Controllers;

import kdg.be.Managers.LoyalityClassManager;
import kdg.be.Modellen.Client;
import kdg.be.Modellen.LoyalityClasses;
import kdg.be.Modellen.Order;
import kdg.be.Modellen.Product;
import kdg.be.Repositories.ClientRepository;
import kdg.be.Repositories.OrderRepository;
import kdg.be.Repositories.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClientManagerController {

private LoyalityClassManager loyalityClassManager;
private ClientRepository clientRepository;
private ProductRepository productRepository;
private OrderRepository orderRepository;

  public ClientManagerController(LoyalityClassManager loyalityClassManager, ClientRepository clientRepository, ProductRepository productRepository
  , OrderRepository orderRepository){
      this.loyalityClassManager=loyalityClassManager;
this.clientRepository = clientRepository;
this.productRepository=productRepository;
this.orderRepository=orderRepository;
  }

  @GetMapping("/loyality")
public List<LoyalityClasses> ShowLoyalityClasses(){

    return loyalityClassManager.findAll();


    }

    @PostMapping("/loyality/create")
    public List<LoyalityClasses> CreateLoyalityClass(@RequestBody LoyalityClasses loyalityClasses){

        loyalityClassManager.save(loyalityClasses);

        return loyalityClassManager.findAll();


    }
    @GetMapping("/customers")
    public List<Client> AllCustomers(){

        return clientRepository.findAll();


    }
    @GetMapping("/products/products")
    public List<Product> AllProducs(){

       return productRepository.findAll();


    }
    @GetMapping("/orders/all")
    public List<Order> AllOrders(){

        return orderRepository.findAll();


    }

}
