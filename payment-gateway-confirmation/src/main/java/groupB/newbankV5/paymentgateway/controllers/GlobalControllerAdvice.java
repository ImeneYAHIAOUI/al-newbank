package groupB.newbankV5.paymentgateway.controllers;

import groupB.newbankV5.paymentgateway.controllers.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice(assignableTypes = {
        TransactionConfirmationController.class
})
public class GlobalControllerAdvice {

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleExceptions(ConstraintViolationException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("Bad request");
        errorDTO.setDetails(e.getMessage());
        return errorDTO;
    }

}