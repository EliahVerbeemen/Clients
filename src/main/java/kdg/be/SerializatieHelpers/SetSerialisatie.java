package kdg.be.SerializatieHelpers;

import jakarta.persistence.AttributeConverter;
import kdg.be.Modellen.Order;

import java.util.Set;

public class SetSerialisatie implements AttributeConverter <Set<Order>,String> {
    @Override
    public String convertToDatabaseColumn(Set<Order> attribute) {
        return null;
    }

    @Override
    public Set<Order> convertToEntityAttribute(String dbData) {
        return null;
    }
}
