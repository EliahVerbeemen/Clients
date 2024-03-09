package kdg.be.Managers.Interfaces;

import kdg.be.Modellen.Product;

import java.util.Optional;

public interface IProductManager {
    public Optional<Product> getProductById(Long id);
}
