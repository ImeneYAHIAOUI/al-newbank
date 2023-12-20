
package groupB.newbankV5.metrics.entities;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transaction")
public class TransactionEnvelop {
    private Payload payload;

    public Payload getPayload() {
        return payload;
    }
}


