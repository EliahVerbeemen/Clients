package kdg.be.Xml;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;
import kdg.be.Modellen.Client;
import kdg.be.Modellen.Order;
import kdg.be.SerializatieHelpers.PurchaseOrderSerializer;

import java.io.Serializable;
import java.util.Date;

@XmlRootElement(name = "PurchaseOrder")
@JsonSerialize(using = PurchaseOrderSerializer.class)
@Entity
public class PurchaseOrder  implements Serializable {



    protected int PurchaseOrderNumber;

    Date OrderDate;


    protected Account Account;
    @Id
    private Long purchasOrderId;

    public Items getItemss() {
        return itemss;
    }

    public void setItemss(Items itemss) {
        this.itemss = itemss;
    }

    public PurchaseOrder() {

    }

    public PurchaseOrder(int purchaseOrderNumber, Date orderDate) {
        this.PurchaseOrderNumber = purchaseOrderNumber;
        OrderDate = orderDate;
    }
    public PurchaseOrder(int purchaseOrderNumber) {
        PurchaseOrderNumber = purchaseOrderNumber;

    }

    public int getPurchaseOrderNumber() {
        return PurchaseOrderNumber;
    }
    @XmlAttribute(name = "PurchaseOrderNumber")

    public void setPurchaseOrderNumber(int purchaseOrderNumber) {
        PurchaseOrderNumber = purchaseOrderNumber;
    }





    public Date getOrderDate() {
        return OrderDate;
    }
   // @XmlAttribute
   @XmlAttribute(name = "OrderDate")

    public void setOrderDate(Date orderDate) {
        OrderDate = orderDate;
    }


    public kdg.be.Xml.Account getAccount() {        return Account;
    }
@XmlElement(name = "Account")
    public void setAccount(kdg.be.Xml.Account account) {
        Account = account;
    }




    public Items getItems() {
        return itemss;
    }
    @XmlElement(name = "Items")

    public void setItems(Items items) {
        this.itemss = items;
    }

private Items itemss=new Items();


    public void setPurchasOrderId(Long purchasOrderId) {
        this.purchasOrderId = purchasOrderId;
    }

    public Long getPurchasOrderId() {
        return purchasOrderId;
    }
}
