package kdg.be.RabbitMQ;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    @Value("${rabbitMQ.orderQueu}")
    private String orderQueue;

  /*  @Value("${exchange.newRecepyExchange}")
    private String exchangeName;*/


    //Kies de juiste import voor de queue
    @Bean
    public Queue queue(){

        return new Queue("receiveOrder",true);


    }

    @Bean
    public DirectExchange directExchange(){


        return new DirectExchange("receiveOrder");
    }



    @Bean

    public Binding binding(Queue queue, DirectExchange directExchange){
        return    BindingBuilder.bind(queue).to(directExchange).with("newOrder");


    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate= new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());

        return rabbitTemplate;
    }
    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {

        //     ObjectMapper objectMapper=new ObjectMapper();


        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("sparrow.rmq.cloudamqp.com");
        connectionFactory.setUsername("umxeqqou");
        connectionFactory.setPassword("z-3ZVFLkp9m_3yaeEZCxKH0KkthMRDoY");
        connectionFactory.setVirtualHost("umxeqqou");

        return connectionFactory;
    }


}