import { LIQUIDATION_STATUS } from './enumerations/liquidation';
import { ILiquidationPropertyDto } from './liquidation-property.model';
import { ILiquidationStaffDto } from './liquidation-staff.model';
import { PaginationParams } from './pagination.model';
export interface ILiquidationDto {
  id: string;
  referenceNumber?: string;
  liquidationDate?: string;
  liquidationReason?: string;
  creator?: string;
  creationDate?: string;
  description?: string;
  status?: LIQUIDATION_STATUS;
  staffs?: ILiquidationStaffDto[];
  properties?: ILiquidationPropertyDto[];
}

export interface IPostLiquidationDto {
  referenceNumber?: string;
  liquidationDate?: string;
  liquidationReason?: string;
  creator?: string;
  creationDate?: string;
  description?: string;
  status?: LIQUIDATION_STATUS;
  staffs?: ILiquidationStaffDto[];
  properties?: ILiquidationPropertyDto[];}

export interface IPatchLiquidationDto {
  referenceNumber?: string;
  liquidationDate?: string;
  liquidationReason?: string;
  creator?: string;
  creationDate?: string;
  description?: string;
  status?: LIQUIDATION_STATUS;
  staffs?: ILiquidationStaffDto[];
  properties?: ILiquidationPropertyDto[];}

  export interface ILiquidationFilter extends PaginationParams {}
