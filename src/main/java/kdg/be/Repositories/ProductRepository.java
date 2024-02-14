package kdg.be.Repositories;

import kdg.be.Modellen.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    //er zijn vele mogelijkheden op niet crud routes te specifieren
    @Query("SELECT c FROM Product c WHERE c.Prijs=0.0")
    List<Product> findAllNewProducts();

}
