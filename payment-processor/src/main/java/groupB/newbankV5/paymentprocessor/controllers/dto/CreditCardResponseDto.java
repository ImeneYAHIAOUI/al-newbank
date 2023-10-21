package groupB.newbankV5.paymentprocessor.controllers.dto;

public class CreditCardResponseDto {

    private boolean response;
    private String message;
    private String authToken;

    private String AccountIBAN;
    private String AccountBIC;

    public CreditCardResponseDto() {
    }

    public CreditCardResponseDto(boolean response, String message, String authToken) {
        this.response = response;
        this.message = message;
        this.authToken = authToken;
    }

    public CreditCardResponseDto(boolean response, String message, String authToken, String accountIBAN, String accountBIC) {
        this.response = response;
        this.message = message;
        this.authToken = authToken;
        AccountIBAN = accountIBAN;
        AccountBIC = accountBIC;
    }

    public String getAccountIBAN() {
        return AccountIBAN;
    }

    public void setAccountIBAN(String accountIBAN) {
        AccountIBAN = accountIBAN;
    }

    public String getAccountBIC() {
        return AccountBIC;
    }

    public void setAccountBIC(String accountBIC) {
        AccountBIC = accountBIC;
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
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
