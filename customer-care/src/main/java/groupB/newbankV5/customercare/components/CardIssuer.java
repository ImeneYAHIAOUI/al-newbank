package groupB.newbankV5.customercare.components;

import groupB.newbankV5.customercare.connectors.dto.CardGenerationRequestDto;
import groupB.newbankV5.customercare.connectors.dto.CreditCardDto;
import groupB.newbankV5.customercare.entities.Account;
import groupB.newbankV5.customercare.entities.CreditCard;
import groupB.newbankV5.customercare.exceptions.AccountNotFoundException;
import groupB.newbankV5.customercare.interfaces.AccountFinder;
import groupB.newbankV5.customercare.interfaces.ICreditCardNetwork;
import groupB.newbankV5.customercare.interfaces.VirtualCardRequester;
import groupB.newbankV5.customercare.repositories.AccountRepository;
import groupB.newbankV5.customercare.repositories.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CardIssuer implements VirtualCardRequester {

    private final ICreditCardNetwork creditCardNetworkProxy;
    private final CreditCardRepository creditCardRepository;
    private final AccountRepository accountRepository;
    private final AccountFinder accountFinder;


    @Autowired
    public CardIssuer(ICreditCardNetwork creditCardNetworkProxy, CreditCardRepository creditCardRepository, AccountRepository accountRepository, AccountFinder accountFinder) {
        this.creditCardNetworkProxy = creditCardNetworkProxy;
        this.creditCardRepository = creditCardRepository;
        this.accountRepository = accountRepository;
        this.accountFinder = accountFinder;
    }


    @Override
    public Account requestVirtualCard(Long accountId) {
        Optional<Account> account = accountFinder.findAccountById(accountId);
        if(account.isPresent()) {
            CardGenerationRequestDto cardGenerationRequestDto = new CardGenerationRequestDto();
            cardGenerationRequestDto.setCardHolderName(account.get().getCustomerProfile().getFirstName() + " " + account.get().getCustomerProfile().getLastName());
            CreditCardDto creditCardDto = creditCardNetworkProxy.getCreditCardDetails(cardGenerationRequestDto);
            CreditCard creditCard = new CreditCard();
            creditCard.setCardNumber(creditCardDto.getCardNumber());
            creditCard.setCardHolderName(creditCardDto.getCardHolderName());
            creditCard.setExpiryDate(creditCardDto.getExpirationDate());
            creditCard.setCvv(creditCardDto.getCvv());
            creditCard = creditCardRepository.save(creditCard);
            account.get().getCreditCards().add(creditCard);
            return accountRepository.save(account.get());
        }
        else {
            throw new AccountNotFoundException("Account not found");
        }
    }
}
