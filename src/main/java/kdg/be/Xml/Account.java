package kdg.be.Xml;

import jakarta.persistence.Embeddable;
import jakarta.xml.bind.annotation.*;
import kdg.be.Modellen.Client;

public class Account {

  @XmlAttribute(name = "Type")
  public  Client.ClientType clientType;


    @Override
    public String toString() {
        return "OK";
    }

  private String name;

  public String getName() {
    return name;
  }

  @XmlValue
  public void setName(String name) {
    this.name = name;
  }





}