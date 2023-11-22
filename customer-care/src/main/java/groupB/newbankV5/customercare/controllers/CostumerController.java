package groupB.newbankV5.customercare.controllers;

import groupB.newbankV5.customercare.components.dto.AccountCreationDto;
import groupB.newbankV5.customercare.controllers.dto.*;
import groupB.newbankV5.customercare.entities.Account;

import groupB.newbankV5.customercare.entities.AccountType;
import groupB.newbankV5.customercare.exceptions.AccountNotFoundException;
import groupB.newbankV5.customercare.interfaces.AccountFinder;
import groupB.newbankV5.customercare.interfaces.AccountRegistration;
import groupB.newbankV5.customercare.interfaces.BusinessAccount;
import groupB.newbankV5.customercare.interfaces.SavingsAccountHandler;
import groupB.newbankV5.customercare.interfaces.VirtualCardRequester;
import groupB.newbankV5.customercare.entities.CardType;
import groupB.newbankV5.customercare.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final BusinessAccount businessAccount;

    private final FundsHandler fundsHandler;

    @Autowired
    public CostumerController(AccountFinder accountFinder, AccountRegistration accountRegistration, VirtualCardRequester virtualCardRequester, SavingsAccountHandler savingsAccountHandler, FundsHandler fundsHandler, BusinessAccount businessAccount) {
        this.accountFinder = accountFinder;
        this.accountRegistration = accountRegistration;
        this.virtualCardRequester = virtualCardRequester;
        this.savingsAccountHandler = savingsAccountHandler;
        this.businessAccount = businessAccount;
        this.fundsHandler = fundsHandler;
    }




    @GetMapping()
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        log.info("\u001B[32mGetting all accounts\u001B[0m");
        List<Account> accountRegistration = accountFinder.findAll();
        accountRegistration.removeIf(account -> account.getType().equals(AccountType.NEWBANK_VIRTUAL_ACCOUNT));
        List<AccountDto> accountDto = accountRegistration.stream().map(AccountDto::accountDtoFactory).toList();
        return ResponseEntity.status(200).body(accountDto);
    }

    @GetMapping("bankAccount")
    public ResponseEntity<bankAccountDto> getAllBankAccounts() {
        log.info("\u001B[32mGetting bank account\u001B[0m");
        Account account = accountFinder.findByType(AccountType.NEWBANK_VIRTUAL_ACCOUNT).orElseThrow();
        bankAccountDto accountDto = new bankAccountDto(account.getBalance());
        return ResponseEntity.status(200).body(accountDto);
    }

    @GetMapping("search")
    public ResponseEntity<AccountDto> searchAccount(@RequestParam(name = "iban", required = false) String iban,
                                                    @RequestParam(name = "number", required = false) String cardNumber,
                                                    @RequestParam(name = "date", required = false) String expiryDate,
                                                    @RequestParam(name = "cvv", required = false) String cvv) {
        log.info("\u001B[32mReceived account search request\u001B[0m");
        if(iban != null) {
            Account account = accountFinder.findByIban(iban).orElseThrow();
            AccountDto accountDto = AccountDto.accountDtoFactory(account);
            return ResponseEntity.status(200).body(accountDto);
        }
        else if(cardNumber != null && expiryDate != null && cvv != null) {
            Account account = accountFinder.findByCreditCard(cardNumber, expiryDate, cvv).orElseThrow();
            AccountDto accountDto = AccountDto.accountDtoFactory(account);
            log.info("\u001B[32mSending account information\u001B[0m");
            return ResponseEntity.status(200).body(accountDto);
        }
        else {
            return ResponseEntity.status(400).build();
        }

    }

    @GetMapping("{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable  long id) throws AccountNotFoundException {
        log.info("\u001B[32mGetting account by id " + id + "\u001B[0m");
        Optional<Account> account = accountFinder.findAccountById(id);
        if(account.isEmpty()) {
            throw new AccountNotFoundException(String.valueOf(id));
        }
        AccountDto accountDto = AccountDto.accountDtoFactory(account.get());
        return ResponseEntity.status(200).body(accountDto);

    }


    @PostMapping()
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountCreationDto accountCreationDto) {
        log.info("Received account creation request");
        Account account = accountRegistration.createAccount(accountCreationDto);
        AccountDto accountDto = AccountDto.accountDtoFactory(account);
        log.info("Account created");
        return ResponseEntity.status(201).body(accountDto);
    }

    @PostMapping("{id}/virtualCard/debit")
    public ResponseEntity<AccountDto> createVirtualDebitCard(@PathVariable long id) {
        log.info("\u001B[32mReceived virtual debit card creation request\u001B[0m");
        Account account = accountFinder.findAccountById(id).orElseThrow();
        AccountDto accountDto = AccountDto.accountDtoFactory(virtualCardRequester.requestVirtualCard(account, CardType.DEBIT));
        log.info("\u001B[32mVirtual debit card created\u001B[0m");
        return ResponseEntity.status(201).body(accountDto);
    }

    @PostMapping("{id}/virtualCard/credit")
    public ResponseEntity<AccountDto> createVirtualCreditCard(@PathVariable long id) {
        log.info("Received virtual credit card creation request");
        Account account = accountFinder.findAccountById(id).orElseThrow();
        AccountDto accountDto = AccountDto.accountDtoFactory(virtualCardRequester.requestVirtualCard(account, CardType.CREDIT));
        log.info("Virtual credit card created");
        return ResponseEntity.status(201).body(accountDto);
    }

    @PutMapping("{id}/funds")
    public ResponseEntity<AccountDto> updateFunds(@PathVariable long id, @RequestBody UpdateFundsDto updateFundsDto) {

        Account account = accountFinder.findAccountById(id).orElseThrow();
        try {
            account = fundsHandler.updateFunds(account, updateFundsDto.getAmount(), updateFundsDto.getOperation());
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }
        AccountDto accountDto = AccountDto.accountDtoFactory(account);
        return ResponseEntity.status(200).body(accountDto);

    }

    @PutMapping("reservedfunds")
    public ResponseEntity<AccountDto> updateReservedFunds( @RequestBody ReserveFundsDto reserveFundsDto) {
        log.info("\u001B[34mReceived reserve amount "+reserveFundsDto.getAmount()+" request\u001B[0m");
        Account account = accountFinder.findByCreditCard(reserveFundsDto.getCardNumber(), reserveFundsDto.getExpirationDate(), reserveFundsDto.getCvv()).orElseThrow();
        try {
            account = fundsHandler.addReservedFunds(account, reserveFundsDto.getAmount(), reserveFundsDto.getCardNumber(), reserveFundsDto.getExpirationDate(), reserveFundsDto.getCvv());
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }
        AccountDto accountDto = AccountDto.accountDtoFactory(account);
        log.info("\u001B[34mReserved funds\u001B[0m");
        return ResponseEntity.status(200).body(accountDto);

    }

    @PutMapping("releasefunds")
    public ResponseEntity<AccountDto> releaseReservedFunds( @RequestBody ReleaseFundsDto releaseFundsDto) {
        Account account ;
        try {
            account = fundsHandler.releaseReservedFunds(releaseFundsDto);
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }
        AccountDto accountDto = AccountDto.accountDtoFactory(account);
        return ResponseEntity.status(200).body(accountDto);

    }

    @PutMapping("{id}/movetosavings")
    public ResponseEntity<AccountDto> moveToSavingsAccount(@PathVariable long id, @RequestBody UpdateFundsDto updateFundsDto) {
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

    @PostMapping("{id}/upgrade")
    public ResponseEntity<AccountDto> upgrade(@PathVariable long id){
        try{
            Account account = accountFinder.findAccountById(id).orElseThrow();
            businessAccount.upgradeToBusinessAccount(account);
            AccountDto accountDto = AccountDto.accountDtoFactory(account);
            return ResponseEntity.status(200).body(accountDto);
        } catch (Exception e) {
            System.out.printf("Error: %s", e.getMessage());
            return ResponseEntity.status(400).build();
        }

    }

    @PutMapping("{id}/deduceweeklylimit")
    public ResponseEntity<AccountDto> deduceWeeklyLimit(@PathVariable long id, @RequestBody UpdateWeeklyLimitDto updateWeeklyLimitDto) {
        Account account = accountFinder.findAccountById(id).orElseThrow();
        try {
            account = fundsHandler.deduceFromWeeklyLimit(account, updateWeeklyLimitDto.getAmount());
        } catch (Exception e) {
            System.out.printf("Error: %s", e.getMessage());
            return ResponseEntity.status(400).build();
        }
        AccountDto accountDto = AccountDto.accountDtoFactory(account);
        return ResponseEntity.status(200).body(accountDto);

    }
    @PostMapping("batchReleaseFunds")
    public ResponseEntity<Object> batchReleaseFunds(@RequestBody List<ReleaseFundsDto> releaseFundsDtos) {
        log.info("\u001B[35mReceived batch release funds request\u001B[0m");
        for(ReleaseFundsDto releaseFundsDto : releaseFundsDtos) {
            try {
                fundsHandler.releaseReservedFunds(releaseFundsDto);
            } catch (Exception e) {
                return ResponseEntity.status(400).build();
            }
        }
        log.info("\u001B[35mBatch release funds completed\u001B[0m");
        return ResponseEntity.status(200).build();


    }

}
