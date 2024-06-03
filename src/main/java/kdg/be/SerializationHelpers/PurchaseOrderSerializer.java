package kdg.be.SerializationHelpers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import kdg.be.Xml.PurchaseOrder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PurchaseOrderSerializer extends StdSerializer<PurchaseOrder> {
    protected PurchaseOrderSerializer(Class<PurchaseOrder> t) {
        super(t);
    }
    protected PurchaseOrderSerializer() {
        super((Class<PurchaseOrder>) null);
    }
    protected PurchaseOrderSerializer(JavaType type) {
        super(type);
    }
    protected PurchaseOrderSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }
    protected PurchaseOrderSerializer(StdSerializer<?> src) {
        super(src);
    }

    @Override
    public void serialize(PurchaseOrder value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("purchaseOrderNumber", value.getPurchaseOrderNumber());
        gen.writeStringField("orderDate", value.getOrderDate().toString());
        ObjectMapper mapper = new ObjectMapper();
        String account = mapper.writeValueAsString(value.getAccount());
        gen.writeStringField("account", account);
        Map<String, Integer> items = new HashMap<>();
        value.getItems().item.forEach(i -> {
            try {
                items.put(mapper.writeValueAsString(i), i.getQuantity());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        gen.writeStringField("items", mapper.writeValueAsString(items));
        gen.writeEndObject();
    }
}
