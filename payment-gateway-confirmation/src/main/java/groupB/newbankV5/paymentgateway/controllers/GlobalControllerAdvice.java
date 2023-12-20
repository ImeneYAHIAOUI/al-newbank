package groupB.newbankV5.paymentgateway.controllers;

import groupB.newbankV5.paymentgateway.controllers.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;

@RestControllerAdvice(assignableTypes = {
        TransactionConfirmationController.class
})
public class GlobalControllerAdvice {

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleExceptions(ConstraintViolationException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("Constraint Violation");
        errorDTO.setDetails(e.getMessage()+ " " + e.getConstraintViolations() + " " + e.getLocalizedMessage() + " " + Arrays.toString(e.getSuppressed()));
        return errorDTO;
    }

}