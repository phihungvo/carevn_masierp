import {
  PAYMENT_REQUEST_STATUS,
  PAYMENT_REQUEST_TYPE,
} from './enumerations/payment-request';
import { PaginationParams } from './pagination.model';
import { ISupplier } from './supplier.model';

export interface IIncomingInvoice {
  id?: string;
  invoiceNo: string;
  seriNumber: string;
  nbr: string;
  createAt: string;
  supplierId: string;
  address: string;
  createBy: string;
  representative: string;
  note: string;
  phone: string;
  taxCode: string;
  paymentMethod: string;
  status: string;
  info: string;
  warehouse: string;
  goods: {
    id: string;
    code: string;
    name: string;
    detail: string;
    unit: string;
    quantity: string;
    unitPrice: string;
    totalAmount: string;
    tax: string;
  }[];
  attachments?: {
    id: string;
    fileName: string;
  }[];
  documentId: string;
  invoiceType: string;
  createdAt?: string;
  totalAmount?: number;
  currencyId?: string;
  currencyCode?: string;
  suppliers?: ISupplier;
  invoiceDate?: Date;
  totalVat?: number;

  grandTotal?: number; // Thay thế totalAmount
}

export interface IIncomingInvoiceParams extends PaginationParams {
  search?: string;
  startDate?: string;
  endDate?: string;
  departmentId?: string;
  employeeId?: string;
  invoiceType?: string;
  sort?: string;
  'supplierId.equals'?: string;
  'type.equals'?: PAYMENT_REQUEST_TYPE;
  currencyId?: string;
  'status.equals'?: PAYMENT_REQUEST_STATUS;
}
