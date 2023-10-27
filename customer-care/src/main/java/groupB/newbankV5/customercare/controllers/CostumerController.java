package groupB.newbankV5.customercare.controllers;

import groupB.newbankV5.customercare.components.dto.AccountCreationDto;
import groupB.newbankV5.customercare.controllers.dto.AccountDto;
import groupB.newbankV5.customercare.controllers.dto.ReserveFundsDto;
import groupB.newbankV5.customercare.controllers.dto.UpdateFundsDto;
import groupB.newbankV5.customercare.entities.Account;

import groupB.newbankV5.customercare.entities.CardType;
import groupB.newbankV5.customercare.exceptions.AccountNotFoundException;
import groupB.newbankV5.customercare.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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
    private final SavingsAccountHandler savingsAccountHandler;
    private final FundsHandler fundsHandler;

    @Autowired
    public CostumerController(AccountFinder accountFinder, AccountRegistration accountRegistration, VirtualCardRequester virtualCardRequester, SavingsAccountHandler savingsAccountHandler, FundsHandler fundsHandler) {
        this.accountFinder = accountFinder;
        this.accountRegistration = accountRegistration;
        this.virtualCardRequester = virtualCardRequester;
        this.savingsAccountHandler = savingsAccountHandler;
        this.fundsHandler = fundsHandler;
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
    public ResponseEntity<AccountDto> getAccountById(@PathVariable  long id) throws AccountNotFoundException {
        log.info("Getting account by id");
        Optional<Account> account = accountFinder.findAccountById(id);
        if(account.isEmpty()) {
            throw new AccountNotFoundException(String.valueOf(id));
        }
        AccountDto accountDto = AccountDto.accountDtoFactory(account.get());
        return ResponseEntity.status(200).body(accountDto);

    }


    @PostMapping()
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountCreationDto accountCreationDto) {
        log.info("Creating account");
        Account account = accountRegistration.createAccount(accountCreationDto);
        AccountDto accountDto = AccountDto.accountDtoFactory(account);
        return ResponseEntity.status(201).body(accountDto);
    }

    @PostMapping("{id}/virtualCard/debit")
    public ResponseEntity<AccountDto> createVirtualDebitCard(@PathVariable long id) {
        log.info("Creating virtual card");
        Account account = accountFinder.findAccountById(id).orElseThrow();
        AccountDto accountDto = AccountDto.accountDtoFactory(virtualCardRequester.requestVirtualCard(account, CardType.DEBIT));
        return ResponseEntity.status(201).body(accountDto);
    }

    @PostMapping("{id}/virtualCard/credit")
    public ResponseEntity<AccountDto> createVirtualCreditCard(@PathVariable long id) {
        log.info("Creating virtual card");
        Account account = accountFinder.findAccountById(id).orElseThrow();
        AccountDto accountDto = AccountDto.accountDtoFactory(virtualCardRequester.requestVirtualCard(account, CardType.CREDIT));
        return ResponseEntity.status(201).body(accountDto);
    }

    @PutMapping("{id}/funds")
    public ResponseEntity<AccountDto> updateFunds(@PathVariable long id, @RequestBody UpdateFundsDto updateFundsDto) {
        log.info("Updating funds");
        log.info("Updating funds");
        Account account = accountFinder.findAccountById(id).orElseThrow();
        try {
            account = fundsHandler.updateFunds(account, updateFundsDto.getAmount(), updateFundsDto.getOperation());
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }
        AccountDto accountDto = AccountDto.accountDtoFactory(account);
        return ResponseEntity.status(200).body(accountDto);

    }

    @PutMapping("{id}/reservedfunds")
    public ResponseEntity<AccountDto> updateReservedFunds(@PathVariable long id, @RequestBody ReserveFundsDto reserveFundsDto) {
        log.info("Updating reserved funds");
        Account account = accountFinder.findAccountById(id).orElseThrow();
        try {
            account = fundsHandler.addReservedFunds(account, reserveFundsDto.getAmount(), reserveFundsDto.getCardNumber(), reserveFundsDto.getExpirationDate(), reserveFundsDto.getCvv());
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }
        AccountDto accountDto = AccountDto.accountDtoFactory(account);
        return ResponseEntity.status(200).body(accountDto);

    }

    @PutMapping("{id}/releasefunds")
    public ResponseEntity<AccountDto> releaseReservedFunds(@PathVariable long id, @RequestBody ReserveFundsDto reserveFundsDto) {
        log.info("Releasing reserved funds");
        Account account = accountFinder.findAccountById(id).orElseThrow();
        try {
            account = fundsHandler.releaseReservedFunds(account, reserveFundsDto.getAmount());
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }
        AccountDto accountDto = AccountDto.accountDtoFactory(account);
        return ResponseEntity.status(200).body(accountDto);

    }

    @PutMapping("{id}/movetosavings")
    public ResponseEntity<AccountDto> moveToSavingsAccount(@PathVariable long id, @RequestBody UpdateFundsDto updateFundsDto) {
        log.info("Moving funds to savings account");
        Account account = accountFinder.findAccountById(id).orElseThrow();
        try {
            account = savingsAccountHandler.moveToSavingsAccount(account, updateFundsDto.getAmount());
        } catch (Exception e) {
            System.out.printf("Error: %s", e.getMessage());
            return ResponseEntity.status(400).build();
        }
        AccountDto accountDto = AccountDto.accountDtoFactory(account);
        return ResponseEntity.status(200).body(accountDto);

    }
}
