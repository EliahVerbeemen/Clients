package kdg.be.Managers.Interfaces;

import kdg.be.Modellen.Client;
import kdg.be.Modellen.Order;
import org.springframework.data.domain.Sort;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IOrderManager {
    public Optional<Order> getOrderById(Long id);
    public Order saveOrder(Order order);
    public Collection<Order> getAllOrders();
    public List<Order> getAllOrdersByClientWithSort(Long id, Sort sort);
    public Order CreateOrder(Client client, Map<Long, Integer> incomingOrder);
    public Client AddOrderToClient(Client client, Order order);
    public Order RepeatOrder(Client client, Order order);
    public Order setPriceInfo(Order order);

}
