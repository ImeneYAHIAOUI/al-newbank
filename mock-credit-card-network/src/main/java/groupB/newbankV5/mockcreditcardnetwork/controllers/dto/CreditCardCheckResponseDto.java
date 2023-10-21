package groupB.newbankV5.mockcreditcardnetwork.controllers.dto;


import java.security.SecureRandom;

public class CreditCardCheckResponseDto {
    boolean response;
    String message;
    String authToken;

    String AccountIBAN;
    String AccountBIC;

    public CreditCardCheckResponseDto() {
    }
    public CreditCardCheckResponseDto(boolean response) {
        this.response = response;
    }

    public CreditCardCheckResponseDto(boolean response, String message) {
        this.response = response;
        this.message = message;
    }

    public CreditCardCheckResponseDto(boolean response, String message, String authToken) {
        this.response = response;
        this.message = message;
        this.authToken = authToken;
    }

    public boolean isResponse() {
        return response;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
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

    public boolean getResponse() {
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

    public void setMessage() {
        if (this.response) {
            this.message = "authorized";
        } else {
            this.message = "authorization failed";
        }
    }

    public void setAuthToken(){
        this.authToken = generateRandomString(10);
    }

    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }


}
