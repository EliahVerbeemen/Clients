package kdg.be.Modellen.DTO.ControllerDTO;

import kdg.be.Modellen.Client;
import kdg.be.Modellen.Enums.OrderStatus;
import kdg.be.Modellen.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class OrderDTO {
    private long orderId;
    private Client client;
    private LocalDate orderDate;
    public double discount;
    public double totalPrice;
    private OrderStatus orderStatus;
    private Map<Long, Integer> products;
    private List<String> remarks;

    public OrderDTO (Order order){
        orderId = order.getOrderId();
        client = order.getClient();
        orderDate = order.getOrderDate();
        discount = order.getDiscount();
        totalPrice = order.getTotalPrice();
        orderStatus = order.getOrderStatus();
        products = order.getProducts();
        remarks = order.getRemarks();
    }

}
