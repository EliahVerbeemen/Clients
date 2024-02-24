package kdg.be.Modellen;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Test {


    private int test;
    private String testje;

    @Id
    @GeneratedValue
    private Long testId;

    public Test(int test, String testje) {
        this.test = test;
        this.testje = testje;

    }

    public Test() {
    }

    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }

    public String getTestje() {
        return testje;
    }

    public void setTestje(String testje) {
        this.testje = testje;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    @OneToOne(optional = true)
    public Klant klant;

    @Override
    public String toString(){

        return "getal: " + this.test+ "woord: "+ this.testje;
    }
}
