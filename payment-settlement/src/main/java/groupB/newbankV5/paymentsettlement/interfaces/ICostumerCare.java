package groupB.newbankV5.paymentsettlement.interfaces;

import groupB.newbankV5.paymentsettlement.connectors.dto.ReleaseFundsDto;
import groupB.newbankV5.paymentsettlement.entities.Transaction;

import java.util.List;

public interface ICostumerCare {
    void releaseFunds(List<ReleaseFundsDto> accounts);
}
