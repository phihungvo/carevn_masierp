import { IFIle } from './file.model';
import { IIncomingInvoice } from './incoming-invoice.model';
import { IItem } from './item.model';
import { PaginationParams } from './pagination.model';
import { ISupplier } from './supplier.model';
import { ISuppliesRequest } from './supplies-request.model';

export enum SUPPLIER_CONTRACT_STATUS {
  NEW = 'NEW',
  WAITING = 'WAITING',
  WAITING_APPROVE = 'WAITING_APPROVE',
  WAITING_LIQUIDATION = 'WAITING_LIQUIDATION',
  REJECTED = 'REJECTED',
  REJECTED_LIQUIDATION = 'REJECTED_LIQUIDATION',
  CANCELLED = 'CANCELLED',
  APPROVED = 'APPROVED',
  LIQUIDATED = 'LIQUIDATED',
  EXPIRED = 'EXPIRED',
  COMPLETED = 'COMPLETED',
}

export interface ISupplierContractDetail {
  id?: string;
  supplyItemId?: string;
  supplyItem?: IItem;
  unitId: string;
  unit?: { id: string; name: string };
  quantity?: number;
  note?: string;

  code?: string;
  price?: number;
  totalPrice?: number;
  totalAmount?: number;

  vatId?: string;
  vatRate?: string;
  vatAmount?: string;
}

interface IRequestApprovals {
  index: number;
  employeeId: string;
  employee?: {
    id: string;
    firstName: string;
    lastName: string;
    fullName: string;
    result: boolean;
  };
  result?: boolean;
  approvedDate?: Date;
  approvedSign?: string;
  approvedSignName?: string;
  rejectNote?: string;
}

export interface ISupplierContract {
  createdBy?: string;
  createdAt?: Date;

  id: string;
  contractCode: string;
  contractName: string;
  supplierId: string;
  contractDate: string;
  deliveryEstDate: string;
  deliveryStatus?: string;
  startDate: string;
  endDate: string;
  contractAmount: number;
  note?: string;
  attachments: { fileId: string; fileName: string; createdAt: Date }[]; // Tep dinh kem
  supplierContractDetails: ISupplierContractDetail[];
  files: IFIle[];
  incomingInvoices?: IIncomingInvoice[];
  suppliesRequestId: string;
  suppliesRequest?: ISuppliesRequest[];
  totalAmount?: number;
  totalAmountAfterTax?: number;
  totalQuantity?: number;
  paymentTermNumber?: number;

  supplier?: ISupplier;
  supplierFullName?: string;
  supplierAddress?: string;
  supplierPosition?: string;
  supplierPhone?: string;
  supplierEmail?: string;
  status?: string;

  requestApprovals: IRequestApprovals[];

  liquidationRequestApprovals: IRequestApprovals[];
}

export interface ISupplierContractFilterParams extends PaginationParams {
  search?: string;
  'supplierId.equals'?: string;
}
