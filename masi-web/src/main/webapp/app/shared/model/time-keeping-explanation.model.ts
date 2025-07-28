import { IEmployee } from './employee.model';
import {
  TIME_KEEPING_EXPLANATION_REASON,
  TIME_KEEPING_EXPLANATION_REVIEW_STATUS,
  TIME_KEEPING_EXPLANATION_STATUS,
} from './enumerations/time-keeping-explanation.model';
import { PaginationParams } from './pagination.model';
import { ITimeKeepingViolation } from './time-keeping-violation.model';

export interface ITimeKeepingExplanation {
  id: string;
  explanation: string;
  status: TIME_KEEPING_EXPLANATION_STATUS;
  reason: TIME_KEEPING_EXPLANATION_REASON;
  isActive: boolean;
  employeeId: string;
  employee: IEmployee;
  violationIds: string[];
  violationDtos: ITimeKeepingViolation[];
  createdAt: string;
  reviewDtos: ITimeKeepingExplanationReviewDto[];
}

export interface IPostTimeKeepingExplanationDto {
  explanation?: string;
  status?: TIME_KEEPING_EXPLANATION_STATUS;
  reason?: string;
  isActive?: boolean;
  employeeId?: string;
  violationIds?: string[];
  reviewerIds?: string[];
  fromDate?: string;
  toDate?: string;
  id?: string;
}

export interface IPatchTimeKeepingExplanationDto {
  explanation: string;
}

export interface ITimeKeepingExplanationReviewDto {
  id?: string;
  reason?: string;
  status: TIME_KEEPING_EXPLANATION_REVIEW_STATUS;
  explanationId?: string;
  isActive?: boolean;
}

export interface ITimeKeepingExplanationParams extends PaginationParams {
  statuses?: TIME_KEEPING_EXPLANATION_REVIEW_STATUS[];
  types?: TIME_KEEPING_EXPLANATION_REASON[];
  searchString?: string;
  startFrom?: string;
  startTo?: string;
  employeeIds?: string[];
  workspaceIds?: string[];
}
