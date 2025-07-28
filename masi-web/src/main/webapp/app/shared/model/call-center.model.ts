import { ICustomer } from './customer.model';
import { IEmployee } from './employee.model';
import {
  CALL_CENTER_GROUP,
  CALL_CENTER_SOURCE,
  CALL_CENTER_STATUS,
  CALL_CENTER_TYPE,
  CALL_CENTER_TYPE_PAGE,
  ECAll_CENTER_TYPE,
} from './enumerations/call-center';
import { PaginationParams } from './pagination.model';

export interface ICallCenter {
  id: string;
  dateOfRecord?: string;
  description?: string;
  staffRecord?: string;
  type: ECAll_CENTER_TYPE;
  staffFeedback?: string;
  customerFeedback?: string;
  note?: string;
  //   status?: ECAll_CENTER_STATUS;

  content?: string;
  dateSolutionOfRecord?: string;
  solutionOfRecord?: string;
  overdueOfRecord?: number;

  code: string;
  receptionDate: Date;
  sourceCs: CALL_CENTER_SOURCE;
  groupCS: CALL_CENTER_GROUP;
  phoneOfCaller: string;
  phoneOfName: string;
  status: CALL_CENTER_STATUS;
  typeCS: CALL_CENTER_TYPE;
  customerId: string;
  customer: ICustomer;
  employeeCreatedId: string;
  employeeCreated: IEmployee;
  attribute: any;
  typePageCs: CALL_CENTER_TYPE_PAGE;
  problemContent: string;
  resolutionContent: string;
  responseContent: string;
  employeeAssignId: string;
  employeeAssign: IEmployee;
  employeeCloseId: string;
  employeeClose: IEmployee;

  employeeAssignDate?: Date;
  employeeCloseDate?: Date;

  createdByEmployee?: IEmployee;
  createdBy?: string;
  createdAt?: Date;
  updatedAt?: Date;
}

export interface IPostCallCenterDto {
  receptionDate: Date;
  groupCS: CALL_CENTER_GROUP;
  sourceCs: CALL_CENTER_SOURCE;
  phoneOfCaller: string;
  phoneOfName: string;
  status: CALL_CENTER_STATUS;
  typeCS: CALL_CENTER_TYPE;
  customerId: string;
  employeeCreatedId: string;
  attribute?: any;
  typePageCs: CALL_CENTER_TYPE_PAGE;
  problemContent?: string;
  resolutionContent?: string;
  responseContent?: string;
  employeeAssignId?: string;
  employeeCloseId?: string;
}

export interface IPatchCallCenterDto {
  receptionDate: Date;
  groupCS: CALL_CENTER_GROUP;
  sourceCs: CALL_CENTER_SOURCE;
  phoneOfCaller: string;
  phoneOfName: string;
  status: CALL_CENTER_STATUS;
  typeCS: CALL_CENTER_TYPE;
  customerId: string;
  employeeCreatedId: string;
  attribute?: string;
  typePageCs: CALL_CENTER_TYPE_PAGE;
  problemContent?: string;
  resolutionContent?: string;
  responseContent?: string;
  employeeAssignId?: string;
  employeeCloseId?: string;
}

export interface ICallCenterFilterParams extends PaginationParams {
  sort?: string;
  'status.contains'?: string;
  'typePageCs.contains'?: CALL_CENTER_TYPE_PAGE;
  'typeCS.contains'?: string;
  'createdAt.greaterThanOrEqual'?: string;
  'createdAt.lessThanOrEqual'?: string;
}
