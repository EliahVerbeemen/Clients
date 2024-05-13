package kdg.be.Modellen.DTO;

import kdg.be.Modellen.ProductState;

public class ProductFromBakery {

    private String name;
    private ProductState _ProductStatus;
    private Long productId;

    public ProductState get_ProductStatus() {
        return _ProductStatus;
    }

    public void set_ProductStatus(ProductState _ProductStatus) {
        this._ProductStatus = _ProductStatus;
    }

    public Long getBakery_Id() {
        return bakery_Id;
    }

    public void setBakery_Id(Long bakery_Id) {
        this.bakery_Id = bakery_Id;
    }

    private Long bakery_Id;
    public ProductFromBakery(String name, ProductState _ProductStatus, Long productId) {
        this.name = name;
        this._ProductStatus = _ProductStatus;
        this.productId = productId;
        this.bakery_Id=productId;
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

       return "name" + this.name + " productId" + this.productId.toString() + "status" + this._ProductStatus;
   }
}
