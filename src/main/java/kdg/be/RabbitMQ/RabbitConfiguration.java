package kdg.be.RabbitMQ;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    @Value("${rabbitMQ.orderQueu}")
    private String orderQueue;

    @Value(value = "${spring.rabbitmq.host}")
    private String rabbitMqAddress;

    @Value(value = "${spring.rabbitmq.username}")
    private String rabbitMqName;

    @Value(value = "${spring.rabbitmq.password}")
    private String rabbitMqPassword;

    @Value(value = "${spring.rabbitmq.virtual-host}")
    private String rabbitMqVirtualHost;

    //Kies de juiste import voor de queue
    @Bean
    public Queue queue() {
        return new Queue("receiveOrder", true);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("receiveOrder");
    }

    @Bean
    @Qualifier(value = "sendPurchaseOrder")
    public Queue sendPurchaseOrder() {
        return new Queue("sendPurchaseOrder", true);
    }

    @Bean
    @Qualifier(value = "purchaseExchange")
    public DirectExchange directExchangePurchaseOrder() {
        return new DirectExchange("purchaseExchange");
    }

    @Bean
    @Qualifier(value = "purchaseBinding")
    public Binding bindingPurchaseOrder(@Qualifier(value = "purchaseExchange") DirectExchange directExchange, @Qualifier(value = "sendPurchaseOrder") Queue queue) {
        return BindingBuilder.bind(queue).to(directExchange).with("purchaseOrder");
    }


    @Bean
    public Binding binding(Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with("newOrder");
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(rabbitMqAddress);
        connectionFactory.setUsername(rabbitMqName);
        connectionFactory.setPassword(rabbitMqPassword);
        connectionFactory.setVirtualHost(rabbitMqVirtualHost);
        return connectionFactory;
    }
}
