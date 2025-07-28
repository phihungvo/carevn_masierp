import {
  PAYMENT_REQUEST_TYPE,
  PAYMENT_REQUEST_STATUS,
} from './enumerations/payment-request';
import { PaginationParams } from './pagination.model';
import { IPaymentRequest } from './payment-request.model';

export interface IPaymemtDetails {
  id: string;
  paymentRequestId: string;
  invoiceId: string;
  paymentRequest: IPaymentRequest;
}

export interface IPostPaymemtDetailsDto {
  // id: string;
  paymentRequestId: string;
  invoiceId: string;
  // paymentRequest: IPaymemtRequest
}

export interface IPatchPaymemtDetailsDto {
  // id: string;
  paymentRequestId: string;
  invoiceId: string;
  // paymentRequest: IPaymemtRequest
}

export interface IVoucher {
  numberVoucher: number;
  day: string;
  totalAmount: number;
  note: string;
}

export interface IPaymemtDetailsParams extends PaginationParams {
  search?: string;
  status?: PAYMENT_REQUEST_STATUS;
  type?: PAYMENT_REQUEST_TYPE;
  'paymentRequestId.equals'?: string;
}
