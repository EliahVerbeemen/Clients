package kdg.be.SerializationHelpers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import kdg.be.Modellen.Product;
import kdg.be.Modellen.ProductState;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ProductDeserializer extends StdDeserializer<Product> {
    protected ProductDeserializer(Class<?> vc) {
        super(vc);
    }
    protected ProductDeserializer() {
        super((Class<?>) null);
    }
    protected ProductDeserializer(JavaType valueType) {
        super(valueType);
    }
    protected ProductDeserializer(StdDeserializer<?> src) {
        super(src);
    }




    @Override
    public Product deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        try {
            Long id = node.get("productId").asLong();
            String name = node.get("name").asText();
            String productStatusString = node.get("productStatus").asText();
            ProductState productState = ProductState.valueOf(productStatusString);
            if (productState.equals(ProductState.FINAL)) {
                productState = ProductState.NEW;
            } else {
                productState = ProductState.DEACTIVATED;
            }
            return new Product(0.0, name, id, productState);

        } catch (Exception ex) {

            return new Product();
        }
    }
}
