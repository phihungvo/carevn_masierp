import { IFIle } from 'app/shared/model/file.model';
import { IEmployee } from './employee.model';
import {
  UNIFORM_ORDER_STATUS,
  UNIFORM_RELEASE_TYPE,
  UNIFORM_STATUS,
} from './enumerations/uniform.model';
import { PaginationParams } from './pagination.model';
import { ISupplier } from './supplier.model';
import { IUom } from './uom.model';

export interface IUniform {
  id?: string;
  name?: string;
  company?: string;
  department?: string;
  uniformDetails?: IUniformDetail[];
  uniformStocks?: IUniformStock[];
  code?: string;
  uomId?: string;
  uomDTO?: IUom;
  basePrice?: number;
  deleteAt?: string;
  status?: string;
}

export interface IUniformDetail {
  id?: string;
  quantity?: number;
  company?: string;
  uniformId?: string;
  uniform?: IUniform;
  uniformReleaseId?: string;
  uniformOrderId?: string;
  uniformReturnId?: string;
  initQuantity?: number;
  returnedQuantity?: number;
  quantityChange?: number;
  basePrice?: number;
  actualPrice?: number;
  uomId?: string;
  uomName?: string;
}

export interface IUniformDetailCreateDto {
  quantity: number;
  uniformId: string;
}

export interface IUniformStock {
  id?: string;
  stock: number;
  company?: string;
  department?: string;
  uniformId?: string;
  uniform?: IUniform;
  warehouseId?: string;
}

export interface IUniformStockParams extends PaginationParams {
  status?: UNIFORM_STATUS;
}

export interface IUniformOrder {
  id: string;
  name: string;
  quantity: number;
  remainQuantity?: number;
  date: string;
  status: UNIFORM_ORDER_STATUS;
  uniformFormDetails: IUniformDetail[];
  uniformOrderProcesses: IUniformOrderProcesses[];
  uniformOrderStockDTOS: IUniformOrderStock[];
  code?: string;
  supplierId?: string;
  supplierName?: string;
  supplier?: ISupplier;
}

export interface IUniformOrderProcesses {
  file: IFIle;
  reason?: string;
}

export interface IUniformOrderParams extends PaginationParams {
  startDate?: string;
  endDate?: string;
  status?: UNIFORM_ORDER_STATUS[];
  name?: string;
}

export interface IUniformOrderCreate {
  name: string;
  date: string;
  details: IUniformDetailCreateDto[];
  supplierId?: string;
  supplierName?: string;
}

export interface IUniformOrderUpdate {
  name: string;
  date: string;
  details: IUniformDetailCreateDto[];
}

export interface IUniformParams extends PaginationParams {
  search?: string;
  status?: UNIFORM_STATUS;
  companyId?: string;
  sort?: string;
}

export interface IUniformOrderProcess {
  status: UNIFORM_ORDER_STATUS;
  fileId?: string;
  reason?: string;
}

export interface IUniformOrderStock {
  uniformOrderId?: string;
  uniformFormDetailDTO?: IUniformDetail[];
  uniformFormDetail?: IUniformDetail[];
  id?: string;
  code?: string;
  totalQuantity?: number;
  warehouseId?: string;
  warehouseName?: string;
}

export interface IUniformRelease {
  id?: string;
  date: string;
  employeeId: string;
  employee?: IEmployee;
  quantity?: number;
  note?: string;
  fileId?: string;
  fileName?: string;
  type: UNIFORM_RELEASE_TYPE;
  cost: number;
  details: IUniformDetail[];
  uniformFormDetails?: IUniformDetail[];
  uniformReturn?: IUniformReturn[];
  isReturned?: boolean;
  remaining?: number;
  code?: string;
  warehouseId?: string;
  warehouseName?: string;
}

export interface IUniformReleaseParams extends PaginationParams {
  search?: string;
  type?: UNIFORM_RELEASE_TYPE[];
  uniformId?: string[];
  startDate?: string;
  endDate?: string;
  employeeIds?: string[];
}

export interface IUniformReturn {
  id?: string;
  date?: string;
  employeeId?: string;
  returnDetails?: IUniformDetail[];
  uniformReleaseId?: string;
  uniformFormDetail?: IUniformDetail[];
}
