package groupB.newbankV5.customercare.controllers;

import groupB.newbankV5.customercare.components.dto.AccountCreationDto;
import groupB.newbankV5.customercare.controllers.dto.AccountDto;
import groupB.newbankV5.customercare.controllers.dto.CreditCardDto;
import groupB.newbankV5.customercare.controllers.dto.UpdateFundsDto;
import groupB.newbankV5.customercare.entities.Account;

import groupB.newbankV5.customercare.entities.CreditCard;
import groupB.newbankV5.customercare.interfaces.AccountFinder;
import groupB.newbankV5.customercare.interfaces.AccountRegistration;
import groupB.newbankV5.customercare.interfaces.VirtualCardRequester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = CostumerController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class CostumerController {

    private static final Logger log = Logger.getLogger(CostumerController.class.getName());
    public static final String BASE_URI = "/api/costumer";

    private final AccountFinder accountFinder;
    private final AccountRegistration accountRegistration;
    private final VirtualCardRequester virtualCardRequester;

    @Autowired
    public CostumerController(AccountFinder accountFinder, AccountRegistration accountRegistration, VirtualCardRequester virtualCardRequester) {
        this.accountFinder = accountFinder;
        this.accountRegistration = accountRegistration;
        this.virtualCardRequester = virtualCardRequester;
    }




    @GetMapping()
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        log.info("Getting all accounts");
        List<Account> accountRegistration = accountFinder.findAll();
        List<AccountDto> accountDto = accountRegistration.stream().map(AccountDto::accountDtoFactory).toList();
        return ResponseEntity.status(200).body(accountDto);
    }

    @GetMapping("search")
    public ResponseEntity<AccountDto> searchAccount(@RequestParam(name = "iban", required = false) String iban,
                                                    @RequestParam(name = "number", required = false) String cardNumber,
                                                    @RequestParam(name = "date", required = false) String expiryDate,
                                                    @RequestParam(name = "cvv", required = false) String cvv) {
        log.info("Searching account");
        if(iban != null) {
            Account account = accountFinder.findByIban(iban).orElseThrow();
            AccountDto accountDto = AccountDto.accountDtoFactory(account);
            return ResponseEntity.status(200).body(accountDto);
        }
        else if(cardNumber != null && expiryDate != null && cvv != null) {
            Account account = accountFinder.findByCreditCard(cardNumber, expiryDate, cvv).orElseThrow();
            AccountDto accountDto = AccountDto.accountDtoFactory(account);
            return ResponseEntity.status(200).body(accountDto);
        }
        else {
            return ResponseEntity.status(400).build();
        }

    }

    @GetMapping("{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable  long id) {
        log.info("Getting account by id");
        Account account = accountFinder.findAccountById(id).orElseThrow();
        AccountDto accountDto = AccountDto.accountDtoFactory(account);
        return ResponseEntity.status(200).body(accountDto);
    }


    @PostMapping()
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountCreationDto accountCreationDto) {
        log.info("Creating account");
        Account account = accountRegistration.createAccount(accountCreationDto);
        AccountDto accountDto = AccountDto.accountDtoFactory(account);
        return ResponseEntity.status(201).body(accountDto);
    }

    @PostMapping("{id}/virtualCard")
    public ResponseEntity<AccountDto> createVirtualCard(@PathVariable long id) {
        log.info("Creating virtual card");
        Account account = accountFinder.findAccountById(id).orElseThrow();
        AccountDto accountDto = AccountDto.accountDtoFactory(virtualCardRequester.requestVirtualCard(account));
        return ResponseEntity.status(201).body(accountDto);
    }

    @PutMapping("{id}/funds")
    public ResponseEntity<AccountDto> updateFunds(@PathVariable long id, @RequestBody UpdateFundsDto updateFundsDto) {
        log.info("Updating funds");
        log.info("Updating funds");
        Account account = accountFinder.findAccountById(id).orElseThrow();
        BigDecimal balance = account.getBalance();
        try {
            account = accountRegistration.updateFunds(account, updateFundsDto.getAmount(), updateFundsDto.getOperation());
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }
        AccountDto accountDto = AccountDto.accountDtoFactory(account);
        return ResponseEntity.status(200).body(accountDto);

    }
}
