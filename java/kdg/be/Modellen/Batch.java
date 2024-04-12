package kdg.be.Modellen;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

//Er moet ergens een plaats zijn waar de orders bijgehouden worden
/*@Entity
public class Batch {

    public Batch(){

    }

    public Batch(LocalDateTime localDateTime){
this.createTime=localDateTime;
    }
    @Id
    @GeneratedValue
   private Long BatchId;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Order> ordersInProcess=new HashSet<>();


   // @CreationTimestamp
   @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime  createTime;

    private ProductsStatusEnum productsStatusEnum = ProductsStatusEnum.OPEN;

    public Long getBatchId() {
        return BatchId;
    }

    public void setBatchId(Long batchId) {
        BatchId = batchId;
    }

    public Set<Order> getOrdersInProcess() {
        return ordersInProcess;
    }

    //



    public void setOrdersInProcess(Set<Order> ordersInProcess) {
        this.ordersInProcess = ordersInProcess;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public ProductsStatusEnum getProductState() {
        return productsStatusEnum;
    }

    public void setProductState(ProductsStatusEnum productsStatusEnum) {
        this.productsStatusEnum = productsStatusEnum;
    }


}
        */
