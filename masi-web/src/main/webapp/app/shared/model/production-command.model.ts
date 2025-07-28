import { ORDER_STATUS } from './enumerations/order.model';
import {
  MANUFACTURE_ORDER_STATUS,
  MANUFACTURE_ORDER_TYPE,
  PRODUCTION_COMMAND_TYPE,
} from './enumerations/production-command.model';
import { PRODUCTION_PROCESS_STATUS } from './enumerations/production-process.model';
import { PRODUCTION_QUALITY_STATUS } from './enumerations/production-quality-control.model';
import { PRODUCTION_STANDARD_STATUS } from './enumerations/production-standard.model';
import { IInventoryMaterial } from './inventory.model';
import { PaginationParams } from './pagination.model';
import { IProductionPackage } from './production-package.model';
import { IProductionProcess } from './production-process.model';

export interface IProductionCommand {
  id: string;
  code: string;
  name: string;
  fromDate: string;
  zonedFromDate: string;
  toDate: string;
  zonedToDate: string;
  status: MANUFACTURE_ORDER_STATUS;
  orderId?: string;
  isActive: boolean;
  createdBy: string;
  createdAt: string;
  workOrders: IProductionProcess[];
  // typeProtein: string;
  listIdContractMaterial?: string[];
  releaseWarehouseDTOS?: IInventoryMaterial[];
  manufactureOrderType?: PRODUCTION_COMMAND_TYPE;
  productionStandardId?: string;
  productionQuantity?: number;
  lastUpdated?: string;
  materialId?: string;
  percentProtein?: string;

  typePage?: MANUFACTURE_ORDER_TYPE;

  itemId?: string;
  orderItemId?: string;
  attributes?: any;

  // Phụ gia ( FULL )
  additives?: {
    id?: string;
    inspectionDate?: Date; // Ngày kiểm tra
    inspectionTime?: Date; // Thời gian kiểm tra
    inspectorId?: string; // Người thực hiện
    status?: string;
    attributes?: any; // Kiểm tra cảm quan
    batchNumber?: string; // Số phiếu cân
    sodiumMaterial?: string; // Khối lượng nguyên liệu
    sodiumMaterialUom?: string;
    sodiumCarbonateBatch?: string; // Số lô Nati cabonat
    sodiumCarbonateWeight?: string; // Khối lương Natri cabonat
    sodiumCarbonateUom?: string;
    sodiumBicarbonateWeight?: string; // Số lô Natri biocanonat
    sodiumBicarbonateBatch?: string; // Khối lượng Natri biocanonat
    sodiumBicarbonateBatchUom?: string;
    bhtWeight?: string; // Khối lượng BHT
    bhtBatch?: string; // Số lô BHT
    bhtUom?: string;
    notes?: string; // Ghi chú
  };
  productionManufacture?: {
    id?: string;
    code?: string;
    name?: string;
    fromDate?: Date;
    toDate?: Date;
    employeeId?: string;
    note?: string;
    attributes: any;
  };
  // Đóng gói ( THIẾU TRẠNG THÁI )
  productPackageDTO?: {
    id?: string;
    packageCode?: string; // Mã đóng gói
    name?: string;
    packageAt?: Date; // Ngày đóng gói
    quantity?: number; // Số bao
    packageBy?: string; // Người đóng gói
    weight?: number; // Khối lượng thành phầm
    note?: string; // Ghi chú
    productPackage?: IProductionPackage;
  };
  // QC ( FULL )
  qualityCheckSampleDTO?: {
    id?: string;
    samplingDate?: Date; // Ngày lấy mẫu
    sampleNo?: string;
    productType?: string;
    sampleWeight?: number; // Số lượng mẫu
    customer?: string; // ID KH
    reason?: string; // Lý do
    sampleReleaseDate?: Date; // Ngày xả mẫu
    internalHum?: string; // Nội bộ
    internalTvn?: string; // Nội bộ
    internalAsh?: string; // Nội bộ
    internalProtein?: string; // Nội bộ
    externalHum?: string; // Đối tác
    externalTvn?: string; // Đối tác
    externalAsh?: string; // Đối tác
    externalProtein?: string; // Đối tác
    samplingEmployeeId: string; // Người lưu mẫu
    note?: string;
    manufactureOrderId?: string; // Lệnh SX
    packageId?: string; // Mã đóng gói
    productPackage?: IProductionPackage;
    proteinPercentageApply?: number; // % đạm áp dụng
    attributes?: any;
    status?: PRODUCTION_QUALITY_STATUS;
    itemId?: string;
  };
  // Lô hàng ( FULL )
  productMaintainDTO?: {
    id?: string;
    productBatchCode?: string;
    productBatchName?: string;
    manufactureDate?: Date;
    expiredDate?: Date;
    productPackageId?: string; // Mã đóng gói
  };
  // Nhập hàng ( FULL )
  productRoutingDTO?: {
    id?: string;
    code?: string;
    name?: string;
    storageId?: string;
    warehouseDate?: Date;
  };
}

export interface IPostProductionCommandDto {
  name: string;
  code: string;
  orderId?: string;
  fromDate?: string;
  toDate?: string;

  // typeProtein: string;
  listIdContractMaterial?: string[];
  releaseWarehouseDTOS?: IInventoryMaterial[];
  manufactureOrderType?: PRODUCTION_COMMAND_TYPE;
  productionStandardId?: string;
  productionQuantity?: number;
  percentProtein?: string;
  materialId?: string;

  itemId?: string;

  attributes?: {
    rawMaterial?: any;
    manufacture?: any;
    note?: string;
  };
  // Phụ gia ( FULL )
  additives?: {
    id?: string;
    inspectionDate: Date; // Ngày kiểm tra
    inspectionTime: Date; // Thời gian kiểm tra
    inspectorId?: string; // Người thực hiện
    status?: string;
    attributes?: any; // Kiểm tra cảm quan
    batchNumber?: string; // Số phiếu cân
    sodiumMaterial?: string; // Khối lượng nguyên liệu
    sodiumMaterialUom?: string;
    sodiumCarbonateBatch?: string; // Số lô Nati cabonat
    sodiumCarbonateWeight?: string; // Khối lương Natri cabonat
    sodiumCarbonateUom?: string;
    sodiumBicarbonateWeight?: string; // Số lô Natri biocanonat
    sodiumBicarbonateBatch?: string; // Khối lượng Natri biocanonat
    sodiumBicarbonateBatchUom?: string;
    bhtWeight?: string; // Khối lượng BHT
    bhtBatch?: string; // Số lô BHT
    bhtUom?: string;
    notes?: string; // Ghi chú
  };
  productionManufacture?: {
    id?: string;
    code?: string;
    name?: string;
    fromDate?: Date;
    toDate?: Date;
    employeeId?: string;
    note?: string;
    attributes: any;
  };
  // Đóng gói ( THIẾU TRẠNG THÁI )
  productionPackaging?: {
    id?: string;
    code?: string; // Mã đóng gói
    name?: string;
    packageAt?: Date; // Ngày đóng gói
    quantityBag?: number; // Số bao
    employeeId?: string; // Người đóng gói
    volume?: number; // Khối lượng thành phầm
    note?: string; // Ghi chú
    productPackage?: IProductionPackage;
  };
  // QC ( FULL )
  productionQuality?: {
    id?: string;
    code?: string; // Mã mẫu
    name?: string;
    qualityCheckSample: {
      samplingDate?: Date; // Ngày lấy mẫu
      sampleNo?: string;
      productType?: string;
      sampleWeight?: number; // Số lượng mẫu
      customer?: string; // ID KH
      reason?: string; // Lý do
      sampleReleaseDate?: Date; // Ngày xả mẫu
      internalHum?: string; // Nội bộ
      internalTvn?: string; // Nội bộ
      internalAsh?: string; // Nội bộ
      internalProtein?: string; // Nội bộ
      externalHum?: string; // Đối tác
      externalTvn?: string; // Đối tác
      externalAsh?: string; // Đối tác
      externalProtein?: string; // Đối tác
      samplingEmployeeId?: string; // Người lưu mẫu
      note?: string;
      manufactureOrderId?: string; // Lệnh SX
      packageId?: string; // Mã đóng gói
      productPackage?: IProductionPackage;
      proteinPercentageApply?: number; // % đạm áp dụng
    };
    attributes?: any;
  };
  // Lô hàng ( FULL )
  productionBatch?: {
    id?: string;
    code?: string;
    name?: string;
    productionDate?: Date;
    expiryDate?: Date;
  };
  // Nhập hàng ( FULL )
  productionSaveInventory?: {
    id?: string;
    code?: string;
    name?: string;
    warehouseId?: string;
    volume?: number;
    date?: Date;
  };
}

export interface IPatchProductionCommandDto {
  name: string;
  code: string;
  orderId?: string;
  fromDate: string;
  toDate: string;
  // typeProtein: string;
  listIdContractMaterial?: string[];
  releaseWarehouseDTOS?: IInventoryMaterial[];
  manufactureOrderType?: PRODUCTION_COMMAND_TYPE;
  productionStandardId?: string;
  productionQuantity?: number;
  materialId?: string;
  percentProtein?: string;

  itemId?: string;

  attributes?: {
    rawMaterial?: any;
    rawMaterial2?: any;
    manufacture?: any;
    note?: string;
    percentProtein?: string;
  };
  // Phụ gia ( FULL )
  additives?: {
    id?: string;
    inspectionDate: Date; // Ngày kiểm tra
    inspectionTime: Date; // Thời gian kiểm tra
    inspectorId?: string; // Người thực hiện
    status?: string;
    attributes?: any; // Kiểm tra cảm quan
    batchNumber?: string; // Số phiếu cân
    sodiumMaterial?: string; // Khối lượng nguyên liệu
    sodiumMaterialUom?: string;
    sodiumCarbonateBatch?: string; // Số lô Nati cabonat
    sodiumCarbonateWeight?: string; // Khối lương Natri cabonat
    sodiumCarbonateUom?: string;
    sodiumBicarbonateWeight?: string; // Số lô Natri biocanonat
    sodiumBicarbonateBatch?: string; // Khối lượng Natri biocanonat
    sodiumBicarbonateBatchUom?: string;
    bhtWeight?: string; // Khối lượng BHT
    bhtBatch?: string; // Số lô BHT
    bhtUom?: string;
    notes?: string; // Ghi chú
  };
  productionManufacture?: {
    id?: string;
    code?: string;
    name?: string;
    fromDate?: Date;
    toDate?: Date;
    employeeId?: string;
    note?: string;
    attributes: any;
  };
  // Đóng gói ( THIẾU TRẠNG THÁI )
  productPackageDTO?: {
    id?: string;
    packageCode?: string; // Mã đóng gói
    name?: string;
    packageAt?: Date; // Ngày đóng gói
    quantity?: number; // Số bao
    packageBy?: string; // Người đóng gói
    weight?: number; // Khối lượng thành phầm
    note?: string; // Ghi chú
    productPackage?: IProductionPackage;
    manufactureOrderId?: string;
  };
  // QC ( FULL )
  qualityCheckSampleDTO?: {
    id?: string;
    samplingDate?: Date; // Ngày lấy mẫu
    sampleNo?: string;
    productType?: string;
    sampleWeight?: number; // Số lượng mẫu
    customer?: string; // ID KH
    reason?: string; // Lý do
    sampleReleaseDate?: Date; // Ngày xả mẫu
    internalHum?: string; // Nội bộ
    internalTvn?: string; // Nội bộ
    internalAsh?: string; // Nội bộ
    internalProtein?: string; // Nội bộ
    externalHum?: string; // Đối tác
    externalTvn?: string; // Đối tác
    externalAsh?: string; // Đối tác
    externalProtein?: string; // Đối tác
    samplingEmployeeId?: string; // Người lưu mẫu
    note?: string;
    manufactureOrderId?: string; // Lệnh SX
    packageId?: string; // Mã đóng gói
    productPackage?: IProductionPackage;
    proteinPercentageApply?: number; // % đạm áp dụng
  };
  isDone?: boolean;
  // Lô hàng ( FULL )
  productMaintainDTO?: {
    id?: string;
    productBatchCode?: string;
    productBatchName?: string;
    manufactureDate?: Date;
    expiredDate?: Date;
    productPackageId?: string;
  };
  // Nhập hàng ( FULL )
  productRoutingDTO?: {
    id?: string;
    code?: string;
    name?: string;
    storageId?: string;
    warehouseDate?: Date;
    quantity?: number;
    productMaintainId?: string;
  };
}

export interface IProductionCommandParams extends PaginationParams {
  fromDate?: string;
  toDate?: string;
  statuses?: MANUFACTURE_ORDER_STATUS[];
  searchString?: string;
  typePage?: string;
  isProductPackageSpecified?: boolean;
  isProductMaintainSpecified?: boolean;
  isProductRoutingSpecified?: boolean;
}

export interface IProductionCommandWithProcessParams extends PaginationParams {
  fromDate: string;
  toDate: string;
  statuses: PRODUCTION_PROCESS_STATUS[];
  searchString?: string;
  manufactureOrderType?: PRODUCTION_COMMAND_TYPE;
}

export interface IProductionOrdersParams extends PaginationParams {
  searchString?: string;
  statuses?: ORDER_STATUS[];
  name?: string;
  startDate?: string;
  endDate?: string;
}

export interface IProductionStandardParams extends PaginationParams {
  searchString?: string;
  statuses?: PRODUCTION_STANDARD_STATUS[];
  name?: string;
  startDate?: string;
  endDate?: string;
}

export interface IPostManufactureOrderByOrderDto {
  code: string;
  name: string;
  productionQuantity: number;
  orderId: string;
  itemId: string;
  orderItemId: string;
  fromDate: string;
  toDate: string;
  note?: string;
  attributes?: { note?: string; percentProtein?: string };
}

export interface IPostManufactureOrderByStandardDto {
  code: string;
  name: string;
  productionQuantity: number;
  productionStandardId: string;
  fromDate: string;
  toDate: string;
  note?: string;
  attributes?: { note?: string };
}
