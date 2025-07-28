import { IEmployee } from './employee.model';
import { PaginationParams } from './pagination.model';
import { WORKSPACE_TYPE } from 'app/shared/model/enumerations/workspace.model';
import { LEAVE_REQUEST_DAY_TYPE, LEAVE_REQUEST_STATUS, LEAVE_REQUEST_TYPE } from './enumerations/leave-request.model';
import { IBodyFile } from 'app/shared/model/file.model';

// Review
export interface Review {
  id: string;
  status: string;
  isActive: boolean;
  createdAt: Date;
  lastUpdated: Date;
  reviewerId: string;
  leaveRequestId: string;
  reviewer: IEmployee;
  reason?: string;
}

// Bảng đơn nghỉ phép
export type ILeaveRequest = {
  id: string;
  employee: IEmployee;
  employeeId: string;
  sensor: IEmployee;
  sensorId: string;
  reviews: Review[];
  substitute: IEmployee;
  substituteId: string;
  status: string;
  fromDate: string;
  toDate: string;
  fromTime?: string;
  toTime?: string;
  reason?: string;
  leaveRequestType: string;
  leaveRequestDayType: string;
  fileId?: string;
  files?: IBodyFile[];
};

// Bảng người duyệt đơn nghỉ phép
export interface ILeaveRequestApprover {
  uuid: string;
  leaveRequestId: string;
  sensor: string;
}

// Tạo Đơn Nghỉ Phép
export interface IPostLeaveRequestDto {
  id?: string;
  status: LEAVE_REQUEST_STATUS;
  employeeId: string;
  substituteId: string;
  fromDate: string;
  toDate: string;
  fromTime?: string;
  toTime?: string;
  reason: string;
  leaveRequestType: LEAVE_REQUEST_TYPE;
  leaveRequestDayType: LEAVE_REQUEST_DAY_TYPE;
  reviewerIds: string[];
  fileId?: string;
  files?: IBodyFile[];
  totalDayOff?: number;
}

export interface IPostLeaveRequestResponse {
  uuid: string;
  message: string;
}

// Cập Nhật Đơn Nghỉ Phép
export interface IPutLeaveRequestDto {
  status: string;
  reason: string;
}

export interface IPutLeaveRequestResponse {
  message: string;
}

// Cập Nhật Đơn Nghỉ Phép
export interface IPatchLeaveRequestDto {
  status: string;
  sensor?: string;
  reason?: string;
}

// Xét Duyệt Đơn Nghỉ Phép
export interface IPatchLeaveRequestReviewDto {
  reviewId: string;
  status: LEAVE_REQUEST_STATUS;
  reason?: string;
}

export interface IPatchLeaveRequestResponse {
  message: string;
  uuid: string;
}

export interface ILeaveRequestParams extends PaginationParams {
  type?: LEAVE_REQUEST_TYPE[];
  status?: LEAVE_REQUEST_STATUS[];
  workspaceType?: WORKSPACE_TYPE;
  sort?: string[]
  fromDate?: string;
  totalDay?: number;
  employeeId?: string;
  typeLeave?: LEAVE_REQUEST_DAY_TYPE;
  workspaceIds?: string[];
  employeeIds?: string[];
}
