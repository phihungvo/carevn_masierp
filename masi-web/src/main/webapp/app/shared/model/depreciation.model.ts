import { DEPRECIATION_STATUS } from "./enumerations/depreciation";
import { PaginationParams } from "./pagination.model";

export interface IDepreciationDto {
    id?: string,
    depreciationPeriod?: string; // Kỳ khấu hao
    depreciationDate?: string | null; // Ngày tính khấu hao
    accountingDate?: string | null; // Ngày hoạch toán
    calculatedBy?: string | null; // Người tính
    description?: string | null; // Diễn giải
    approvalListDate?: string | null; // Danh sách duyệt
    status?: DEPRECIATION_STATUS // Trạng thái
    createdBy?: string; // Người tạo
    createdDate?: string | null; // Ngày tạo
    assetDepreciationList?: Array<{
      assetCode?: string;
      assetName?: string;
      depreciationAmount?: number;
    }> | null; // Danh sách khấu hao tài sản
  }
  

export interface IAsset {}

export interface IPostDepreciationDto {
    depreciation?: IDepreciationDto;
}

export interface IPatchDepreciationDto {
    depreciation?: IDepreciationDto;
}
export interface IDepreciationFilter extends PaginationParams {

}

export interface IDepreciationSubDto {
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