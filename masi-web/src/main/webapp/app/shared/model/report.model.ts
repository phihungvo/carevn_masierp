import { IEmployeeProfiles } from './employee.model';
import { LEAVE_REQUEST_TYPE } from './enumerations/leave-request.model';
import { UNIFORM_REPORT_TYPE } from './enumerations/uniform.model';
import { PaginationParams } from './pagination.model';
import { EUniformStatus } from 'app/shared/model/enumerations/report-uniforms-expired';

export interface IUniformExpiring {
  employeeCode: string;
  fullName: string;
  type: string;
  employeeId: string;
}
export interface IUniformExpiringParams extends PaginationParams {
  type?: EUniformStatus;
}

export interface IUniformImport {
  id: string;
  name: string;
  total: number;
  type: string;
}

export interface IUniformImportParams extends PaginationParams {
  fromDate?: string;
  toDate?: string;
  type?: string;
}

export interface IUniformSupport {
  id: string;
  name: string;
  total: number;
  type: string;
}

export interface IEmployeeExpiringContract extends IEmployeeProfiles {}

export interface ILeaveRegimeReport {
  leaveType: LEAVE_REQUEST_TYPE;
  count: number;
  leaveTypeDisplay: string;
}

export interface IUniformSupportParams extends PaginationParams {
  fromDate?: string;
  toDate?: string;
}

export interface IUniformChangeReport {
  quantity: number;
  uniformName: string;
  employeeCode: string;
  type: UNIFORM_REPORT_TYPE;
}

export interface IUniformChangeReportParams extends PaginationParams {
  fromDate?: string;
  toDate?: string;
  type?: UNIFORM_REPORT_TYPE;
}

export interface IReportRecruitment {
  id: string;
  position: string;
  totalRequest: number;
  totalRecruited: number;
  totalRecruiting: number;
}

export interface IEmployeeExpiringContractParams extends PaginationParams {}

export interface ILeaveRegimeReportParams extends PaginationParams {
  fromDate?: string;
  toDate?: string;
}

export interface IRecruitmentReportParams extends PaginationParams {
  fromDate?: string;
  toDate?: string;
}

export interface IHrChangeReport {
  totalLeave: number;
  totalJoin: number;
  month: string;
  sum: boolean;
}

export interface IHrChangeReportParams extends PaginationParams {
  fromDate?: string;
  toDate?: string;
}
