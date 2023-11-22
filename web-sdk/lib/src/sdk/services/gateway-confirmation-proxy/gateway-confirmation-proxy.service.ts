import axios, { AxiosRequestConfig } from 'axios';
import { HttpStatus } from '@nestjs/common';
import { MerchantDTO } from '../../dto/merchant.dto';
import { ApplicationDto } from '../../dto/application.dto';
import { MerchantAlreadyExists } from '../../exceptions/merchant-already-exists.exception';
import { ApplicationNotFound } from '../../exceptions/application-not-found.exception';
import {AuthorizeDto} from "../../dto/authorise.dto";

export class GatewayConfirmationProxyService {
  private readonly _gatewayBaseUrl: string;
  private readonly _gatewayPath = '/api/gateway-confirmation';
  constructor(gateway_confirmation_host: string) {
    this._gatewayBaseUrl = `http://${gateway_confirmation_host}`;
  }
 
  async confirmPayment(transactionId: string, token: string): Promise<String> {
    try {
      const httpOptions: AxiosRequestConfig = {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
      };
      const response = await axios.post(
          `${this._gatewayBaseUrl}${this._gatewayPath}/${transactionId}`,
          httpOptions,
      );
      return response.data;
    } catch (error: any) {
      if (error.response && error.response.status === HttpStatus.NOT_FOUND) {
        console.error(error.response.data)
        console.error(`Application not found`);
        throw new ApplicationNotFound();
      } else {
        const errorMessage = `Error while processing payment: ${error.message}`;
        console.error(errorMessage);
        throw new Error(errorMessage);
      }
    }
  }

}
