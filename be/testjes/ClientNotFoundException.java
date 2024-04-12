package kdg.be.testjes;

public class ClientNotFoundException extends Exception {

    public ClientNotFoundException() {
        super("The requested client could not be found");
    }




}
