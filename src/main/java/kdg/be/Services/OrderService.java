package kdg.be.Services;

import jakarta.transaction.Transactional;
import kdg.be.Modellen.*;
import kdg.be.Modellen.Enums.OrderStatus;
import kdg.be.Repositories.OrderRepository;
import kdg.be.Services.Interfaces.IOrderService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class OrderService implements IOrderService {
    OrderRepository orderRepo;
    ProductService productService;
    LoyaltyClassService loyaltyService;

    public OrderService(OrderRepository orderRepository, ProductService productService, LoyaltyClassService loyaltyClassService) {
        orderRepo = orderRepository;
        this.productService = productService;
        loyaltyService = loyaltyClassService;
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepo.findById(id);
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepo.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    @Override
    public List<Order> getAllOrdersByClientWithSort(Long id, Sort sort) {
        return orderRepo.findByClient_ClientId(id, sort);
    }

    @Override
    @Transactional
    public Order createOrder(Client client, Map<Long, Integer> incomingOrder) {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.NOT_CONFIRMED);
        order.setOrderDate(LocalDate.now());
        Map<Product, Integer> orderProducts = new HashMap<>();
        AtomicReference<Double> subTotal = new AtomicReference<>(0d);
        incomingOrder.forEach((productId, aantal) ->
        {
            Optional<Product> productOptional = productService.getProductById(productId);
            if (productOptional.isPresent() && productOptional.get().getProductState().equals(ProductState.FINAL)) {
                Product product = productOptional.get();
                orderProducts.put(product, aantal);
                subTotal.updateAndGet(v -> (product.getPrice() * aantal));
                order.getProducts().put(productId, aantal);
            } else {
                order.getRemarks().add("Chosen product is not available: " + productOptional);
            }
        });
        order.setTotalPrice(subTotal.get());


        return order;
    }

    @Override
    @Transactional
    public Client addOrderToClient(Client client, Order order) {
        order.setClient(client);
        orderRepo.save(order);
        return client;
    }

    @Override
    @Transactional
    public Order repeatOrder(Client client, Order order) {
        HashMap<Long, Integer> orderProducts = new HashMap<>();
        orderProducts.putAll(order.getProducts());
        Order newOrder = new Order(order.getClient(), LocalDate.now(), orderProducts, OrderStatus.NOT_CONFIRMED);
        newOrder.setOrderId(null);
        Order newPricedOrder = setPriceInfo(newOrder);
        newPricedOrder.setClient(client);
        client.getOrders().add(newPricedOrder);
        orderRepo.save(newPricedOrder);
        return newPricedOrder;
    }

    //Niet vergeten om wat validatie te doen. Orders zonder producten bvb
    //Spring voorziet default annotaties
    @Override
    @Transactional
    public Order setPriceInfo(Order order) {
        AtomicReference<Double> subTotal = new AtomicReference<>((double) 0);
        double discount = 0.0;
        Client client = order.getClient();

        LoyaltyClass loyaltyClass = loyaltyService.getLoyaltyClass(client.getPoints());
        order.getProducts().forEach((productKey, quantity) -> {
            Optional<Product> product = productService.getProductById(productKey);
            product.ifPresent(value -> subTotal.updateAndGet(v -> v + value.getPrice() * quantity));
        });

        discount = subTotal.get() * loyaltyClass.getReduction();
        order.setDiscount(discount);
        order.setTotalPrice(subTotal.get() - discount);
        return order;
    }

    public boolean BeforeTenOClock() {
        LocalTime hour = LocalTime.now();
        return hour.isBefore(LocalTime.of(22, 0, 0));
    }
}
