import { PaginationParams } from './pagination.model';
import { IWarehouseType } from './warehouse-type.model';

export interface IWarehouse {
  id?: string;
  code?: string;
  name: string;
  address: string;
  warehouseType?: Partial<IWarehouseType>;
  warehouseTypeId?: string;
  warehouseTypePage?: string;
  createBy?: string;
}

export interface IWarehouseParams extends PaginationParams {
  orderId?: string;
  sort?: string[];
  'warehouseTypePage.contains'?:
    | 'SEMI_FINISHED_PRODUCTS_STORAGE'
    | 'FINISHED_PRODUCTS_STORAGE';
  isNotGetWareHouseUniform?: boolean;
}
