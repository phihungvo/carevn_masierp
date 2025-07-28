import { PRODUCTION_MAINTENANCE_STATUS } from './enumerations/production-maintenance.model';
import { PaginationParams } from './pagination.model';
import { IProductionPackage } from './production-package.model';

export interface IProductionMaintain {
  id?: string;
  productBatchCode: string;
  productBatchName: string;
  manufactureDate: string;
  expiredDate: string;
  isDeleted?: boolean;
  createdAt?: string;
  lastUpdatedAt?: string;
  productPackageId?: string;
  productPackageDTO?: IProductionPackage;
  status?: PRODUCTION_MAINTENANCE_STATUS;
  note?: string;
}

export interface IProductionMaintainParams extends PaginationParams {
  search?: string;
  manufactureStartDate?: string;
  manufactureEndDate?: string;
  expiredStartDate?: string;
  expiredEndDate?: string;
  statuses?: PRODUCTION_MAINTENANCE_STATUS[];
  isProductRoutingSpecified?: boolean;
}
