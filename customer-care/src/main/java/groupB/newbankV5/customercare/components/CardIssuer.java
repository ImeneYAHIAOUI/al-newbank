package groupB.newbankV5.customercare.components;

import groupB.newbankV5.customercare.connectors.dto.CardGenerationRequestDto;
import groupB.newbankV5.customercare.connectors.dto.CreditCardDto;
import groupB.newbankV5.customercare.entities.*;
import groupB.newbankV5.customercare.interfaces.ICreditCardNetwork;
import groupB.newbankV5.customercare.interfaces.VirtualCardRequester;
import groupB.newbankV5.customercare.repositories.AccountRepository;
import groupB.newbankV5.customercare.repositories.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Component
@Transactional
public class CardIssuer implements VirtualCardRequester {

    private final ICreditCardNetwork creditCardNetworkProxy;
    private final AccountRepository accountRepository;
    private final CreditCardRepository creditCardRepository;


    @Autowired
    public CardIssuer(ICreditCardNetwork creditCardNetworkProxy, AccountRepository accountRepository, CreditCardRepository creditCardRepository) {
        this.creditCardNetworkProxy = creditCardNetworkProxy;
        this.accountRepository = accountRepository;
        this.creditCardRepository = creditCardRepository;
    }


    @Override
    public Account requestVirtualCard(Account account, CardType cardType) {
        CardGenerationRequestDto cardGenerationRequestDto = new CardGenerationRequestDto();
        cardGenerationRequestDto.setCardHolderName(account.getCustomerProfile().getFirstName() + " " + account.getCustomerProfile().getLastName());
        CreditCardDto creditCardDto = creditCardNetworkProxy.getCreditCardDetails(cardGenerationRequestDto);
        BigDecimal cardLimit = account.getType() == AccountType.PERSONAL ? Constants.DEFAULT_CARD_LIMIT : Constants.DEFAULT_CARD_LIMIT_BUSINESS;
        CreditCard creditCard = new CreditCard(creditCardDto.getCardNumber(), creditCardDto.getCardHolderName(), creditCardDto.getExpirationDate(), creditCardDto.getCvv(), account,  cardLimit, cardType);
        CreditCard savedCreditCard = creditCardRepository.saveAndFlush(creditCard);
        account.addCreditCard(savedCreditCard);
        return accountRepository.saveAndFlush(account);

    }
}
