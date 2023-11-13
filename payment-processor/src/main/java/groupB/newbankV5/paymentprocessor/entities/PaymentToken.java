package groupB.newbankV5.paymentprocessor.entities;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;


@Table
public class PaymentToken {

    @PrimaryKey
    private String authToken;

    private boolean used;

    private LocalDateTime expiryDate;




    public PaymentToken(String authToken) {
        this.authToken = authToken;
        this.used = false;
        this.expiryDate = LocalDateTime.now().plusMinutes(1);
    }

    public PaymentToken() {

    }


    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }
}
