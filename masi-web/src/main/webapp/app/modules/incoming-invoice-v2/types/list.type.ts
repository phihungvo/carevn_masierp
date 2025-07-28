import { PAYMENT_REQUEST_STATUS, PAYMENT_REQUEST_TYPE } from "app/shared/model/enumerations/payment-request";
import { PaginationParams } from "app/shared/model/pagination.model";
import { ISupplier } from "app/shared/model/supplier.model";
import { InvoiceType } from "../constants/status";

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
  invoiceType: InvoiceType;
  createdAt?: Date;
  totalAmount?: number;
  currencyId?: string;
  currencyCode?: string;
  suppliers?: ISupplier;
  invoiceDate?: Date;
  debtDays?: number;
  currencyRate: number
  totalQuantity: number
  importFee: number
  totalAmountVat: number
  grandTotal: number
  patternNo: string
  totalFeeAfterImport: number
  totalAmountSupplies: number
  totalPreImportFee: number
  totalImportTax: number
  totalEnvTax: number
  totalVat: number
  totalAmountAfterVat: number
  vat: number
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
