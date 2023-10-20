package groupB.newbankV5.paymentsettlement.interfaces;

import groupB.newbankV5.paymentsettlement.connectors.dto.SettleDto;
import groupB.newbankV5.paymentsettlement.connectors.dto.IbanAmountDto;

public interface IExternalBank {
    SettleDto addAmount(IbanAmountDto transferDto);

    SettleDto deductAmount(IbanAmountDto transferDto);
}
