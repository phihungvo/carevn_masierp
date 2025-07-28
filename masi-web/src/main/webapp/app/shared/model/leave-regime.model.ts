import { IBodyFile } from 'app/shared/model/file.model';
import { IEmployee } from './employee.model';
import { LEAVE_REGIME_STATUS } from './enumerations/leave-regime.model';
import { LEAVE_REQUEST_DAY_TYPE, LEAVE_REQUEST_TYPE } from './enumerations/leave-request.model';
import { PaginationParams } from './pagination.model';

export interface ILeaveRegime {
  id?: string;
  leaveType: LEAVE_REQUEST_TYPE;
  lastWorkDate: string;
  returnWorkDate: string;
  substituteId?: string;
  substitute?: IEmployee;
  workspaceId?: string;
  approverIds?: string[];
  approvers?: IEmployee[];
  employeeId?: string;
  employee?: IEmployee;
  leaveRequestDayType?: LEAVE_REQUEST_DAY_TYPE;
  status?: LEAVE_REGIME_STATUS;
  leaveRequestId?: string;
  processLeaveRegimeRequests?: ILeaveRegimeRequest[];
  fileId?: string;
  fileName?: string;
  files?: IBodyFile[];
  createdDate?: string;
  fromTime?: string;
  toTime?: string;
  totalDayOff?: number;
}

export interface ILeaveRegimeParams extends PaginationParams {
  leaveType?: LEAVE_REQUEST_TYPE[];
  startDate?: string;
  endDate?: string;
  workspaceId?: string;
  status?: LEAVE_REGIME_STATUS[];
}

export interface ILeaveRegimeRequest {
  id?: string;
  leaveRegimeRequestId?: string;
  status?: LEAVE_REGIME_STATUS;
  employeeId?: string;
  employee?: IEmployee;
  approverId?: string;
  approver?: IEmployee;
  reason?: string;
  fileId?: string;
  fileName?: string;
  updatedAt?: string;
}

export interface IProcessLeaveRegimeRequest {
  approverIds: string[];
}

export interface ILeaveRegimeReview {
  fileId?: string;
  fileName?: string;
  reason?: string;
  status: LEAVE_REGIME_STATUS;
}
