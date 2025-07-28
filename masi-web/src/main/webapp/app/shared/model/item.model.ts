import { IItemCategory } from './item-category.model';
import { IItemType } from './item-type.model';
import { PaginationParams } from './pagination.model';
import { ISupplier } from './supplier.model';
import { IUom } from './uom.model';

export interface IItem {
  id?: string;
  code: string;
  name: string;
  uomId: string;
  percentProtein: number;
  itemCategoryId: string;
  itemCategory?: IItemCategory;
  uom?: IUom;
  note?: string;
  notes?: string;
  attribute?: { [key: string]: string };
  vatRate?: number;
  unitPrice?: number;
  supplierId?: string;
  supplier?: ISupplier;
  isActive?: boolean;
  itemType?: string;
  itemTypes?: IItemType;
  itemTypeId?: string;
  company?: string;
  item?: {
    code: string;
  };
}
export interface IItemParams extends PaginationParams {
  'name.contains'?: string;
  sort?: string[];
  search?: string;
  itemType?: string;
  'itemType.contains'?: string;
  status?: boolean | undefined;
  companyId?: string;
  'createdDate.greaterThanOrEqual'?: string;
  'createdDate.lessThanOrEqual'?: string;
}
