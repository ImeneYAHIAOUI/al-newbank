package groupB.newbankV5.transactions.entities;

public class SettleDto {
    private boolean settled;

    public SettleDto() {
    }

    public boolean isSettled() {
        return settled;
    }

    public void setSettled(boolean settled) {
        this.settled = settled;
    }
}
