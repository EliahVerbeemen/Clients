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
    private double discount;
    private double totalPrice;
    private OrderStatus orderStatus;
    private Map<Long, Integer> products;
    private List<String> remarks;

//Getters en setters anders zit de client enkel de public properties
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





    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Map<Long, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Long, Integer> products) {
        this.products = products;
    }

    public List<String> getRemarks() {
        return remarks;
    }

    public void setRemarks(List<String> remarks) {
        this.remarks = remarks;
    }


}
