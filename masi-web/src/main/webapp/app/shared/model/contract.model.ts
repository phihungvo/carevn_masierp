import { ICustomer } from './customer.model';
import { IEmployee } from './employee.model';
import { CONTRACT_STATUS, CONTRACT_TYPE } from './enumerations/contract.model';
import { UNIT } from './enumerations/unit.model';
import { IFIle } from './file.model';
import { IItem } from './item.model';
import { PaginationParams } from './pagination.model';
import { IQuotationDetail } from './quotation.model';

export interface IContractFile {
  contractFileNew?: string[];
}

export interface IContract {
  id?: string;
  status: CONTRACT_STATUS;
  contractName: string;
  contractValidFrom: string;
  contractValidTo: string;
  contractType: CONTRACT_TYPE;
  contractOwner: string;
  contractTotal: number;
  proteinPercent?: string;
  approvalSignFile?: string;
  approvalSignFileAttachment?: IFIle;
  rejectNote?: string;
  lastUpdated?: boolean;
  createdDate?: string;
  isDeleted?: boolean;
  isActive?: boolean;
  files?: IContractFile[];
  fileAttachments?: IFIle[];
  contractFile?: IContractFile;
  owner?: IEmployee;
  customerId?: string;
  customer?: ICustomer;
  reviewAt?: string;
  reviewBy?: string;
  // deliveryTermFrom?: string;
  // deliveryTermTo?: string;
  // payTerm?: string;
  // payCondition?: string;
  // deliveryLocation?: string;
  contractMaterialDTOS?: IContractMaterial[];
  contractProductDTOS?: IContractProduct[];
  monetaryUnit?: UNIT;
  exchangeRate?: number;
  quotationDetails?: IQuotationDetail[];
  requestApprovals?: IRequestApprovals[];
  quotationId?: string;
  normalApprovals?: INormalApprovals[];
}

export interface INormalApprovals {
  company?: string;
  department?: string;
  documentId?: string;
  employee: {
    id: string;
    firstName?: string;
    lastName?: string;
    fullName?: string;
  };
  employeeId?: string;
  id?: string;
  index?: number;
  result?: boolean;
  approvedSign?: string;
  approvedSignName?: string;
  rejectNote?: string;
}

export interface IRequestApprovals {
  comany: string;
  employee: {
    id: string;
    firstName?: string;
    lastName?: string;
    fullName?: string;
  };
  createdBy: string;
  createdDate: string;
  department: string;
  documentId: string;
  employeeId: string;
  id: string;
  index: number;
  isDeleted: boolean;
  result?: boolean;
  approvedSign?: string;
  approvedSignName?: string;
  rejectNote?: string;
}

export interface IContractParams extends Partial<PaginationParams> {
  search?: string;
  contractType?: CONTRACT_TYPE;
  contractStatusList?: CONTRACT_STATUS[];
  proteinPercent?: string;
  contractValidFrom?: string;
  contractValidTo?: string;
  isExpired?: boolean;
  withTotal?: boolean;
  employeeOwner?: string[];
  companyName?: string[];
  withFull?: boolean;
  withSum?: boolean;
}

export interface IApproveContract {
  contractStatus?: string;
  approvalSign?: string;
  rejectNote?: string;
  approvalSignName?: string;
  reviewId?: string;
  isApproved?: boolean;
  id?: string;
  documentId?: string;
  approvedSignName?: string;
  approvedSign?: string;
}

export interface IContractLiquidConsent {
  approvalSign: string;
}

export interface IContractLiquidRefuse {
  rejectNote: string;
}

export interface IAdditive {
  id?: string;
  name: string;
  unitPrice: number;
  quantity: number;
  protein: string;
}

export interface IContractMaterial {
  id?: string;
  idContract?: string;
  idMaterial?: string;
  price?: number;
  unit?: string;
  quantity?: number;
  proteinParameters?: string;
  company?: string;
  department?: string;
  materialName?: string;
  name?: string;
  nameMaterialNew?: string;
  orderId?: string;
  itemId?: string;
  itemDTO?: IItem;
  manufactureOrderId?: string;
}

export interface IContractProduct {
  id?: string;
  idProduct?: string;
  productName?: string;
  note?: string;
  company?: string;
}

export interface IUnitOption {
  label: string;
  value: string;
}

export interface ContractReviewCreate {
  documentId?: string;
  employeeIds?: string[];
  approvedSign?: string;
  approvedSignName?: string;
  rejectNote?: string;
  isApproved?: boolean;
}
