package kdg.be.Xml;

import jakarta.persistence.Embeddable;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;
import kdg.be.Modellen.Enums.ClientType;

@Embeddable
public class Account {

    @XmlAttribute(name = "Type")
    public ClientType clientType;
    private String name;
    public String getName() {
        return name;
    }
    @XmlValue
    public void setName(String name) {
        this.name = name;
    }


}
