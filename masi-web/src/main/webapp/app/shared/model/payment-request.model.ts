import { IEmployee } from './employee.model';
import {
  PAYMENT_REQUEST_STATUS,
  PAYMENT_REQUEST_TYPE,
} from './enumerations/payment-request';
import { IFIle } from './file.model';
import { PaginationParams } from './pagination.model';
import { ISupplier } from './supplier.model';

export interface IPaymentRequest {
  id?: string;
  createdBy?: string;
  createdDate?: string;
  departmentId?: string;
  company?: string;
  files?: IFIle[];

  // NEW
  code?: string;
  order?: number;
  type?: string; // ENUM PAYMENT  ADVANCEMENT  REIMBURSEMENT
  attachments?: { fileId: string; fileName: string; createdAt: Date }[]; // Tep dinh kem
  totalAmount?: number;
  paidAmount?: number;
  remainingAmount?: number;
  paymentDate?: Date; // Ngày đề nghị
  reimbursementDate?: Date; // Ngày đề nghị
  note?: string;
  content?: string; // Nội dung
  status?: PAYMENT_REQUEST_STATUS; // ENUM
  supplierId?: string;
  suppliers?: ISupplier;
  paymentVoucher?: string; // Phiếu chi
  paymentVoucherAmount?: number; // Tổng tiền chi
  employeeId?: string;
  employee?: IEmployee;
  requestApprovals?: {
    id?: string;
    index: number;
    employeeId: string;
    employee?: IEmployee;
    updatedAt?: string;
    approvedSign?: string;
    approvedSignName?: string;
    result?: boolean;
  }[];
  paymentDetails?: {
    id?: string;
    invoiceId?: string;
    incomingInvoice?: {
      invoiceNo?: string;
      invoiceDate?: Date;
      content?: string;
      totalAmount?: number;
      series?: string;
      note?: string;
      documentId?: string;
      grandTotal?: number;
    };
  }[];
  remainingBalance?: number;
  overSpent?: number;
  reimbursementDTOS?: {
    id?: string;
    reimbursementId?: string;
    advanceId?: string;
    advancement?: IPaymentRequest;
  }[];
  createdByEmployee?: IEmployee;
}

export interface IPostPaymentRequestDto {
  code: string;
  order: number;
  type: string; // ENUM PAYMENT  ADVANCEMENT  REIMBURSEMENT
  attachments: { fileId: string; fileName: string; createdAt: Date }[]; // Tep dinh kem
  totalAmount: number;
  paidAmount: number;
  remainingAmount: number;
  paymentDate: Date; // Ngày đề nghị
  note: string;
  content: string; // Nội dung
  status: string; // ENUM
  supplierId?: string;
  paymentVoucher: string; // Phiếu chi
  paymentVoucherAmount: number; // Tổng tiền chi
  employeeId?: string;
  requestApprovals: {
    index: number;
    employeeId: string;
  }[];
  paymentDetails: {
    invoiceId: string;
    incomingInvoice: {
      invoiceNo: string;
      invoiceDate: Date;
      content: string;
      totalAmount: number;
      series: string;
      note: string;
      documentId: string;
    };
  }[];
  remainingBalance?: number;
  overSpent?: number;
  reimbursementDTOS?: {
    reimbursementId?: string;
    advanceId?: string;
    advancement?: IPaymentRequest;
  }[];
}

export interface IPatchPaymentRequestDto {
  id: string;
  code?: string;
  type: string; // ENUM PAYMENT  ADVANCEMENT  REIMBURSEMENT
  attachments: { fileId: string; fileName: string; createdAt: Date }[]; // Tep dinh kem
  totalAmount: number;
  paidAmount: number;
  remainingAmount: number;
  paymentDate: Date; // Ngày đề nghị
  note: string;
  content: string; // Nội dung
  status: string; // ENUM
  supplierId?: string;
  paymentVoucher: string; // Phiếu chi
  paymentVoucherAmount: number; // Tổng tiền chi
  employeeId?: string;
  requestApprovals: {
    index: number;
    employeeId: string;
  }[];
  paymentDetails: {
    invoiceId: string;
    incomingInvoice: {
      invoiceNo: string;
      invoiceDate: Date;
      content: string;
      totalAmount: number;
      series: string;
      note: string;
      documentId: string;
      grandTotal?: number;
    };
  }[];
  remainingBalance?: number;
  overSpent?: number;
  reimbursementDTOS?: {
    id?: string;
    reimbursementId?: string;
    advanceId?: string;
    advancement?: IPaymentRequest;
  }[];
}

export interface IVoucher {
  numberVoucher: number;
  day: string;
  totalAmount: number;
  note: string;
}

export interface IPaymentRequestParams extends PaginationParams {
  search?: string;
  sort?: string;
  'status.equals'?: PAYMENT_REQUEST_STATUS;
  'type.equals'?: PAYMENT_REQUEST_TYPE;
  'paymentDate.equals'?: string;
  'paymentDate.greaterThanOrEqual'?: string;
  'paymentDate.lessThanOrEqual'?: string;
  'content.contains'?: string;
  employeeId?: string;
  'code.contains'?: string;
  'createdDate.equals'?: string;
  'supplierId.equals'?: string;
}

export interface IPaymentRequestApproval {
  approvedSign: string;
  approvedSignName: string;
}

export interface IPaymentRequestReject {
  rejectNote: string;
}
