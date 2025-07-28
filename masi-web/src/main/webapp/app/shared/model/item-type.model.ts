import { PaginationParams } from './pagination.model';

export interface IItemType {
  id: string;
  code: string;
  name: string;
  itemType?: string;
}

export interface IItemTypeParams extends PaginationParams {
  searchString?: string;
  itemTypeCategory?: string;
}
