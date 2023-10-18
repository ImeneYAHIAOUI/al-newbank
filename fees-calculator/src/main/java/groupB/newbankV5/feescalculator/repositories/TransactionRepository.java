package groupB.newbankV5.feescalculator.repositories;

import groupB.newbankV5.feescalculator.entities.Merchant;
import groupB.newbankV5.feescalculator.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByMerchant(Merchant merchant);
}
