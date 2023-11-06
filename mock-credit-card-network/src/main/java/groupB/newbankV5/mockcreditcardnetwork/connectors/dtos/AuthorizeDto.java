package groupB.newbankV5.mockcreditcardnetwork.connectors.dtos;

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
