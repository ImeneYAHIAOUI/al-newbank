package groupB.newbankV5.transactions.components;

import groupB.newbankV5.transactions.entities.Transaction;
import groupB.newbankV5.transactions.interfaces.IPersister;
import org.springframework.stereotype.Component;

@Component
public class Persister implements IPersister {

    @Override
    public void saveTransaction(Transaction transaction) {

    }

}
