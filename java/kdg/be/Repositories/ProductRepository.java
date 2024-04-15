package kdg.be.Repositories;

import kdg.be.Modellen.Product;
import kdg.be.Modellen.ProductState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    //er zijn vele mogelijkheden op niet crud routes te specifieren
 /*   @Query("SELECT c FROM Product c WHERE c.Prijs=0.0 AND NOT c.Gedeactiveerd")
    List<Product> findAllNewProducts();

    @Query("SELECT c FROM Product c WHERE c.Prijs !=0.0 AND NOT c.Gedeactiveerd")
    List<Product> findProductsForCustomers();*/
    Optional<Product> findByBakkeryId(Long aLong);


    public List<Product> findProductsBy_productState(ProductState productState);

}
