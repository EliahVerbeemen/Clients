package kdg.be.Controllers;

import kdg.be.Managers.LoyalityClassManager;
import kdg.be.Modellen.Customer;
import kdg.be.Modellen.LoyalityClasses;
import kdg.be.Modellen.Order;
import kdg.be.Modellen.Product;
import kdg.be.Repositories.KlantRepository;
import kdg.be.Repositories.OrderRepository;
import kdg.be.Repositories.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerManagerController {

private LoyalityClassManager loyalityClassManager;
private KlantRepository klantRepository;
private ProductRepository productRepository;
private OrderRepository orderRepository;

  public  CustomerManagerController(LoyalityClassManager loyalityClassManager, KlantRepository klantRepository, ProductRepository productRepository
  ,OrderRepository orderRepository){
      this.loyalityClassManager=loyalityClassManager;
this.klantRepository=klantRepository;
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
    public List<Customer> AllCustomers(){

        return klantRepository.findAll();


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
