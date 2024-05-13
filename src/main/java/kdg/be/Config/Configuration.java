package kdg.be.Config;

import kdg.be.Modellen.Enums.ClientType;
import kdg.be.Modellen.Product;
import kdg.be.Modellen.ProductState;
import kdg.be.Services.LoyalityClassService;
import kdg.be.Modellen.Client;
import kdg.be.Repositories.ClientRepository;
import kdg.be.Repositories.ProductRepository;
import org.springframework.beans.factory.InitializingBean;

@org.springframework.context.annotation.Configuration
public class Configuration implements InitializingBean {
    private ProductRepository productRepository;
    private ClientRepository clientRepository;
    private LoyalityClassService loyalityClassService;

    //Dit is 1 van de mogelijkheden om spring een methode uit te laten voeren
    public Configuration(ProductRepository productRepository, ClientRepository clientRepository, LoyalityClassService loyalityClassService) {

        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
        this.loyalityClassService = loyalityClassService;

    }


    @Override
    public void afterPropertiesSet() {

       // productRepository.save(new Product("naam", 5.0));
        // Deze klant heeft als klantennummer 1
        clientRepository.save(new Client(ClientType.B2B));
Product product=new Product("test",5);
product.set_productState(ProductState.Finaal);
product.setProductNumeber(1L);
        productRepository.save(product);

    }
}
