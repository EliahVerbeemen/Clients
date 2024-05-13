package kdg.be.Services;

import kdg.be.Modellen.Enums.OrderStatus;
import kdg.be.Services.Interfaces.IOrderManager;
import kdg.be.Modellen.*;
import kdg.be.Repositories.OrderRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class OrderService implements IOrderManager {
    OrderRepository _orderRepo;
    ProductService _productMgr;
    LoyalityClassService _loyaltyMgr;

    public OrderService(OrderRepository orderRepository, ProductService productService, LoyalityClassService loyalityClassService){
        _orderRepo = orderRepository;
        _productMgr = productService;
        _loyaltyMgr = loyalityClassService;
    }

    @Override
    public Optional<Order> getOrderById(Long id){
        return _orderRepo.findById(id);
    }

    @Override
    public Order saveOrder(Order order){
        return _orderRepo.save(order);
    }
    @Override
    public Collection<Order> getAllOrders(){
        return _orderRepo.findAll();
    }

    @Override
    public List<Order> getAllOrdersByClientWithSort(Long id, Sort sort){
        return _orderRepo.findByClient_ClientId(id, sort);
    }

    @Override
    public Order CreateOrder(Client client, Map<Long, Integer> incomingOrder){
        Order order = new Order();
        order.setOrderStatus(OrderStatus.Niet_bevestigd);
        order.setOrderDate(LocalDate.now());
        System.out.println("methode");
        System.out.println(incomingOrder.size());
        Map<Product, Integer> orderProducts = new HashMap<>();
        AtomicReference<Double> subTotal = new AtomicReference<>(0d);
        incomingOrder.forEach((productId, aantal) ->
        {


            Optional<Product> productOptional = _productMgr.getProductById(productId);
            if (productOptional.isPresent()&&productOptional.get().get_productState().equals(ProductState.Finaal)) {
                Product product = productOptional.get();
                orderProducts.put(product, aantal);
                subTotal.updateAndGet(v -> (product.getPrice() * aantal));
                order.getProducts().put(productId, aantal);
            } else {
                System.out.println("niet gevonden");
                order.getRemarks().add("Chosen product is not available: " + productOptional.toString());
            }
        });
        order.setTotalPrice(subTotal.get());
     System.out.println("onder aan service");
     System.out.println(order.getProducts().size());
        return order;
    }
    @Override
    public Client AddOrderToClient(Client client, Order order){
       ;
        order.setClient(client);
        _orderRepo.save(order);
        return client;
    }

    @Override
    public Order RepeatOrder(Client client, Order order){
        HashMap<Long, Integer> orderProducts = new HashMap<>();
       //Is het product nog wel beschikbaar?
        orderProducts.putAll(order.getProducts());
        Order newOrder = new Order(order.getClient(), LocalDate.now(), orderProducts, OrderStatus.Niet_bevestigd);
        newOrder.setOrderId(null);
        Order newPricedOrder = setPriceInfo(newOrder);
        newPricedOrder.setClient(client);
        client.getOrders().add(newPricedOrder);
        _orderRepo.save(newPricedOrder);
        return newPricedOrder;
    }

    //Niet vergeten om wat validatie te doen. Orders zonder producten bvb
    //Spring voorziet default annotaties
    @Override
    public Order setPriceInfo(Order order) {
        AtomicReference<Double> subTotal = new AtomicReference<>((double) 0);
        double discount = 0.0;
        Client client = order.getClient();

        System.out.println(order.getProducts().size());

        LoyalityClass loyalityClass = _loyaltyMgr.getLoyaltyClass(client.getPoints());
        order.getProducts().forEach((productKey, quantity) -> {
            Optional<Product> product = _productMgr.getProductById(productKey);
            product.ifPresent(value -> subTotal.updateAndGet(v -> v + value.getPrice() * quantity));


        });
        System.out.println(subTotal);
        discount = subTotal.get() * loyalityClass.getReduction();
        order.setDiscount(discount);
        order.setTotalPrice(subTotal.get() - discount);

        return order;
    }

    public boolean BeforeTenOClock() {
        LocalTime hour = LocalTime.now();
        return hour.isBefore(LocalTime.of(22, 0, 0));

    }





}
