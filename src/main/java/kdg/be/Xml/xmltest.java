package kdg.be.Xml;

import kdg.be.Xml.PurchaseOrder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class xmltest {


    @PostMapping(value = "/xml",consumes = MediaType.APPLICATION_XML_VALUE)

    public String testt(@RequestBody PurchaseOrder t){

//System.out.println(t.getAccount().clientType);

        return t.getItems().item.get(2).toString();
    }

}
