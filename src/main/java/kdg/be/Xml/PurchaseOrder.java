package kdg.be.Xml;

import jakarta.persistence.Embedded;
import jakarta.persistence.OneToOne;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XmlRootElement(name = "PurchaseOrder")
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


    @Override
    public String toString(){

        return "ok";
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
