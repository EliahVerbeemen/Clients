package kdg.be.Xml;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Item {


    public Item() {
        System.out.println("item");

    }

    public String getProductNumber() {
        return ProductNumber;
    }

    public void setProductNumber(String productNumber) {
        ProductNumber = productNumber;
    }

    @XmlAttribute(name = "ProductNumber")
public String ProductNumber;

@XmlElement(name = "ProductName")
   private String ProductName;

@XmlElement(name = "Quantity")
   private int Quantity;

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getSpecialInstruction() {
        return SpecialInstruction;
    }

    public void setSpecialInstruction(String specialInstruction) {
        SpecialInstruction = specialInstruction;
    }

    @XmlElement(name = "SpecialInstruction")

   private String SpecialInstruction;


}
