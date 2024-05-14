package kdg.be.Services;

import kdg.be.Services.Interfaces.IProductManager;
import kdg.be.Modellen.Product;
import kdg.be.Modellen.ProductState;
import kdg.be.Repositories.ProductRepository;
import kdg.be.testjes.ProductNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class ProductService implements IProductManager {
    private ProductRepository _repo;
    public ProductService(ProductRepository repository) {_repo = repository;}
    @Override
    public Optional<Product> getProductById(Long id) {
        return _repo.findById(id);
    }
    @Override
    public List<Product> getAllProductsByProductState(ProductState productState) {return _repo.findProductsBy_productState(productState);}

    @Override
    public Product receiveProductFromBakery(Product product) {
        Optional<Product> optionalProduct=   getProductById(product.getProductId());
        if(optionalProduct.isPresent()){
            Product productToUpdate=optionalProduct.get();
            productToUpdate.set_productState(ProductState.Gedeactiveerd);
            productToUpdate.setName(product.getName());
           return _repo.save(productToUpdate);

        }
        else{
            product.set_productState(ProductState.Nieuw);
         return   _repo.save(product);

        }



    }



    @Transactional
    @Override
    public Optional<Product> UpdatePriceAndActivate(Long productId, float price)  {
       Optional<Product> optionalProduct= _repo.findById(productId);
        if(optionalProduct.isPresent()){

            Product productToUpdate=optionalProduct.get();
            productToUpdate.set_productState(ProductState.Finaal);
            productToUpdate.setPrice(price);
          return Optional.of(_repo.save(productToUpdate));

        }
       else return Optional.empty();


    }

    @Transactional
    @Override
    public Optional<Product> DeativateProduct(Long productId)  {
        Optional<Product> optionalProduct= _repo.findById(productId);
        if(optionalProduct.isPresent()){

            Product productToUpdate=optionalProduct.get();
            productToUpdate.set_productState(ProductState.Gedeactiveerd);

            return Optional.of(_repo.save(productToUpdate));

        }
        else return Optional.empty();


    }

}
