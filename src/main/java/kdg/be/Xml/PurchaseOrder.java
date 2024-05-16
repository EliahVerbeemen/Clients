package kdg.be.Xml;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.xml.bind.annotation.*;
import kdg.be.SerializatieHelpers.PurchaseOrderSerializer;

import java.io.Serializable;
import java.util.Date;

@XmlRootElement(name = "PurchaseOrder")
//@JsonSerialize(using = PurchaseOrderSerializer.class)
public class PurchaseOrder  implements Serializable {

    protected int PurchaseOrderNumber;

    Date OrderDate;

  //  @XmlElement(name = "Account")
    protected Account Account;
  // @XmlElement(name = "Account")

    public PurchaseOrder() {

        System.out.println("empty constructor");
    }

    public PurchaseOrder(int purchaseOrderNumber, Date orderDate) {
        PurchaseOrderNumber = purchaseOrderNumber;
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

    // private Account Account;



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
}
