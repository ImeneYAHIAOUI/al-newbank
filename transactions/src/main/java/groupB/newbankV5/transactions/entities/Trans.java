package groupB.newbankV5.transactions.entities;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table
public class Trans {
    @PrimaryKey
    private UUID id;
    private String amount;
    public Trans() {
        this.id = UUID.randomUUID();
    }

    public Trans( String amount) {
        this.id = UUID.randomUUID();
        this.amount = amount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}