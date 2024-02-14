package kdg.be.Repositories;

import kdg.be.Modellen.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
