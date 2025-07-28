import { PURCHASE_STATUS, PURCHASE_UNIT } from './enumerations/purchase.model';
import { IFIle } from './file.model';
import { PaginationParams } from './pagination.model';

interface IPurchaseRequestFile {
  id: string;
  filePath: string;
}

interface IPurchaseFileUpload {
  purchaseRequestId?: string;
  file?: string;
  contentType?: string;
  fileName?: string;
}

export interface IPurchaseDelivery {
  id?: string;
  deliveried: number;
  waitingDelivery?: number;
  createdAt?: string;
  purchaseRequestId?: string;
}

export interface IPurchase {
  id?: string;
  requestStatus?: PURCHASE_STATUS;
  productName: string;
  unit: PURCHASE_UNIT;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
  supplier?: string;
  note?: string;
  files?: string[];
  purchaseRequestFiles?: IFIle[];
  purchaseDelivery?: IPurchaseDelivery;
  createDate?: string;
  updatedDate?: string;
  isDeleted?: boolean;
}

export interface IPurchaseParams extends PaginationParams {
  searchString?: string;
  statuses?: PURCHASE_STATUS[];
  createdFrom?: string;
  createdTo?: string;
}

export interface PurchaseReviewCreate {
  documentId: string;
  employeeId1: string;
  employeeId2: string;
  employeeId3: string;
  employeeId4: string;
}

export interface PurchaseReview {
  id?: string;
  status: PURCHASE_STATUS.APPROVED | PURCHASE_STATUS.REJECTED;
  approvalStatusFile?: string;
  approvalStatusNote?: string;
  employeeId?: string;
}
