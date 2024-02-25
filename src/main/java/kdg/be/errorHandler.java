package kdg.be;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class errorHandler {

    @ExceptionHandler({AfterTenOClockError.class})
    public ResponseEntity<String> errorHandle(){

        return  ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("everything is just fine");

    }


}
