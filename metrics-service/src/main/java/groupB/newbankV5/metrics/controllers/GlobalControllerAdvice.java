package groupB.newbankV5.metrics.controllers;

import groupB.newbankV5.metrics.controllers.dto.ErrorDTO;
import groupB.newbankV5.metrics.exceptions.ApplicationNotFoundException;
import groupB.newbankV5.metrics.exceptions.InvalidTokenException;
import groupB.newbankV5.metrics.exceptions.MerchantNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {
        MetricsController.class,
})
public class GlobalControllerAdvice {


    @ExceptionHandler({ MerchantNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleExceptions(MerchantNotFoundException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError(e.getMessage());
        errorDTO.setDetails("Merchant not found");
        return errorDTO;
    }
    @ExceptionHandler({ApplicationNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleExceptions(ApplicationNotFoundException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError(e.getMessage());
        errorDTO.setDetails("Application not found");
        return errorDTO;
    }
    @ExceptionHandler({InvalidTokenException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleExceptions(InvalidTokenException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError(e.getMessage());
        errorDTO.setDetails("Invalid token provided");
        return errorDTO;
    }

}