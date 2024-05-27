package kdg.be.Services;

import kdg.be.Services.Interfaces.IProductService;
import kdg.be.Modellen.Product;
import kdg.be.Modellen.ProductState;
import kdg.be.Repositories.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class ProductService implements IProductService {
    private ProductRepository repo;
    public ProductService(ProductRepository repository) {
        repo = repository;}

    @Override
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return repo.findById(id);
    }
    @Override
    public List<Product> getAllProductsByProductState(ProductState productState) {return repo.findProductsBy_productState(productState);}

    @Override
    public Product receiveProductFromBakery(Product product) {
        Optional<Product> optionalProduct=   getProductById(product.getProductId());
        if(optionalProduct.isPresent()&&optionalProduct.get().getProductState().equals(ProductState.DEACTIVATED)){
            Product productToUpdate=optionalProduct.get();
            productToUpdate.setProductState(ProductState.DEACTIVATED);
            productToUpdate.setName(product.getName());
           return repo.save(productToUpdate);

        }
        else{
            System.out.println("products saved");
            System.out.println(product.getProductState());
            return repo.save(product);

        }
    }

    @Transactional
    @Override
    public Optional<Product> updatePriceAndActivate(Long productId, float price)  {
       Optional<Product> optionalProduct= repo.findById(productId);
        if(optionalProduct.isPresent()){
            Product productToUpdate=optionalProduct.get();
            productToUpdate.setProductState(ProductState.FINAL);
            productToUpdate.setPrice(price);
          return Optional.of(repo.save(productToUpdate));
        }
       else return Optional.empty();
    }

    @Override
    public List<Product> getProductsByState(ProductState state) {
        return repo.findProductsBy_productState(state);
    }

    @Transactional
    @Override
    public Optional<Product> deactivateProduct(Long productId)  {
        Optional<Product> optionalProduct= repo.findById(productId);
        if(optionalProduct.isPresent()){
            Product productToUpdate=optionalProduct.get();
            productToUpdate.setProductState(ProductState.DEACTIVATED);
            return Optional.of(repo.save(productToUpdate));
        }
        else return Optional.empty();
    }


}
