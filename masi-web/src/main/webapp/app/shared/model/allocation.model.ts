import { ALLOCATION_STATUS, ALLOCATION_TYPE } from './enumerations/allocation';
import { PaginationParams } from './pagination.model';
import { IToolDto } from './tool.model';

export interface IAllocationDto {
  id: string;
  depreciationPeriod?: string;
  depreciationDate?: string;
  accountingDate?: string;
  calculator?: string;
  description?: string;
  toolType?: string;
  managementUnit?: string;
  type: ALLOCATION_TYPE;
  status?: ALLOCATION_STATUS;
  tools: IToolDto[];
}

export interface IPostAllocationDto {
  depreciationPeriod?: string;
  depreciationDate?: string;
  accountingDate?: string;
  calculator?: string;
  description?: string;
  toolType?: string;
  managementUnit?: string;
  type: ALLOCATION_TYPE;
  status?: ALLOCATION_STATUS;
  tools: IToolDto[];
}

export interface IPatchAllocationDto {
  depreciationPeriod?: string;
  depreciationDate?: string;
  accountingDate?: string;
  calculator?: string;
  description?: string;
  toolType?: string;
  managementUnit?: string;
  type: ALLOCATION_TYPE;
  status?: ALLOCATION_STATUS;
  tools: IToolDto[];
}
export interface IAllocationFilter extends PaginationParams {}
