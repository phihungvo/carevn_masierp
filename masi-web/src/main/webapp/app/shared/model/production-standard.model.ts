import { ORDER_STATUS } from './enumerations/order.model';
import { PRODUCTION_STANDARD_STATUS } from './enumerations/production-standard.model';
import { PaginationParams } from './pagination.model';
import { IProductionCommand } from './production-command.model';

// Tạo mới Định Mức Sản Xuất
export interface IPostProductionStandardDto {
  code: string;
  name: string;
  dueDate: string;
  productionPowderQty: number;
  quantity?: number;
  note?: string;
}

export interface IPostProductionStandardResponse {
  message: string;
}

// Lấy Định Mức Sản Xuất
export interface IProductionStandard {
  id: string;
  code?: string;
  name: string;
  unit: string;
  startDate: string;
  zonedStartDate: string;
  dueDate: string;
  zonedDueDate: string;
  productionPowderQty: number;
  workspace: string;
  materialId: string;
  manufactureOrderDTOS: IProductionCommand[];
  quantity?: number;
  status: PRODUCTION_STANDARD_STATUS;
  note?: string;
}

// Cập Nhật Định Mức Sản Xuất
export interface IPatchProductionStandardDto {
  name: string;
  dueDate: string;
  productionPowderQty: number;
  note?: string;
}

export interface IPutProductionStandardResponse {
  message: string;
}

// Xóa Định Mức Sản Xuất
export interface IDeleteProductionStandardResponse {
  message: string;
}

// Quản lý lệnh sản xuất

export type ProductionCommand = {
  id: string;
  name: string;
  startDate: Date;
  endDate: Date;
  orderURL: string;
  status: string;
};
