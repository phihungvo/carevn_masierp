import { PaginationParams } from './pagination.model';

export interface ICompany {
  id: string;
  normalizedName: string;
  name: string;

  description?: string;
  parentId?: string;
  code?: string;
  taxCode?: string;
  website?: string;
  callcenter?: string;
  address?: string;
  representativeName?: string;
  representativePhone?: string;
  representativeEmail?: string;
  representativeDob?: Date;
  representativeIdNumber?: string;
  imageId?: string;

  isDeleted?: boolean;
  isActivated?: boolean;
}

export interface ICompanyParams extends PaginationParams {
  search?: string;
  'code.contains'?: string;
}

export interface IPostCompanyDto {
  name?: string;
  description?: string;
  parentId?: string;
  normalizedName?: string;
  code?: string;
  taxCode?: string;
  website?: string;
  callcenter?: string;
  address?: string;
  representativeName?: string;
  representativePhone?: string;
  representativeEmail?: string;
  representativeDob?: Date;
  representativeIdNumber?: string;
  imageId?: string;
}

export interface IPatchCompanyDto {
  id?: string;
  name?: string;
  description?: string;
  parentId?: string;
  normalizedName?: string;
  code?: string;
  taxCode?: string;
  website?: string;
  callcenter?: string;
  address?: string;
  representativeName?: string;
  representativePhone?: string;
  representativeEmail?: string;
  representativeDob?: Date;
  representativeIdNumber?: string;
  imageId?: string;
}
