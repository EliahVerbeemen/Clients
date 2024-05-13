package kdg.be;

import kdg.be.testjes.ClientNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class errorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({AfterTenOClockError.class})
    public ResponseEntity<String> errorHandle(){

        return  ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("everything is just fine");

    }

    @ExceptionHandler({ClientNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> clientError(ClientNotFoundException ex){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());

    }
}
