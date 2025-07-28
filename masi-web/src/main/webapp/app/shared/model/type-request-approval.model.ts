import { PAGE_NAME, TYPE_REQUEST_APPROVAL } from "./enumerations/type-request-approval.model";
import { PaginationParams } from "./pagination.model";

export interface ITypeRequestApproval {
  id: string;
  pageName: PAGE_NAME;
  type: TYPE_REQUEST_APPROVAL;
  numberOfReviewers: number;
}

export interface ITypeRequestApprovalParams extends PaginationParams {
  'isDeleted.equals': boolean | false;
  'pageName.equals': string;
}
