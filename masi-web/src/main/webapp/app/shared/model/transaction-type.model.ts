import { PaginationParams } from "./pagination.model";
import { ITransferTypeDto } from "./transfer-type.model";


export interface ITransactionType {
    id?: string;
    code?: string;
    transferDate?: Date;
    transactionType?: ITransferTypeDto;
    transactionTypeId?: string;
    description?: string;
}


export interface ITransactionTypeParams extends PaginationParams {
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