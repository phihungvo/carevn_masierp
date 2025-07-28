import { IEmployee } from './employee.model';
import { TIME_KEEPING_VIOLATION_TYPE } from './enumerations/time-keeping-violation.model';
import { PaginationParams } from './pagination.model';
import { ITimeSheet } from './time-sheet.model';

export interface ITimeKeepingViolation {
  id: string;
  type: TIME_KEEPING_VIOLATION_TYPE;
  isActive: boolean;
  timeKeeping: ITimeSheet;
  employee: IEmployee;
  timeKeepingId: string;
  employeeId: string;
  explanationId?: string
}

export interface ITimeKeepingViolationsParams extends PaginationParams {
  fromDate?: string;
  toDate?: string;
  type?: TIME_KEEPING_VIOLATION_TYPE[];
  employeeIds?: string[];
  workspaceIds?: string[];
  explanationId?: string
  explained?: boolean;
}
