package groupB.newbankV5.paymentprocessor.controllers.dto;

public class CreditCardResponseDto {

    private boolean valid;
    private String message;
    private String authToken;

    public CreditCardResponseDto() {
    }

    public CreditCardResponseDto(boolean valid, String message, String authToken) {
        this.valid = valid;
        this.message = message;
        this.authToken = authToken;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
