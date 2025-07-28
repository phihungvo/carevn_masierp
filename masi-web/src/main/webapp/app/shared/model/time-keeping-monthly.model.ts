import { IEmployee } from './employee.model';
import { ITimeSheet } from './time-sheet.model';
import { PaginationParams } from './pagination.model';
import { TIME_SHEET_TYPE } from './enumerations/time-keeping.model';
import { WORKSPACE_TYPE } from 'app/shared/model/enumerations/workspace.model';
import { TIME_KEEPING_MONTHLY_REVIEW, TIME_KEEPING_MONTHLY_STATUS } from './enumerations/time-keeping-monthly.model';
import { IFIle } from './file.model';

export interface ITimeKeepingMonthly {
  id: string;
  month: string;
  status: TIME_KEEPING_MONTHLY_STATUS;
  createdDate: string;
  lastUpdated: string;
  employee: IEmployee;
  timeKeepings: ITimeSheet[];
  shiftHours: number;
  holiday300: number;
  offDay: number;
  annualLeave: number;
  totalHoursAtFactory: number;
  totalWorkAtFactory: number;
  totalWorkFromHome: number;
  totalWork: number;
  offDayInMonth: number;
  review: ITimeKeepingMonthlyReviewDto;
}

export interface ITimeKeepingMonthlyReviewDto {
  id?: string;
  month: string;
  status: TIME_KEEPING_MONTHLY_REVIEW;
  note?: string;
  signatureFile?: string;
  signature?: IFIle;
}

export interface ITimeKeepingMonthlyApprove {
  note?: string;
  signatureFile?: string;
  ids: string[];
}

export interface ITimeKeepingMonthlyParams extends PaginationParams {
  month?: string;
  type?: TIME_SHEET_TYPE;
  workspaceType: WORKSPACE_TYPE;
}
