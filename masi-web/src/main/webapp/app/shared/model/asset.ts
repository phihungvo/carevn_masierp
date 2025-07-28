import { PaginationParams } from "./pagination.model";
import { ECAll_CENTER_STATUS, ECAll_CENTER_TYPE } from "./enumerations/call-center";

export interface IAsset {
    id: string;
    codeTS?: string;
    codeWarehouse?: string;
    description?: string;
    group?: string;
    reason?: string;
    rice?: string;
    GT?: string
    SL?: string
    status?: number,
}
export interface IAssetParams extends PaginationParams {
    'name.contains'?: string;
    'code.contains'?: string;
    search?: string;
    status?: boolean | undefined;
    companyId?: string;
  }
  


export interface IAssetFilter extends PaginationParams {

}