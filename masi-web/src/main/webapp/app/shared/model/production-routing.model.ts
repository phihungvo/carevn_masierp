import { PaginationParams } from './pagination.model';
import { IProductionCommand } from './production-command.model';
import { IProductionMaintain } from './production-maintain.model';
import { IStorage } from './storage.model';
import { IWarehouse } from './warehouse.model';

export interface IProductionRouting {
  id?: string;
  name: string;
  warehouseDate: Date;
  storageId: string;
  storage?: IStorage;
  productMaintainId?: string;
  productMaintainDTO?: IProductionMaintain;
  manufactureOrder?: IProductionCommand;

  warehouseDTO?: IWarehouse;

  // isActive?: boolean;
  // unit: string;
  quantity: number;
  // factoryId: string;
  // factory?: IFactory;
}

export interface IProductionRoutingParams extends PaginationParams {
  search?: string;
  factoryId?: string;
  storageId?: string;
}
