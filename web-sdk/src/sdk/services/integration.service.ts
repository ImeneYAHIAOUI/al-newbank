import { GatewayProxyService } from '../services/gateway-proxy/gateway-proxy.service';
import { MerchantDTO } from '../dto/merchant.dto';
import { ApplicationInfo } from '../dto/application-info.dto';
import { ApplicationDto } from '../dto/application.dto';
import { PaymentService } from './payment.service';
import {  Logger, HttpException, HttpStatus } from '@nestjs/common';
import Axios, { AxiosResponse } from 'axios';
import { MerchantAlreadyExists } from '../exceptions/merchant-already-exists.exception';
import { ApplicationAlreadyExists } from '../exceptions/application-already-exists.exception';
import { InvalidMerchantInformationException } from '../exceptions/invalid-merchant-information.exception';
import { InvalidApplicationInformationException } from '../exceptions/invalid-application-information.exception';
export class IntegrationService {
  private readonly logger = new Logger(IntegrationService.name);
  private paymentService: PaymentService;
  constructor(private readonly gatewayProxyService: GatewayProxyService) {}

async integrateMerchant(name: string, email: string, iban: string, bic: string): Promise<MerchantDTO> {
    if (!name || !email || !iban || !bic) {
        throw new InvalidMerchantInformationException("Name, email, IBAN, and BIC are required.");
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        throw new InvalidMerchantInformationException("Invalid email address.");
    }
    // doit commencer par 2 lettres suivies de 2 chiffres  puis de 4 lettres ou chiffres, et enfin de 7 chiffres.
    const ibanRegex = /^[A-Z]{2}\d{2}[A-Z0-9]{4}\d{7}([A-Z0-9]?){0,16}$/;
    if (!ibanRegex.test(iban)) {
        throw new InvalidMerchantInformationException("Invalid IBAN.");
    }
    //  doit commencer par 6 lettres, suivies de 2 lettres ou chiffres + il peut y avoir entre 0 et 3 caractères supplémentaires  des lettres ou des chiffres.
    const bicRegex = /^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$/;
    if (!bicRegex.test(bic)) {
        throw new InvalidMerchantInformationException("Invalid BIC.");
    }

    const merchant = { name, email, bankAccount: { IBAN: iban, BIC: bic } };
    return await this.gatewayProxyService.integrateMerchant(merchant);
}



async integrateApplication(
    name: string,
    email: string,
    url: string,
    description: string,
    merchantId: string
): Promise<ApplicationDto> {
    if (!name || !email || !url || !description || !merchantId) {
        throw new InvalidApplicationInformationException("All fields are required.");
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        throw new InvalidApplicationInformationException("Invalid email address.");
    }
    const integratedApplication = {
        name,
        email,
        url,
        description,
        merchantId,
    };
    return await this.gatewayProxyService.integrateApplication(integratedApplication);
}


async integrateService(application: ApplicationInfo): Promise<{ id: string, apiKey: string }> {
  try {
    const merchantIntegrationResult: MerchantDTO = await this.integrateMerchant(application.name, application.email,application.IBAN, application.BIC);
    this.logger.log(`Merchant integration successful for ${merchantIntegrationResult.name}. Merchant ID: ${merchantIntegrationResult.id}`);
    const applicationIntegrationResult: ApplicationDto = await this.integrateApplication(
      merchantIntegrationResult.name,
      merchantIntegrationResult.email,
      application.url,
      application.description,
      merchantIntegrationResult.id.toString()
    );
    const applicationId: string = applicationIntegrationResult.id.toString();
    this.logger.log(`Application integration successful for ${application.name}. Application ID: ${applicationId}`);
    const apiKeyResult: string = await this.gatewayProxyService.createApiKey(applicationId);
    this.logger.log(`API key created successfully for application ID: ${applicationId}`);
    return { id: applicationId, apiKey: apiKeyResult };
  } catch (error) {
    if (error instanceof InvalidMerchantInformationException) {
      this.logger.error(`Invalid Merchant Information: ${error.message}`);
    } else if (error instanceof InvalidApplicationInformationException) {
      this.logger.error(`Invalid Application Information: ${error.message}`);
    }
    throw new Error(`Error in integration service: ${error.message}`);
  }
}


  //async getApiKey(applicationId: string): Promise<string> {
    //try {
      //const apiKey = await this.gatewayProxyService.getApiKey(applicationId);
      //return apiKey;
    //} catch (error) {
      //throw new Error(`Error in integration service: ${error.message}`);
    //}
  //}
}

