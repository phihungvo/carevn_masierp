import { PaginationParams } from './pagination.model';

export interface ICurrency {
  id: string;
  code: string;
  name: string;
  symbol: string;
  isActive: true;
  rate: number;
  attributes: string;
}

export interface ICurrencyParams extends PaginationParams {
  'name.contains'?: string;
  'code.contains'?: string;
  search?: string;
}
