package kdg.be.Services.Interfaces;

import kdg.be.Modellen.Client;
import kdg.be.Modellen.Order;
import org.springframework.data.domain.Sort;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IOrderService {
    public Optional<Order> getOrderById(Long id);
    public Order saveOrder(Order order);
    public Collection<Order> getAllOrders();
    public List<Order> getAllOrdersByClientWithSort(Long id, Sort sort);
    public Order createOrder(Client client, Map<Long, Integer> incomingOrder);
    public Client addOrderToClient(Client client, Order order);
    public Order repeatOrder(Client client, Order order);
    public Order setPriceInfo(Order order);

}
