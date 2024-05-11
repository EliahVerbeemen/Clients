package kdg.be.Config;

import kdg.be.Modellen.Enums.ClientType;
import kdg.be.Services.LoyaltyClassService;
import kdg.be.Modellen.Client;
import kdg.be.Modellen.LoyaltyClass;
import kdg.be.Repositories.ClientRepository;
import kdg.be.Repositories.ProductRepository;
import org.springframework.beans.factory.InitializingBean;

@org.springframework.context.annotation.Configuration
public class Configuration implements InitializingBean {
    private ProductRepository productRepository;
    private ClientRepository clientRepository;
    private LoyaltyClassService loyaltyClassService;

    //Dit is 1 van de mogelijkheden om spring een methode uit te laten voeren
    public Configuration(ProductRepository productRepository, ClientRepository clientRepository, LoyaltyClassService loyaltyClassService) {

        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
        this.loyaltyClassService = loyaltyClassService;

    }


    @Override
    public void afterPropertiesSet() {

       // productRepository.save(new Product("naam", 5.0));
        // Deze klant heeft als klantennummer 1
        clientRepository.save(new Client(ClientType.B2B));

        //Voeg de standaard loyaliteitsklasse toe
        loyaltyClassService.save(new LoyaltyClass("brons", 0, 0d));
        loyaltyClassService.save(new LoyaltyClass("zilver", 1000, 0.05d));
        loyaltyClassService.save(new LoyaltyClass("goud", 5000, 0.10d));
        loyaltyClassService.save(new LoyaltyClass("platina", 10000, 0.20d));

    }
}
