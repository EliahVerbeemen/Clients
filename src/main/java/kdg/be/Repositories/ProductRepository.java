package kdg.be.Repositories;

import kdg.be.Modellen.Product;
import kdg.be.Modellen.ProductState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {



    public List<Product> findProductsBy_productState(ProductState productState);

}
