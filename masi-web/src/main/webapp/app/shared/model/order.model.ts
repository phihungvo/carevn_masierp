import { IContract } from './contract.model';
import { IEmployee } from './employee.model';
import {
  ORDER_REVIEW_SOLUTION,
  ORDER_STATUS,
} from './enumerations/order.model';
import { IFIle } from './file.model';
import { PaginationParams } from './pagination.model';
import { IProductionCommand } from './production-command.model';

export interface IOrder {
  id?: string;
  status?: ORDER_STATUS;
  orderCode: string;
  orderReviews?: OrderReview[];
  numberOrder?: number;
  dateOrder?: string;
  contractId: string;
  contract?: IContract;
  packageType: string;
  note?: string;
  finishDate: string;
  lastUpdated?: string;
  createdDate?: string;
  isDeleted?: boolean;
  waitUntil?: string;
  deliveryTermFrom?: string;
  deliveryTermTo?: string;
  payTerm?: string;
  payCondition?: string;
  deliveryLocation?: string;
  employees?: {
    id: string;
    firstName: string;
    lastName: string;
    result: boolean;
  }[];
  qualityIndexes?: {
    id: string;
    name: string;
    value: string;
  }[];
  contractMaterialUse?: string[];
}

export interface IOrderParams extends PaginationParams {
  searchString?: string;
  statuses?: ORDER_STATUS[];
  customerId?: string;
}

export interface OrderReviewCreate {
  documentId: string;
  employeeId1: string;
  employeeId2: string;
  employeeId3: string;
  employeeId4: string;
  employeeId5: string;
  employeeId6: string;
  employeeId7: string;
  employeeId8: string;
}

export interface OrderReview {
  id?: string;
  status: ORDER_STATUS.APPROVED | ORDER_STATUS.REJECTED;
  approvalSignFile?: IFIle;
  approvalStatusSignFile?: string;
  approvalStatusNote?: string;
  approvalSolution?: ORDER_REVIEW_SOLUTION;
  awaitingDate?: string;
  employeeId?: string;
  employee?: IEmployee;
  lastUpdated?: string;
}

export interface IOrderWithManufactureOrders extends IOrder {
  manufactureOrders?: IProductionCommand[];
}
