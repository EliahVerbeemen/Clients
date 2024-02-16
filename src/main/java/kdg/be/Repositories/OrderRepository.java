package kdg.be.Repositories;

import kdg.be.Modellen.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByKlant_KlantNumber(long id, Sort sort);

    Optional<Order> findByOrderNumber(Long Id);


}
