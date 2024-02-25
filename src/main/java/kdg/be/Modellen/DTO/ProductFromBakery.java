package kdg.be.Modellen.DTO;

import kdg.be.Modellen.ProductState;

public class ProductFromBakery {

    private String name;
    private ProductState _ProductStatus;
    private Long productId;



    public ProductFromBakery(String name, ProductState productState, Long productId) {
        this.name = name;
        this._ProductStatus = productState;
        this.productId = productId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductState getProductState() {
        return _ProductStatus;
    }

    public void setProductState(ProductState productState) {
        this._ProductStatus = productState;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
    @Override
    public String toString() {
        return this .name + " " + this.productId.toString();
    }
}
