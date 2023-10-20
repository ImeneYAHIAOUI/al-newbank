package groupB.newbankV5.paymentsettlement.connectors.dto;

import java.math.BigDecimal;

public class SettleDto {
    private boolean settled;

    public boolean isSettled() {
        return settled;
    }

    public void setSettled(boolean settled) {
        this.settled = settled;
    }
}
