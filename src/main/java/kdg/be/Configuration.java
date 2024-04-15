package kdg.be;

import kdg.be.Managers.LoyaltyClassManager;
import kdg.be.Modellen.Client;
import kdg.be.Modellen.LoyalityClasses;
import kdg.be.Modellen.Product;
import kdg.be.Repositories.ClientRepository;
import kdg.be.Repositories.ProductRepository;
import org.springframework.beans.factory.InitializingBean;

@org.springframework.context.annotation.Configuration
public class Configuration implements InitializingBean {
    private ProductRepository productRepository;
    private ClientRepository clientRepository;

    private LoyaltyClassManager loyaltyClassManager;

    //Dit is 1 van de mogelijkheden om spring een methode uit te laten voeren
    public Configuration(ProductRepository productRepository, ClientRepository clientRepository, LoyaltyClassManager loyaltyClassManager) {

        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
        this.loyaltyClassManager = loyaltyClassManager;

    }


    @Override
    public void afterPropertiesSet() {

       // productRepository.save(new Product("naam", 5.0));
        // Deze klant heeft als klantennummer 1
        clientRepository.save(new Client(Client.ClientType.B2B));

        //Voeg de standaard loyaliteitsklasse toe
        loyaltyClassManager.save(new LoyalityClasses("brons", 0, 0d));
        loyaltyClassManager.save(new LoyalityClasses("zilver", 1000, 0.05d));
        loyaltyClassManager.save(new LoyalityClasses("goud", 5000, 0.10d));
        loyaltyClassManager.save(new LoyalityClasses("platina", 10000, 0.20d));

    }
}
