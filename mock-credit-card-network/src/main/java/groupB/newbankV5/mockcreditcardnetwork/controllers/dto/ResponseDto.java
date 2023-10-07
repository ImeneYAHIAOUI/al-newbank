package groupB.newbankV5.mockcreditcardnetwork.controllers.dto;


public class ResponseDto {
    boolean response;
    String message;

    public ResponseDto() {
    }
    public ResponseDto(boolean response) {
        this.response = response;
    }

    public ResponseDto(boolean response, String message) {
        this.response = response;
        this.message = message;
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


}
