import { PaginationParams } from './pagination.model';

export interface IVatRate {
  id: string;
  code: string;
  name: string;
  value: number;
  company: string;
  createdBy: string;
  createdAt: Date;
}

export interface IVatRateParams extends PaginationParams {
  'name.contains'?: string;
  'code.contains'?: string;
}
