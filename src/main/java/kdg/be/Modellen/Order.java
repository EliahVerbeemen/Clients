package kdg.be.Modellen;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import kdg.be.Modellen.Enums.OrderStatus;
import kdg.be.SerializatieHelpers.OrderSerializer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@JsonSerialize(using = OrderSerializer.class)
public class Order {

    @Transient
    public double discount;
    public double totalPrice;
    // Properties
    @Id
    @GeneratedValue
    private Long orderId;
    @ManyToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference
    private Client client;
    private LocalDate orderDate;
    private OrderStatus orderStatus;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<Long, Integer> products = new HashMap<>();

    @ElementCollection
    private List<String> remarks = new ArrayList<>();

    // Constructors
    public Order(Client client, LocalDate orderDate, Map<Long, Integer> orderProducts, OrderStatus orderStatus) {
        this.client = client;
        this.orderDate = orderDate;
        products = orderProducts;
        this.orderStatus = orderStatus;
    }

    public Order(Map<Long, Integer> orderProducts, Client client) {
        this.client = client;
        products = orderProducts;
    }

    public Order() {

    }

    // GET & SET
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderNumber) {
        this.orderId = orderNumber;
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

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
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
