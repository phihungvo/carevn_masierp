import { PaginationParams } from './pagination.model';

export interface IStorage {
  id: string;
  name: string;
}

export interface IStorageParams extends PaginationParams {}
