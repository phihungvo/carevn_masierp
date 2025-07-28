import { IFIle } from 'app/shared/model/file.model';
import { DateObject } from 'react-multi-date-picker';
import { IWorkspace } from 'app/shared/model/workspace.model';
import { PaginationParams } from 'app/shared/model/pagination.model';
import { VOUCHER_STATUS } from 'app/shared/model/enumerations/voucher.model';

export interface IVoucherRequestPayment {
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
  requestApprovals: {
    index: number;
    employeeId: string;
    employee: {
      id: string;
      firstName: string;
      lastName: string;
      fullName: string;
      result: boolean;
    };
    result: boolean;
    approvedDate: Date;
    approvedSign: string;
    approvedSignName: string;
    rejectNote: string;
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
}

export interface IVoucherRequestOfAdvance {
  id: string;
  voucherNumber: string;
  voucherDate: DateObject;
  employee: { id: string; fullName: string };
  workspace: IWorkspace;
  numberMoney: string;
  advanceDate: DateObject;
  content: string;
  file: IFIle;
  status: VOUCHER_STATUS;
}

export interface IVoucherAdvanceRepayment {
  id: string;
  voucherNumber: string;
  voucherDate: DateObject;
  employee: { id: string; fullName: string };
  workspace: IWorkspace;
  note: string;
  file: IFIle;
  status: VOUCHER_STATUS;
}

export interface IVoucherChangeLog {
  id: string;
  changeDate: string;
  change: IChangeItem[];
  changeBy: string;
  employeeId: string;
}

export interface IChangeItem {
  newValue: string;
  oldValue: string;
  fieldName: string;
}

export interface IVoucherRequestPaymentParams extends PaginationParams {
  search?: string;
  status?: VOUCHER_STATUS[];
}

export interface IVoucherRequestPaymentFormParams extends PaginationParams {}
