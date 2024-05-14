package kdg.be.RabbitMQ;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kdg.be.Modellen.Product;
import kdg.be.Services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

//https://docs.spring.io/spring-amqp/api/constant-values.html
@Service

public class RabbitListener {

   Logger logger=LoggerFactory.getLogger(RabbitListener.class);
    private final ProductService productService;
    public RabbitListener(ProductService productService){
        this.productService=productService;

    }

   @Value("${rabbitMQ.recepyQueue}")
    private String recepyQueu;






    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues ={"newRecepiesQueue"} )
    public void ReceiveRecepy(String incomingRecepy){
System.out.println(incomingRecepy);

        ObjectMapper objectMapper=new ObjectMapper();
        try {
          Product product=  objectMapper.readValue(incomingRecepy,Product.class);

         productService.receiveProductFromBakery(product);

System.out.println("gelukt");
System.out.println(product.getPrice());
        } catch (JsonProcessingException e) {
          System.out.println(e.getMessage());
            logger.warn("product deserialization failed");
logger.debug(e.getMessage());
        }


    }




   /* @org.springframework.amqp.rabbit.annotation.RabbitListener(queues ={"deactivate"} )
    public void DeactivateRecepy(Long productId){

      Optional<Product> optionalProductToConfirm= productRepository.findByBakkeryId(productId);
      if(optionalProductToConfirm.isPresent()){

          Product product=optionalProductToConfirm.get();
          product.setActive(true);
          productRepository.save(product);

      }


    }*/



   /* @org.springframework.amqp.rabbit.annotation.RabbitListener(queues ={"newRecepiesQueue"} )
    public void ReceiveRecepy(){
        System.out.println("!!!");

    }*/
}
