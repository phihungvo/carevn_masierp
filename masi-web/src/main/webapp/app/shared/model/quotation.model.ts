import { IContractMaterial } from './contract.model';
import { ICustomer } from './customer.model';
import { IEmployee } from './employee.model';
import { QUOTATION_STATUS } from './enumerations/quotation.model';
import { PaginationParams } from './pagination.model';

export interface IQuotation {
  id?: string;
  name?: string;
  status?: QUOTATION_STATUS;
  description: string;
  totalPrice?: string;
  fileId?: string;
  fileName?: string;
  rejectNote?: string;
  approverId?: string;
  approver?: IEmployee;
  company?: string;
  department?: string;
  customerRejectNote?: string;
  quotationDetails?: IQuotationDetail[];
  deliveryLocation?: string;
  deliveryLocationEn?: string;
  deliveryDate?: string;
  packaging?: string;
  minimumWeight?: string;
  packagingEn?: string;
  paymentMethod?: string;
  paymentMethodEn?: string;
  priceType?: string;
  priceTypeEn?: string;
  materialCriteria?: string;
  materialCriteriaEn?: string;
  customerId?: string;
  customer?: ICustomer;
  processAt?: string;
  approvalSignName?: string;

  approvalSignFile?: string;
}

export interface IQuotationDetail {
  id?: string;
  index?: number;
  deliveryLocation?: string;
  deliveryLocationEn?: string;
  deliveryDate?: string;
  packaging?: string;
  packagingEn?: string;
  minimumWeight?: string;
  weight?: string;
  price?: string;
  priceType?: string;
  priceTypeEn?: string;
  paymentMethod?: string;
  paymentMethodEn?: string;
  materialId?: string;
  material?: IContractMaterial;
  note?: string;
  nitrogen180Price?: string;
  nitrogen150Price?: string;
  materialCriteria?: string;
  materialCriteriaEn?: string;
  company?: string;
  persisted?: boolean;
  quotationId?: string;
}

export interface IQuotationCreate {
  name?: string;
  description?: string;
  deliveryLocation?: string;
  deliveryLocationEn?: string;
  deliveryDate?: string;
  packaging?: string;
  minimumWeight?: string;
  packagingEn?: string;
  paymentMethod?: string;
  paymentMethodEn?: string;
  priceType?: string;
  priceTypeEn?: string;
  materialCriteria?: string;
  materialCriteriaEn?: string;
  quotationDetails?: IQuotationDetail[];
  customerId?: string;
}

export interface IQuotationParams extends PaginationParams {
  companyId?: string;
  search?: string;
  status?: QUOTATION_STATUS[];
}

export interface IQuotationInternalApprove {
  status?: QUOTATION_STATUS;
  rejectNote?: string;
  approvalSignFile?: string;
  approvalSignName?: string;
}

export interface IQuotationInternalReject {
  rejectNote: string;
}

export interface IQuotationCustomerProcess {
  status: QUOTATION_STATUS;
  rejectNote?: string;
  fileId?: string;
  fileName?: string;
}
