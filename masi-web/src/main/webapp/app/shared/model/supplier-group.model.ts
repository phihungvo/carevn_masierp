import { PaginationParams } from './pagination.model';

export interface ISupplierGroup {
  id: string;
  name: string;
}

export interface ISupplierGroupParams extends PaginationParams {
  'name.contains'?: string;
  'code.contains'?: string;
}
