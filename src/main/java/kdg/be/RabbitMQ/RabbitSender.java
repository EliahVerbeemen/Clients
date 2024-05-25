package kdg.be.RabbitMQ;

import com.fasterxml.jackson.databind.ObjectMapper;
import kdg.be.Modellen.Order;
import kdg.be.Xml.PurchaseOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitSender {

    private final RabbitTemplate rabbitTemplate;
    Logger logger = LoggerFactory.getLogger(RabbitSender.class);

    public RabbitSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void SendOrderToBaker(Order order) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String serialisedOrder = objectMapper.writeValueAsString(order);
            this.rabbitTemplate.convertAndSend("receiveOrder", "newOrder", serialisedOrder);
        } catch (Exception ex) {
            logger.warn("An error occurred during serialisation ");
        }
    }

    public void SendPurchaseOrderToBaker(PurchaseOrder purchaseOrder) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String serialisedPurchaseOrder = objectMapper.writeValueAsString(purchaseOrder);
            this.rabbitTemplate.convertAndSend("purchaseExchange", "purchaseOrder", serialisedPurchaseOrder);
        } catch (Exception ex) {
            logger.warn("An error occurred during serialisation ");
        }

    }
}
