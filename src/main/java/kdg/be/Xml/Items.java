package kdg.be.Xml;

import jakarta.xml.bind.annotation.XmlElement;

import java.util.ArrayList;
import java.util.List;

public class Items {
public Items(){
    System.out.println("ok");
}

@XmlElement(name = "Item")
public List<Item>item=new ArrayList<>();

}