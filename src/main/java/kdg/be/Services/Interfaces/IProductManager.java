package kdg.be.Services.Interfaces;

import kdg.be.Modellen.Product;
import kdg.be.Modellen.ProductState;

import java.util.List;
import java.util.Optional;

public interface IProductManager {
    public Optional<Product> getProductById(Long id);


    public List<Product> getAllProductsByProductState(ProductState productState) ;

    public Product receiveProductFromBakery(Product product) ;

    Optional<Product> DeativateProduct(Long productId);

    public Optional<Product> UpdatePriceAndActivate(Long productId, float price);

}
