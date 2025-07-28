import { PaginationParams } from './pagination.model';

export interface IContactType {
  id: string;
  name: string;
}

export interface IContactTypeParams extends PaginationParams {
  'name.contains'?: string;
  'code.contains'?: string;
}
