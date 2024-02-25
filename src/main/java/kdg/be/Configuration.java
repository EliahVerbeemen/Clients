package kdg.be;

import kdg.be.Managers.LoyalityClassManager;
import kdg.be.Modellen.Customer;
import kdg.be.Modellen.LoyalityClasses;
import kdg.be.Modellen.Product;
import kdg.be.Repositories.KlantRepository;
import kdg.be.Repositories.ProductRepository;
import org.springframework.beans.factory.InitializingBean;

@org.springframework.context.annotation.Configuration
public class Configuration implements InitializingBean{
private ProductRepository productRepository;
private KlantRepository klantRepository;

private LoyalityClassManager loyalityClassManager;

//Dit is 1 van de mogelijkheden om spring een methode uit te laten voeren
public Configuration(ProductRepository productRepository, KlantRepository klantRepository, LoyalityClassManager loyalityClassManager){

    this.productRepository=productRepository;
    this.klantRepository=klantRepository;
    this.loyalityClassManager=loyalityClassManager;

}


    @Override
    public void afterPropertiesSet()  {

        productRepository.save(new Product("naam",5.0));
       // Deze klant heeft als klantennummer 1
        klantRepository.save(new Customer(Customer.Klanttype.B2B));

        //Voeg de standaard loyaliteitsklasse toe
loyalityClassManager.save(new LoyalityClasses("brons",0,0d));
loyalityClassManager.save(new LoyalityClasses("zilver",1000,0.05d));
        loyalityClassManager.save(new LoyalityClasses("goud",5000,0.10d));
        loyalityClassManager.save(new LoyalityClasses("platina",10000,0.20d));

    }
}
