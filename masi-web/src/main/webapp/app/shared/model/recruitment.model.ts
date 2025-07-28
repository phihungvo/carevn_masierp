import { IEmployee, IEmployeeProfiles } from './employee.model';
import {
  GENDER,
  INTERVIEW_MODE,
  INTERVIEW_RESULT,
  RECRUITMENT_CONTRACT_TYPE,
  RECRUITMENT_POSITION,
  RECRUITMENT_PROCESS,
  RECRUITMENT_STATUS,
} from './enumerations/recruitment.model';
import { IFIle } from './file.model';
import { PaginationParams } from './pagination.model';
import { IWorkspace } from './workspace.model';

export interface IRecruitment {
  id?: string;
  departmentId: string;
  department?: IWorkspace;
  position: RECRUITMENT_POSITION;
  jobTitle: string;
  wage: number;
  quantity: number;
  level: number;
  startDate?: string;
  recruitmentPurposes?: string;
  employeeId?: string;
  requestNotes?: string;
  description?: string;
  gender?: GENDER;
  approvalSignFile?: string;
  rejectNote?: string;
  status?: RECRUITMENT_STATUS;
  contractType: RECRUITMENT_CONTRACT_TYPE;
  interviewSchedules?: IInterviewSchedule[];
  salaryUnit?: string;
  replaceForId?: string;
  replaceFor?: IEmployeeProfiles;
  listRecruitmentReviews?: IRecruitmentReviewRequest[];
  checkApprove?: boolean;
  deadline?: string;
  numberOfCandidates?: number;
  numberAdjourn?: number;
}

export interface IRecruitmentRenew {
  deadline: string;
  empIds: string[];
  status?: RECRUITMENT_STATUS;
}

export interface IRecruitmentHistory {
  id: string;
  changeDate: string;
  changeBy: string,
  recruitmentRequestId: string
  change: IRecruitmentChange[]
}

export interface IRecruitmentChange {
  newValue: string;
  oldValue: string;
  fieldName: string;
}

export interface IRecruitmentParams extends PaginationParams {
  search?: string;
  status?: RECRUITMENT_STATUS[];
  position?: RECRUITMENT_POSITION;
  interviewResult?: INTERVIEW_RESULT;
  recruitmentProcess?: RECRUITMENT_PROCESS;
}

export interface IRecruitmentApprovalConsent {
  approvalSignFile: string;
}

export interface IRecruitmentReject {
  rejectNote: string;
}

export interface IInterviewSchedule {
  id?: string;
  candidateName: string;
  interviewDate: string;
  cvFile?: string;
  cvFileAttachment?: IFIle;
  interviewerId: string;
  interviewer?: IEmployee;
  rate?: string;
  process?: RECRUITMENT_PROCESS;
  interviewMode?: INTERVIEW_MODE;
  interviewResult?: INTERVIEW_RESULT;
  recruitmentRequest?: IRecruitment;
  email?: string;
  phoneNumber?: string;
}

export interface IInterviewResult {
  interviewResult: INTERVIEW_RESULT;
  rate: string;
}

export interface IInterviewScheduleParams extends PaginationParams {
  search?: string;
  interviewResult?: INTERVIEW_RESULT;
  process?: RECRUITMENT_PROCESS;
  recruitmentId?: string;
}

export interface IRecruitmentReviewRequest {
  id: string;
  position: RECRUITMENT_POSITION;
  employeeId: string;
  employeeName: string;
  requestId: string;
  company: string;
  result: boolean;
  approvalSignFileAttachment: IFIle;
  rejectNote: string;
  updatedAt?: string;
  approvalSignFile?: string;
}
