package kdg.be.Config;

import kdg.be.Modellen.Client;
import kdg.be.Modellen.Enums.ClientType;
import kdg.be.Modellen.Product;
import kdg.be.Modellen.ProductState;
import kdg.be.Repositories.ClientRepository;
import kdg.be.Repositories.ProductRepository;
import kdg.be.Services.LoyaltyClassService;
import org.springframework.beans.factory.InitializingBean;

@org.springframework.context.annotation.Configuration
public class Configuration implements InitializingBean {
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final LoyaltyClassService loyaltyClassService;

        public Configuration(ProductRepository productRepository, ClientRepository clientRepository, LoyaltyClassService loyaltyClassService) {

        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
        this.loyaltyClassService = loyaltyClassService;

    }


    @Override
    public void afterPropertiesSet() {
        clientRepository.save(new Client(ClientType.B2B));
        Product product = new Product("test", 5);
        product.setProductState(ProductState.FINAL);
        product.setProductNumeber(1L);
        productRepository.save(product);

    }
}
