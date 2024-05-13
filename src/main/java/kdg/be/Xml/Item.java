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
    @XmlElement(name = "SpecialInstruction")

   private String SpecialInstruction;

    @Override
    public String toString() {
        return this.ProductName+ " "+ this.Quantity+ " " + this.SpecialInstruction;
    }
}
