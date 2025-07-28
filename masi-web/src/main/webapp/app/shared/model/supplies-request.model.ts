import { SupplierContract } from 'app/validation/supplies-request.validation';
import { SuppliesRequestStatus } from './enumerations/supplies-request';
import { IItem } from './item.model';
import { PaginationParams } from './pagination.model';
import { ISupplier } from './supplier.model';
import { IUom } from './uom.model';

export interface SuppliesDetail {
  id:                  string;
  code:                string;
  requestNumber:       string;
  requestDate:         Date;
  requestByEmployeeId: string;
  supplierId?: string;
  supplier: any;
  supplierFullName?: string;
  supplierPosition?: string;
  supplierPhone?: string;
  supplierEmail?: string;
  requestStatus:       string;
  totalAmount:         number;
  totalAmountAfterVat: number;
  note:                string;
  createdByEmployeeId: string;
  totalQuantity:       number;
  attachedFiles:       AttachedFile[];
  company:             string;
  department:          string;
  isDeleted:           boolean;
  createdBy:           string;
  createdDate:         Date;
  updatedBy:           string;
  updatedAt:           Date;
  suppliesItemDTO:     SuppliesItemDTO[];
  requestApprovals:    RequestApproval[];
  requestTypeId:       string;
  requestType:         RequestType;
  deliveredQuantity:   number;
  remainingQuantity:   number;
  isReview:            boolean;
  supplierContracts:   SupplierContract[];
}

export interface AttachedFile {
  fileId:    string;
  fileName:  string;
  createdAt: Date;
}

export interface RequestApproval {
  id:               string;
  index:            number;
  documentId:       string;
  employeeId:       string;
  result:           boolean;
  approvedSign:     string;
  approvedSignName: string;
  company:          string;
  department:       string;
  isDeleted:        boolean;
  createdBy:        string;
  createdDate:      Date;
  updatedBy:        string;
  updatedAt:        Date;
}

export interface RequestType {
  id:         string;
  code:       string;
  name:       string;
  note:       string;
  isDeleted:  boolean;
  createdAt:  Date;
  createdBy:  string;
  updatedAt:  Date;
  updatedBy:  string;
  company:    string;
  department: string;
}

export interface SuppliesItemDTO {
  id:                  string;
  idSuppliesRequest:   string;
  idItem:              string;
  idUom:               string;
  quantity:            number;
  price:               number;
  company:             string;
  department:          string;
  item:                Item;
  suppliers:           Suppliers;
  isDeleted:           boolean;
  createdBy:           string;
  createdDate:         Date;
  note:                string;
  vat:                 number;
  vatId:               string;
  totalAmount:         number;
  totalAmountAfterVat: number;
}

export interface Item {
  id:             string;
  code:           string;
  name:           string;
  uomId:          string;
  uom:            Uom;
  attribute:      Attribute;
  company:        string;
  isDeleted:      boolean;
  itemCategory:   ItemCategory;
  itemCategoryId: string;
  percentProtein: number;
  vatRate:        number;
  vatId:          string;
  unitPrice:      number;
  itemType:       string;
  isActive:       boolean;
}

export interface Attribute {
  material:    { [key: string]: number };
  material_id: string;
}

export interface ItemCategory {
  id:              string;
  code:            string;
  name:            string;
  company:         string;
  isDeleted:       boolean;
  createdBy:       string;
  createdDate:     Date;
  items:           any[];
  warehouseTypeId: string;
}

export interface Uom {
  id:       string;
  name:     string;
  createAt: Date;
  createBy: string;
  company:  string;
}

export interface Suppliers {
  suppliesDetails: any[];
}


export interface ISuppliesRequest {
  id?: string;
  code?: string;
  requestNumber?: string;
  requestDate?: string;
  requestByEmployeeId?: string;
  departmentId?: string;
  suppliesId?: string;
  requestStatus?: SuppliesRequestStatus;
  totalAmount?: number;
  totalAmountAfterVat?: number;
  suppliesItemDTO?: ISuppliesItem[];
  note: string;
  company?: string;
  department?: string;
  createdAt?: string;
  vat?: number;
  requestApprovals?: ISuppliesRequestReview[];
  createdBy?: string;
  requestType?: {
    id: string;
    name: string;
  },
  deliveredQuantity?: number;
  remainingQuantity?: number;
  attachedFiles?: {
    createdAt: string;
    fileId: string;
    fileName: string;
  }[],
  totalQuantity?: number;
}

export interface ISuppliesItem {
  id?: string;
  idSuppliesRequest?: string;
  idItem: string;
  quantity: number;
  idUom?: string;
  item?: Partial<IItem>;
  uom?: IUom;
  totalPrice?: number;
  price: number;
  company?: string;
  department?: string;
  suppliesId?: string;
  suppliers?: Partial<ISupplier>;
  image?: string[];
  imageIds?: string
  note?: string
  totalAmount: number;
  totalAmountAfterVat: number;
  vat: number;
}

export interface ISuppliesRequestReview {
  "id": string,
  "index": number,
  "documentId": string,
  "employeeId": string,
  "result": string,
  "company": string,
  "department": string,
  "isDeleted": string,
  "createdBy": string,
  "createdDate": string,
  "updatedAt": string,
  approvedSign?: string;
}

export interface ISuppliesRequestApprove {
  documentId?: string;
  rejectNote?: string;
  result?: boolean;
  approvedSign?: string;
  approvedSignName?: string;
}
export interface ISuppliesRequestParams extends PaginationParams {
  'requestDate.greaterThanOrEqual'?: string;
  'requestDate.lessThanOrEqual'?: string;
  'status.equals'?: string;
  'requestTypeId.equals'?: string;
  search?: string;
  'isDeleted.equals'?: boolean;
  'tmp.requestDate.greaterThanOrEqual'?: string;
  'tmp.requestDate.lessThanOrEqual'?: string;
  "supplierId.equals"?: string
}

export interface SuppliesRequestTypes {
  id:         string;
  code:       string;
  name:       string;
  note:       string;
  attribute:  Object;
  isDeleted:  boolean;
  createdAt:  Date;
  createdBy:  string;
  updatedAt:  Date;
  updatedBy:  string;
  deletedAt:  Date;
  deletedBy:  string;
  company:    string;
  department: string;
}
