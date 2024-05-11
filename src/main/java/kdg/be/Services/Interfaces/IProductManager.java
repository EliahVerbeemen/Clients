package kdg.be.Services.Interfaces;

import kdg.be.Modellen.Product;

import java.util.Optional;

public interface IProductManager {
    public Optional<Product> getProductById(Long id);
}
