import dayjs from 'dayjs';

import { WORKSPACE_TYPE } from 'app/shared/model/enumerations/workspace.model';
import { IJobHistory } from 'app/shared/model/job-history.model';
import { IJob } from 'app/shared/model/job.model';
import { IAccount } from './account.model';
import {
  EMPLOYEE_STATUS,
  PROFILE_ATTACHMENT_TYPE,
  PROFILE_STATES,
} from './enumerations/employee.model';
import {
  GENDER,
  RECRUITMENT_CONTRACT_TYPE,
  RECRUITMENT_POSITION,
} from './enumerations/recruitment.model';
import { PaginationParams } from './pagination.model';
import { IWorkspace } from './workspace.model';

export interface IEmployee {
  id?: number | string;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  hireDate?: dayjs.Dayjs | null;
  salary?: number | null;
  commissionPct?: number | null;
  jobs?: IJob[] | null;
  manager?: IEmployee | null;
  // department?: IDepartment | null;
  jobHistory?: IJobHistory | null;
  name?: string;
  code?: string;
  gender?: string;
  department?: string;
  position?: string;
  status?: string;
  isActive?: boolean;
  companyId?: 'MASI' | 'Kim Long';
  employeeProfile?: IEmployeeProfiles;
  workspace?: IWorkspace;
  employeeCode?: string;
  fullName?: string;
  workspaceName?: string;
  workspaceNormalizedName?: string;
}

export interface IEmployeeProfiles {
  code?: string
  id?: string;
  employeeCode: string;
  lastName?: string;
  firstName?: string;
  fullName: string;
  gender: GENDER;
  workspaceId: string;
  workspace?: IWorkspace;
  citizenId: string;
  citizenIssueDate?: string;
  citizenIssuePlace?: string;
  residenceAddress?: string;
  temporaryAddress?: string;
  birthday?: string;
  phone?: string;
  taxCode: string;
  startWorkDate: string;
  role: string;
  position: RECRUITMENT_POSITION;
  bankCode?: string;
  bankNumber?: string;
  contractType: RECRUITMENT_CONTRACT_TYPE;
  contractTerm: string;
  contractNumber: string;
  contractDate: string;
  contractEndDate?: string;
  level?: string;
  parkingCard?: string;
  insuranceCard?: string;
  referrerId?: string;
  referrer?: IEmployee;
  referrerDate?: string;
  email?: string;
  note?: string;
  status?: EMPLOYEE_STATUS;
  isActive?: boolean;
  probationDateFrom?: string;
  probationDateTo?: string;
  officialWorkTypeDuration?: number;
  insurancePaymentLevel?: number;
  files?: IProfileAttachment[];
  confirmLeave?: IConfirmLeave;
  ignore?: boolean;
  error?: string;
  useDaysOff?: number;
  numberDaysOff?: number;
  account?: IAccount;
  officialWorkTypeDurationYear?: number;
  pin?: string;
  isHasProfileAttachment?: boolean;
}

export interface IFileAttachment {
  id: string;
  name: string;
  path: string;
}

export interface IProfileAttachment {
  id?: string;
  employeeProfileId?: string;
  type: PROFILE_ATTACHMENT_TYPE;
  path: string;
  fileAttachment?: IFileAttachment;
}

export interface IEmployeeSequenceId {
  gender: GENDER;
  nextEmployeeId: string;
}

export interface IConfirmLeave {
  id: string;
  submissionDate: string;
  reason: string;
  recruitmentSolution?: string;
  hrSolution?: string;
  leaveDate: string;
  fileAttachmentIds?: string[];
  fileAttachments?: IFileAttachment[];
}

export interface IEmployeeImport {
  base64: string;
  contentType: string;
}

export interface IEmployeeParams extends PaginationParams {
  search?: string;
  profileStates?: PROFILE_STATES[];
  workspaceIds?: string[];
  workspaceTypes?: WORKSPACE_TYPE;
  employeeStatuses?: EMPLOYEE_STATUS;
  hasAccount?: boolean;
  isFilterCompany?: boolean;
}

export interface IEmployeeSequenceIdParams {
  gender: GENDER;
}

export type HISTORY_KEYS =
  | 'probationDateFrom'
  | 'probationDateTo'
  | 'officialWorkTypeDuration'
  | 'insurancePaymentLevel'
  | 'contractType';

export interface IChangeItem {
  newValue: string;
  oldValue: string;
  fieldName: HISTORY_KEYS;
}

export interface IEmployeeChangeLog {
  id: string;
  changeDate: string;
  change: IChangeItem[];
  changeBy: string;
  employeeId: string;
}

export interface IEmployeeChangeLogParams extends PaginationParams {
  employeeId: string;
}

export interface IListDepartment {
  employeesHCNS: IEmployeeProfiles[];
  employeesSALE: IEmployeeProfiles[];
  employeesWORKER: IEmployeeProfiles[];
  employeesLOGPUR: IEmployeeProfiles[];
}

export const defaultValue: Readonly<IEmployee> = {};
