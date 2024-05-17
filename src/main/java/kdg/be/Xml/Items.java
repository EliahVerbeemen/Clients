package kdg.be.Xml;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.ArrayList;
import java.util.List;
@Embeddable
public class Items {

@XmlElement(name = "Item")
@OneToMany
public List<Item>item=new ArrayList<>();

}
