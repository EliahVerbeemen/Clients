package kdg.be.Managers;

import kdg.be.Managers.Interfaces.IProductManager;
import kdg.be.Modellen.Product;
import kdg.be.Repositories.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductManager implements IProductManager {
    private ProductRepository _repo;
    public ProductManager(ProductRepository repository) {_repo = repository;}
    @Override
    public Optional<Product> getProductById(Long id) {
        return _repo.findById(id);
    }
}
