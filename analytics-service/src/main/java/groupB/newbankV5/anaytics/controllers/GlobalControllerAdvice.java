package groupB.newbankV5.anaytics.controllers;

import groupB.newbankV5.anaytics.controllers.dto.ErrorDTO;
import groupB.newbankV5.anaytics.exceptions.MerchantNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {
        TransactionController.class,
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
}