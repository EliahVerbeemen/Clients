package kdg.be.Modellen.DTO.ControllerDTO;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import kdg.be.Xml.Account;
import kdg.be.Xml.Items;
import kdg.be.Xml.PurchaseOrder;

import java.util.Date;

public class PurchaseOrderDTO {


    protected int PurchaseOrderNumber;

    Date OrderDate;


    protected kdg.be.Xml.Account Account;


    public PurchaseOrderDTO() {

    }

    public PurchaseOrderDTO(PurchaseOrder purchaseOrder) {

        this.PurchaseOrderNumber=purchaseOrder.getPurchaseOrderNumber();
        this.itemss= purchaseOrder.getItems();
        this.Account= purchaseOrder.getAccount();
        this.OrderDate=purchaseOrder.getOrderDate();







    }

    public int getPurchaseOrderNumber() {
        return PurchaseOrderNumber;
    }


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
    public void setAccount(kdg.be.Xml.Account account) {
        Account = account;
    }




    public Items getItems() {
        return itemss;
    }


    public void setItems(Items items) {
        this.itemss = items;
    }

    private Items itemss=new Items();







}
