package kdg.be.Services;

import kdg.be.Services.Interfaces.IProductManager;
import kdg.be.Modellen.Product;
import kdg.be.Modellen.ProductState;
import kdg.be.Repositories.ProductRepository;
import kdg.be.testjes.ProductNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductService implements IProductManager {
    private ProductRepository _repo;
    public ProductService(ProductRepository repository) {_repo = repository;}
    @Override
    public Optional<Product> getProductById(Long id) {
        return _repo.findById(id);
    }


    public Product UpdatePriceAndActivate(Long productId, float price) throws ProductNotFoundException {
       Optional<Product> optionalProduct= _repo.findById(productId);
        if(optionalProduct.isPresent()){

            Product productToUpdate=optionalProduct.get();
            productToUpdate.set_productState(ProductState.Finaal);
            productToUpdate.setPrice(price);
          return  _repo.save(productToUpdate);

        }
        else  throw new ProductNotFoundException();


    }
}
