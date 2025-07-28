import { TRANSFER_ASSETS_TYPE } from "./enumerations/transfer-assets";
import { PaginationParams } from "./pagination.model";
import { ITransferTypeDto } from "./transfer-type.model";


export interface IItemCategory {
    id?: string;
    code?: string;
    attribute?: any;
    name? : string;
}


export interface IItem {
    id?: string;
    code?: string;
    attribute?: any;
    name? : string;
    uomId? : string;
    uom? : any;
    itemCategoryId? : string;
    itemCategory? : IItemCategory;
    supplierId?: string;
    supplier?: any;
}

export interface IItemInfo {
    id?: string;
    code?: string;
    usageDate?: string;
}
export interface IInventoriesStorage {
    id?: string;
    code?: string;
    attribute?: any;
    quantity?: number;
    supplier?: any,
    itemId?: string;
    notes?: string;
    price?: number;
    item?: IItem;
    itemInfo?: IItemInfo;
}
export interface ITransferAssetTransferDetailsDto {
    id?: string;
    code?: string;
    employeeFromId?: string;
    employeeToId?: string;
    attribute?: any;
    inventoriesStorageId?: string;
    inventoriesStorage?: IInventoriesStorage
}

export interface ITransferAssets {
    id?: string;
    code?: string;
    transferDate?: string;
    transactionType?: ITransferTypeDto;
    transactionTypeId?: string;
    description?: string;
    toPersonId?: string;
    toDepartmentId?: string;
    fromDepartmentId?: string;
    fromPersonId?: string;
    toAddress?: string;
    toPerson?: string;
    attribute?: any;
    createdBy?: string;
    createdAt?: string;
    status?: string;
    assetTransferDetailsDTOS? : ITransferAssetTransferDetailsDto[];
}

export interface ITransferAssetsDto {
    id?: string;
    code?: string;
    transferDate?: string;
    transactionType?: ITransferTypeDto;
    description?: string;
}

export interface IAsset {}

export interface IPostTransferAssetsDto {
    transferAssets?: ITransferAssetsDto;
}

export interface IPatchTransferAssetsDto {
    transferAssets?: ITransferAssetsDto;
}
export interface ITransferAssetsFilter extends PaginationParams {

}

export interface ITransferAssetsFilterParams extends PaginationParams {
    sort?: string;
    'status.contains'?: string;
    'createdAt.greaterThanOrEqual'?: string;
    'createdAt.lessThanOrEqual'?: string;
  }

export interface ITransferAssetsSubDto {
    transfer: string;
    description: string;
    unitOfMeasure: string;
    quantity: number;
    fromDepartment: string;
    toDepartment: string;
    fromNSD: string;
    toNSD: string;
    note: string;
  }


export interface ITransferAssetsParams extends PaginationParams {
    'code.contains'?: string;
    'name.contains'?: string;
    sort?: string[];
    search?: string;
    itemType?: string;
    'itemType.contains'?: string;
    status?: boolean | undefined;
    companyId?: string;
    'createdDate.greaterThanOrEqual'?: string;
    'createdDate.lessThanOrEqual'?: string;
  }