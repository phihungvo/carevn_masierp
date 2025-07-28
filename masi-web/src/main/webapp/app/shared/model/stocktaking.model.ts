import { IEmployee } from './employee.model';
import { IFIle } from './file.model';
import { IItem } from './item.model';
import { PaginationParams } from './pagination.model';
import { Item } from './supplies-request.model';
import { IWarehouse } from './warehouse.model';

export enum STOCKTAKING_STATUS {
  NEW = 'NEW',
  WAITING_APPROVED = 'WAITING_APPROVED',
  REJECTED = 'REJECTED',
  CANCELLED = 'CANCELLED',
  APPROVED = 'APPROVED',
  COMPLETED = 'COMPLETED',
}

export interface IStocktakingDetail {
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

export interface IStocktaking {
  createdBy?: string;
  createdAt?: Date;

  id: string;
  code?: string;

  checkDate: Date;

  warehouseId?: string;
  warehouse: IWarehouse;

  amountOfDifference?: number;

  note?: string;

  attachment: { fileId: string; fileName: string; createdAt: Date }[]; // Tep dinh kem
  supplierContractDetails: IStocktakingDetail[];
  status: STOCKTAKING_STATUS;
  files: IFIle[];

  requestApprovals: {
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
  }[];

  approver1: string;
  approver1Employee: IEmployee;
  approver2: string;
  approver2Employee: IEmployee;
  approver3: string;
  approver3Employee: IEmployee;

  listInventoriesCheckDetail: {
    id?: string;
    itemId: string;
    item?: Item;
    uomName?: string;
    totalQty?: string;
    systemQuantity: number;
    actualQuantity: number;
    note?: string;
  }[];
}

export interface IStocktakingFilterParams extends PaginationParams {
  search?: string;
  'supplierId.equals'?: string;
  'code.contains'?: string;
}
