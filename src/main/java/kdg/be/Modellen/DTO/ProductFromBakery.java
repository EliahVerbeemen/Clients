package kdg.be.Modellen.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import kdg.be.Modellen.ProductState;
import kdg.be.SerializationHelpers.ProductDeserializer;

@JsonDeserialize(using = ProductDeserializer.class)
public class ProductFromBakery {

    private String name;
    private ProductState productState;
    private Long productId;
    private Long bakeryId;

    public ProductFromBakery(String name, ProductState productState, Long productId) {
        this.name = name;
        this.productState = productState;
        this.productId = productId;
        this.bakeryId = productId;
    }

    public Long getBakeryId() {
        return bakeryId;
    }

    public void setBakeryId(Long bakeryId) {
        this.bakeryId = bakeryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductState getProductState() {
        return productState;
    }

    public void setProductState(ProductState productState) {
        this.productState = productState;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "name" + this.name + " productId" + this.productId.toString() + "status" + this.productState;
    }
}
