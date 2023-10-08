package groupB.newbankV5.customercare.controllers;

import groupB.newbankV5.customercare.components.dto.AccountCreationDto;
import groupB.newbankV5.customercare.controllers.dto.AccountDto;
import groupB.newbankV5.customercare.entities.Account;

import groupB.newbankV5.customercare.interfaces.AccountFinder;
import groupB.newbankV5.customercare.interfaces.AccountRegistration;
import groupB.newbankV5.customercare.interfaces.VirtualCardRequester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("virtualCard/{id}")
    public ResponseEntity<AccountDto> createVirtualCard(@PathVariable long id) {
        log.info("Creating virtual card");
        AccountDto accountDto = AccountDto.accountDtoFactory(virtualCardRequester.requestVirtualCard(id));
        return ResponseEntity.status(201).body(accountDto);
    }


}
