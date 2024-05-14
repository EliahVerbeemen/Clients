package kdg.be.Modellen.DTO;

import java.util.Map;

//Tijdelijk
public class ClientWithProducts {

    private Long clientId;
    private Map<Long, Integer> order;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Map<Long, Integer> getOrder() {
        return order;
    }

    public ClientWithProducts(Long clientId, Map<Long, Integer> order) {
        this.clientId = clientId;
        this.order = order;
    }

    public void setOrder(Map<Long, Integer> order) {
        this.order = order;
    }
}
