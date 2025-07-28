import { PaginationParams } from './pagination.model';

export interface IWarehouseType {
  id?: string;
  name: string;
  description?: string;
}

export interface IWarehouseTypeParams extends PaginationParams {}
