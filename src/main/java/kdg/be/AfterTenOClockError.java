package kdg.be;

public class AfterTenOClockError extends RuntimeException{

public AfterTenOClockError(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
    public AfterTenOClockError() {
        super();
    }
}



