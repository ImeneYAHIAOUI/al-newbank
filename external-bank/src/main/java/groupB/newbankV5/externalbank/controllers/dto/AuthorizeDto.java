package groupB.newbankV5.externalbank.controllers.dto;

public class AuthorizeDto {
    private boolean authorized;

    public AuthorizeDto() {
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }


}
