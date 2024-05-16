package kdg.be.Modellen;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import kdg.be.SerializatieHelpers.ProductDeserializer;

@Entity
@JsonDeserialize(using = ProductDeserializer.class)
public class Product {

    //Properties
    @Id
    private Long productId;


    public ProductState get_productState() {
        return _productState;
    }

    public void set_productState(ProductState _productState) {
        this._productState = _productState;
    }

    private  ProductState _productState;
    private double price;
    private String name;




    // GET & SET
    public Long getProductId() {
        return productId;
    }

    public void setProductNumeber(Long productNumeber) {
        productId = productNumeber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }







    public Product(double price, String name, Long bakkeryId,ProductState productState) {
        this.price = price;
        this.name = name;
        this.productId = bakkeryId;
        this._productState=productState;

    }

    // Constructors
    public Product() {
    }



    public Product(String name, double price) {
        this.price = price;
        this.name = name;
    }
    public Product(double price, String name, ProductState productState,long bakkeryId) {
        this.price = price;
        this.name = name;
        this.productId = bakkeryId;
        this._productState=productState;
    }
}
