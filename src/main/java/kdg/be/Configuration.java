package kdg.be;

import kdg.be.Managers.LoyalityClassManager;
import kdg.be.Modellen.Klant;
import kdg.be.Modellen.LoyalityClasses;
import kdg.be.Modellen.Product;
import kdg.be.Repositories.KlantRepository;
import kdg.be.Repositories.OrderRepository;
import kdg.be.Repositories.ProductRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration implements InitializingBean{
private ProductRepository productRepository;
private KlantRepository klantRepository;

private LoyalityClassManager loyalityClassManager;
public Configuration(ProductRepository productRepository, KlantRepository klantRepository, LoyalityClassManager loyalityClassManager){

    this.productRepository=productRepository;
    this.klantRepository=klantRepository;
    this.loyalityClassManager=loyalityClassManager;

}


    @Override
    public void afterPropertiesSet() throws Exception {

        productRepository.save(new Product("naam",5.0));
        klantRepository.save(new Klant(Klant.Klanttype.B2B));
klantRepository.findAll().forEach(e->System.out.println(e.getKlantNumber()));
loyalityClassManager.save(new LoyalityClasses("brons",0,0d));
loyalityClassManager.save(new LoyalityClasses("zilver",1000,0.05d));
        loyalityClassManager.save(new LoyalityClasses("goud",5000,0.10d));
        loyalityClassManager.save(new LoyalityClasses("platina",10000,0.20d));

    }
}
