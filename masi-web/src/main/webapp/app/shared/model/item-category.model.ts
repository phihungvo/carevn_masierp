import { ItemCategoryCode } from './enumerations/item-category.model';
import { PaginationParams } from './pagination.model';

export interface IItemCategory {
  id: string;
  code: ItemCategoryCode;
  name: string;
}

export interface IItemCategoryParams extends PaginationParams {
  searchString?: string;
  itemTypeCategory?: string;
}
