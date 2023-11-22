import { GatewayProxyService } from './gateway-proxy/gateway-proxy.service';
import { PaymentInfoDTO } from '../dto/payment-info.dto';
import axios from 'axios';
import * as crypto from 'crypto';
import {PaymentDto} from "../dto/payment.dto";
import {AuthorizeDto} from "../dto/authorise.dto";


export class analyticsService {
  private readonly gatewayProxyService;

  constructor(loadBalancerHost: string) {
    this.gatewayProxyService = new GatewayProxyService(loadBalancerHost);
  }



}