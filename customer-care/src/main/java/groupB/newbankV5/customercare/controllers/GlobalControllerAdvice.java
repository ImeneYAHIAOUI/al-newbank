package groupB.newbankV5.customercare.controllers;

import groupB.newbankV5.customercare.exceptions.AccountNotFoundException;
import groupB.newbankV5.customercare.controllers.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice(assignableTypes = {
        CostumerController.class
})
public class GlobalControllerAdvice {

    @ExceptionHandler({AccountNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleExceptions(AccountNotFoundException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setError("Bad request");
        errorDTO.setDetails(e.getMessage());
        return errorDTO;
    }



}