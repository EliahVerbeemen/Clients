package kdg.be.Config;

import kdg.be.Modellen.Client;
import kdg.be.Modellen.Enums.ClientType;
import kdg.be.Modellen.LoyaltyClass;
import kdg.be.Modellen.Product;
import kdg.be.Modellen.ProductState;
import kdg.be.Repositories.ClientRepository;
import kdg.be.Repositories.LoyaltyClassRepository;
import kdg.be.Repositories.ProductRepository;
import kdg.be.Services.LoyaltyClassService;
import org.springframework.beans.factory.InitializingBean;

@org.springframework.context.annotation.Configuration
public class Configuration implements InitializingBean {
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final LoyaltyClassService loyaltyClassService;
    private final LoyaltyClassRepository loyaltyClassRepository;

        public Configuration(ProductRepository productRepository, ClientRepository clientRepository, LoyaltyClassService loyaltyClassService, LoyaltyClassRepository loyaltyClassRepository) {
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
        this.loyaltyClassService = loyaltyClassService;
        this.loyaltyClassRepository = loyaltyClassRepository;
    }


    @Override
    public void afterPropertiesSet() {
        clientRepository.save(new Client(ClientType.B2B));
        Product product = new Product("test", 5);
        product.setProductState(ProductState.FINAL);
        product.setProductNumber(1L);
        productRepository.save(product);
        loyaltyClassRepository.save(new LoyaltyClass("bronze", 0, 0));
        loyaltyClassRepository.save(new LoyaltyClass("silver", 1000, 0.05));
        loyaltyClassRepository.save(new LoyaltyClass("gold", 5000, 0.1));
        loyaltyClassRepository.save(new LoyaltyClass("platinum", 10000, 0.2));

    }
}
