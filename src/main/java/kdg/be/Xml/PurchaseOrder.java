package kdg.be.Xml;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import kdg.be.SerializationHelpers.PurchaseOrderSerializer;

import java.io.Serializable;
import java.util.Date;

@XmlRootElement(name = "PurchaseOrder")
@JsonSerialize(using = PurchaseOrderSerializer.class)
@Entity
public class PurchaseOrder implements Serializable {
    protected int purchaseOrderNumber;
    protected Account account;
    Date orderDate;
    @Id
    private Long purchaseOrderId;
    private Items items = new Items();

    public PurchaseOrder() {

    }

    public PurchaseOrder(int purchaseOrderNumber, Date orderDate) {
        this.purchaseOrderNumber = purchaseOrderNumber;
        this.orderDate = orderDate;
    }

    public PurchaseOrder(int purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;

    }

    public int getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    @XmlAttribute(name = "PurchaseOrderNumber")

    public void setPurchaseOrderNumber(int purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    // @XmlAttribute
    @XmlAttribute(name = "OrderDate")

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public kdg.be.Xml.Account getAccount() {
        return account;
    }

    @XmlElement(name = "Account")
    public void setAccount(kdg.be.Xml.Account account) {
        this.account = account;
    }

    public Items getItems() {
        return items;
    }

    @XmlElement(name = "Items")

    public void setItems(Items items) {
        this.items = items;
    }

    public Long getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(Long purchasOrderId) {
        this.purchaseOrderId = purchasOrderId;
    }
}
