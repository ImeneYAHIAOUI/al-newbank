package groupB.newbankV5.paymentgateway.controllers;

import groupB.newbankV5.paymentgateway.controllers.dto.ErrorDTO;
import groupB.newbankV5.paymentgateway.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.security.InvalidKeyException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestControllerAdvice(assignableTypes = {
        TransactionerController.class
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

    @ExceptionHandler({MerchantAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleExceptions(MerchantAlreadyExistsException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError(e.getMessage());
        errorDTO.setDetails("Merchant already exists");
        return errorDTO;
    }
    @ExceptionHandler({ApplicationAlreadyExists.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleExceptions(ApplicationAlreadyExists e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError(e.getMessage());
        errorDTO.setDetails("Application already exists");
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


    @ExceptionHandler({MerchantNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleExceptions(MerchantNotFoundException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError(e.getMessage());
        errorDTO.setDetails("Merchant not found");
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

    @ExceptionHandler({CCNException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleExceptions(CCNException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError(e.getMessage());
        errorDTO.setDetails("Credit card network rejected transaction");
        return errorDTO;
    }

    @ExceptionHandler({InvalidKeyException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleExceptions(InvalidKeyException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError(e.getMessage());
        errorDTO.setDetails("Invalid key");
        return errorDTO;
    }

    @ExceptionHandler({TimeoutException.class})
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public ErrorDTO handleExceptions(TimeoutException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError(e.getMessage());
        errorDTO.setDetails("timeout exception");
        return errorDTO;
    }

}