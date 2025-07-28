import { IEmployee } from './employee.model';
import { PRODUCTION_PACKAGES_STATUS } from './enumerations/production-packages.model';
import { PaginationParams } from './pagination.model';
import { IProductionCommand } from './production-command.model';
import { IProductionProcess } from './production-process.model';
import { IUom } from './uom.model';
export interface IProductionPackage {
  id?: string;
  packageCode: string;
  packageName?: string;
  quantity: number;
  unit?: string;
  isDeleted?: string;
  createdAt?: string;
  lastUpdatedAt?: string;
  workOrder?: IProductionProcess;
  workOrderId?: string;
  manufactureOrderId?: string;
  manufactureOrder?: IProductionCommand;
  productionQuantity?: number;
  uomDTO?: IUom;
  status?: PRODUCTION_PACKAGES_STATUS;
  packageByEmployee?: IEmployee;
  packageBy?: string;
  packageAt?: string;
  note?: string;
}
export interface IProductionPackageParams extends PaginationParams {
  manufactureOrderIds?: string[];
  search?: string;
  moIds?: string[];
  statuses?: string[];
  isHasQC?: boolean;
  isExistItem?: boolean;
  isProductMaintainSpecified?: boolean;
}
