package groupB.newbankV5.customercare.interfaces;

import groupB.newbankV5.customercare.entities.Account;

public interface VirtualCardRequester {

    Account requestVirtualCard(Long accountId);
}
