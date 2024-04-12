package kdg.be.SerializatieHelpers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import jakarta.persistence.AttributeConverter;
import kdg.be.Modellen.Order;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SetSerialisatie extends StdDeserializer<Set<Order>> {

public SetSerialisatie(){
    this(null);
}


    protected SetSerialisatie(Class<?>  src) {
        super(src);
    }

    @Override
    public Set<Order> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        return new HashSet<Order>();
    }
}
