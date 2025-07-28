import { PaginationParams } from './pagination.model';

export interface IFactory {
  id: string;
  name: string;
}

export interface IFactoryParams extends PaginationParams {}
