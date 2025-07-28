import { PaginationParams } from './pagination.model';

export interface IUom {
  id?: string;
  name: string;
}

export interface IUomParams extends PaginationParams {}

export interface IUomGroup {
  id?: string;
  name: string;
  baseUom?: IUom;
  baseUomId?: string;
  uomGroupDetailsDTOs: IUomGroupDetail[];
}

export interface IUomGroupParams extends PaginationParams {}

export interface IUomGroupDetail {
  id?: string;
  name?: string;
  baseQty?: number;
  altQty?: number;
  active?: boolean;
  baseUom?: IUom;
  baseUomId?: string;
  altUom?: IUom;
  altUomId?: string;
  uomGroup?: IUomGroup;
}
