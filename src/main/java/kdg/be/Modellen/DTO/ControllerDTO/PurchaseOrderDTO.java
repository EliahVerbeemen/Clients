package kdg.be.Modellen.DTO.ControllerDTO;

import jakarta.xml.bind.annotation.XmlAttribute;
import kdg.be.Xml.Account;
import kdg.be.Xml.Items;
import kdg.be.Xml.PurchaseOrder;

import java.util.Date;

public class PurchaseOrderDTO {
    protected int purchaseOrderNumber;
    protected Account account;
    Date OrderDate;
    private Items items = new Items();

    public PurchaseOrderDTO() {
    }

    public PurchaseOrderDTO(PurchaseOrder purchaseOrder) {
        this.purchaseOrderNumber = purchaseOrder.getPurchaseOrderNumber();
        this.items = purchaseOrder.getItems();
        this.account = purchaseOrder.getAccount();
        this.OrderDate = purchaseOrder.getOrderDate();
    }

    public int getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }
    public void setPurchaseOrderNumber(int purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }
    public Date getOrderDate() {
        return OrderDate;
    }
    @XmlAttribute(name = "OrderDate")
    public void setOrderDate(Date orderDate) {
        OrderDate = orderDate;
    }
    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }
    public Items getItems() {
        return items;
    }
    public void setItems(Items items) {
        this.items = items;
    }


}
