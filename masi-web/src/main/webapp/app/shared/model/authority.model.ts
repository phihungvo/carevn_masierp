import { PaginationParams } from './pagination.model';

export interface IAuthority {
  id?: string;
  name?: string;
  description?: string;
  resource?: string;
  action?: string;
  listDescription?: IAuthority[];
}

export interface IAuthorityParams extends PaginationParams {
  search?: string;
}
