package kdg.be.Modellen;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue
    private Long ProductNumber;

    /*
 @ElementCollection
    private Map<Ingredient,int> Ingredienten;
*/
private double Prijs=0.0;

private String Naam;

    public Long getProductNumber() {
        return ProductNumber;
    }

    public void setProductNumeber(Long productNumeber) {
        ProductNumber = productNumeber;
    }

    public double getPrijs() {
        return Prijs;
    }

    public void setPrijs(double prijs) {
        Prijs = prijs;
    }

    public String getNaam() {
        return Naam;
    }

    public void setNaam(String naam) {
        Naam = naam;
    }


    //deze applicatie heeft geen interesse in een recept
  /*  public String getRecept() {
        return Recept;
    }

    public void setRecept(String recept) {
        Recept = recept;
    }*/

    public boolean isGedeactiveerd() {
        return Gedeactiveerd;
    }

    public void setGedeactiveerd(boolean gedeactiveerd) {
        Gedeactiveerd = gedeactiveerd;
    }

  //  private String Recept;
private boolean Gedeactiveerd=false;
//private boolean Finaal=false;

public Product(){}
public Product(String  naam, double prijs ){
    Prijs=prijs;

    Naam=naam;


}


}
