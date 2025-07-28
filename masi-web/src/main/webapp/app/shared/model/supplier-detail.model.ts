import { IItem } from './item.model';
import { PaginationParams } from './pagination.model';

export interface ISuppliesDetails {
  id?: string
  supplierId?: string
  itemId?: string
  item?: IItem,
  basePrice?: number
  notes?: string
  createAt?: string
  createBy?: string
  updateAt?: string
  updateBy?: string
  company?: string

}

export interface ISupplierDetailParams extends PaginationParams {
  'name.contains'?: string;
  'code.contains'?: string;
}
