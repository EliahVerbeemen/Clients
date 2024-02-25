package kdg.be.RabbitMQ;

import kdg.be.Modellen.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class RabbitSender {

    private RabbitTemplate rabbitTemplate;


    public RabbitSender(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate=rabbitTemplate;

    }


    public void SendOrderToBaker(Order order){

System.out.println(order.getTotaalprijs());
        this.rabbitTemplate.convertAndSend("receiveOrder","newOrder",order);


    }

}
