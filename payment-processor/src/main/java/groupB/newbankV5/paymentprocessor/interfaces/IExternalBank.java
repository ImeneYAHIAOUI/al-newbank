package groupB.newbankV5.paymentprocessor.interfaces;

import groupB.newbankV5.paymentprocessor.connectors.dto.AuthorizeDto;
import groupB.newbankV5.paymentprocessor.controllers.dto.TransferDto;

public interface IExternalBank {
    AuthorizeDto authorizeTransfer(TransferDto transferDto);
}
