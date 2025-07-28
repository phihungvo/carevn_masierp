import { PRODUCTION_COMMAND_TYPE } from './enumerations/production-command.model';
import { PaginationParams } from './pagination.model';

export interface IInventoryMaterial {
  itemId?: string;
  itemName?: string;
  itemCategoryId?: string;
  itemCategoryCode?: string;
  itemCategoryName?: string;
  itemCode?: string;
  quantity?: number;
  percentProtein?: number;
  uomId?: string;
  uomName?: string;
  expireDate?: string;
  warehouseId?: string;
  warehouseName?: string;
  warehouseTypeId?: string;
  warehouseTypeName?: string;
  productionVolume?: number;
}

export interface IInventoryMaterialParams extends PaginationParams {
  warehouseIds?: string[];
  type?: PRODUCTION_COMMAND_TYPE;
}
