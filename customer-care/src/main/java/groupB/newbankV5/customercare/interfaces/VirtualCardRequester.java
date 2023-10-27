package groupB.newbankV5.customercare.interfaces;

import groupB.newbankV5.customercare.entities.Account;
import groupB.newbankV5.customercare.entities.CardType;

public interface VirtualCardRequester {


    Account requestVirtualCard(Account account, CardType cardType);
}
