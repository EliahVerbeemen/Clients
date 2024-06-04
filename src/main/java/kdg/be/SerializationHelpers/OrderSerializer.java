package kdg.be.SerializationHelpers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import kdg.be.Modellen.Order;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OrderSerializer extends StdSerializer<Order> {
    protected OrderSerializer(Class<Order> t) {
        super(t);
    }
    protected OrderSerializer() {
        super((Class<Order>) null);
    }
    protected OrderSerializer(JavaType type) {
        super(type);
    }
    protected OrderSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }
    protected OrderSerializer(StdSerializer<?> src) {
        super(src);
    }

    @Override
    public void serialize(Order order, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("orderId", order.getOrderId());
        ObjectMapper objectMapper = new ObjectMapper();
        String products = objectMapper.writeValueAsString(order.getProducts());
        gen.writeStringField("products", products);
        gen.writeEndObject();
    }
}
