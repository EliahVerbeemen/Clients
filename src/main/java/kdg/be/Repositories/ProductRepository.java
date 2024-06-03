package kdg.be.Repositories;

import kdg.be.Modellen.Product;
import kdg.be.Modellen.ProductState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    public List<Product> findProductsByproductState(ProductState productState);
}
