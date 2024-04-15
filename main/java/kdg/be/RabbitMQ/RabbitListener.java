package kdg.be.RabbitMQ;

import kdg.be.Modellen.DTO.ProductFromBakery;
import kdg.be.Modellen.Product;
import kdg.be.Modellen.ProductState;
import kdg.be.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

//https://docs.spring.io/spring-amqp/api/constant-values.html
@Service
public class RabbitListener {

    //Hoog tijd voor manager
    private final ProductRepository productRepository;
    public RabbitListener(ProductRepository productRepository){
        this.productRepository=productRepository;

    }

   @Value("${rabbitMQ.recepyQueue}")
    private String recepyQueu;




    //TODO producten met een .0 prijs mogen niet getoond worden aan klanten
    //TODO prijstoewijzing


    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues ={"newRecepiesQueue"} )
    public void ReceiveRecepy(ProductFromBakery recepy){


        System.out.println("ontvangen");
        System.out.println(recepy);

        if(recepy.getProductState().equals(ProductState.Finaal)){


            productRepository.save(new Product(0d,recepy.getName(), recepy.getProductId(),ProductState.Nieuw));

         }
        else{

         Optional<Product>optionealProduct=   productRepository.findById(recepy.getProductId());
         if(optionealProduct.isPresent()&&recepy.getProductState().equals(ProductState.Gedeactiveerd)){

           //TODO update and save


         }


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
