package kdg.be.Modellen.DTO;

import java.util.Map;

//Tijdelijk
public class CustomerWithProducts {

private Long klantId;
private Map<Long,Integer> order;

    public Long getKlantId() {
        return klantId;
    }

    public void setKlantId(Long klantId) {
        this.klantId = klantId;
    }

    public Map<Long, Integer> getOrder() {
        return order;
    }

    public CustomerWithProducts(Long klantId, Map<Long, Integer> order) {
        this.klantId = klantId;
        this.order = order;
    }

    public void setOrder(Map<Long, Integer> order) {
        this.order = order;
    }
}
