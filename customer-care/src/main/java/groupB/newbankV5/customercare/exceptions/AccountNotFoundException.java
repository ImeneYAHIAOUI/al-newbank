package groupB.newbankV5.customercare.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String id) {
        super("Account with id " + id+" not found");
    }



}
