package kdg.be.RabbitMQ;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kdg.be.Modellen.Product;
import kdg.be.Services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
public class RabbitListener {

    private final ProductService productService;
    Logger logger = LoggerFactory.getLogger(RabbitListener.class);
    @Value("${rabbitMQ.recepyQueue}")
    private String recipeQueue;

    public RabbitListener(ProductService productService) {
        this.productService = productService;
    }

    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = {"newRecepiesQueue"})
    public void ReceiveRecipe(String incomingRecipe) {


        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Product product = objectMapper.readValue(incomingRecipe, Product.class);
            productService.receiveProductFromBakery(product);
            logger.info("The bakery has send a new product");
        } catch (JsonProcessingException e) {

            logger.warn("product deserialization failed");
            logger.debug(e.getMessage());
        }
    }
}
