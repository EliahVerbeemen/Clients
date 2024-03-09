package kdg.be.Repositories;

import kdg.be.Modellen.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByClient_ClientId(long id, Sort sort);

}
