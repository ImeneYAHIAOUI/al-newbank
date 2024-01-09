package groupB.newbankV5.metrics.entities;


import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rtransaction")

public class RedisTransaction {
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }



    private String transactionId;


}
