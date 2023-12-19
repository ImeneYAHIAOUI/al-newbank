package groupB.newbankV5.metrics.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Payload {

    @JsonProperty("after")
    private  Transaction transaction;

    public Transaction getTransaction() {
        return transaction;
    }
}
