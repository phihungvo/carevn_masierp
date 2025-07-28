import { IEmployee } from './employee.model';
import { IItem } from './item.model';
import { PaginationParams } from './pagination.model';
import { ISupplier } from './supplier.model';
import { IWarehouse } from './warehouse.model';

export enum INVENTORIES_STATUS {
  NEW = 'NEW',
  WAITING_APPROVED = 'WAITING_APPROVED',
  REJECTED = 'REJECTED',
  CANCELLED = 'CANCELLED',
  APPROVED = 'APPROVED',
  COMPLETED = 'COMPLETED',
}

export interface IInventoriesType {
  id: string;
  code: string;
  name: string;
  description: string;
  company: string;
  department: string;
}

export interface IInventoriesStorage {
  id: string;
  code: string;
  receiverUserId: string;
  deliveryDate: string;
  inventoriesTypeId: string;
  inventoriesType: IInventoriesType;
  dateCreate: string;
  incomingWarehouseId: string;
  incomingWarehouse: IWarehouse;
  totalQuantity: number;
  totalAmount: number;
  purchaseContractId: string;
  // purchaseContract: IPurchaseContract;
  status: INVENTORIES_STATUS;
  isInvoice: boolean;
  isNoReview: boolean;
  note: string;
  item?: any;
  customerId: string;
  customer: ISupplier;
  inventoriesDetails: Partial<{
    id: string;
    code: string;
    itemId: string;
    item: IItem;

    quantity: number;
    price: number;
    note: string;
    uomId: string;
    totalPrice: number;

    // Xuất kho
    vatRate?: number;
    vatId?: string;
    vatAmount?: number;
    registerDate?: Date;
    depreciationDate?: Date;
    departmentId?: string;
    usageMonth?: number;
    holder?: string;
    depreciationAllocation?: string;
    expenseAccount?: string;
    costElements?: string;
    unitPrice?: number;
  }>[];
  orderId?: string;
  invoiceId?: string;
  productionId?: string;
  invoices?: { id?: string; invoiceNo?: string }[];
  invoice?: { id?: string; invoiceNo?: string };
  createdAt: string;
  file: { fileId: string; fileName: string; createdAt: Date }[]; // Tep dinh kem
  attribute: {
    shipper: string;
    shipperPhone?: string;

    // Xuất kho
    shipperAddress?: string;
    receiverName?: string;
    receiverPhone?: string;

    workspaceId?: string;
  };
  purchaseContract?: {
    id?: string;
    contractName?: string;
    contractCode?: string;
  };
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
  createdBy?: string;
  createdByEmployee?: IEmployee;
  employeeId?: string;

  isReview?: boolean;

  quantity?: number;
}

export interface IInventoriesStorageParams extends PaginationParams {
  search?: string;
  'id.notEquals'?: string;
  'code.contains'?: string;
  'warehouseGroupType.equals'?: string;
  'status.equals'?: string;
  'status.doesNotContain'?: string;
  'createdAt.greaterThanOrEqual'?: string;
  'createdAt.lessThanOrEqual'?: string;
  'customerId.equals'?: string;
  'inventoriesTypeId.equals'?: string;
  'incomingWarehouseId.equals'?: string;
  'isInvoice.equals'?: boolean;
  'checkDepreciation'?: boolean;
  'userPosition.equals'?: string;
}

export interface IInventoriesTypeParams extends PaginationParams {}

export interface IInventoryStoragePayload {
  code: string;
  receiverUserId: string;
  deliveryDate: string;
  inventoriesTypeId: string;
  customerRecipientId: string;
  incomingWarehouseId: string;
  quantity: number;
  totalAmount: number;
  purchaseContractId: string;
  status: INVENTORIES_STATUS;
}
