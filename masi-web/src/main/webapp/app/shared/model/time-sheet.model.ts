import { IEmployee } from './employee.model';
import { TIME_SHEET_TYPE } from './enumerations/time-keeping.model';
import { WORKSPACE_TYPE } from 'app/shared/model/enumerations/workspace.model';

// Bảng chấm công
export interface ITimeSheet {
  id: string;
  employee: IEmployee;
  hoursWorked: number;
  date: string;
  zonedDate: string;
  note?: string;
  locked: boolean;
  firstCheckIn?: string;
  lastCheckIn?: string;
  isDayOff?: boolean;
  character?: string;
  totalCompletionPercent?: number;
}

// Cập Nhật Chấm Công
export interface IPatchTimeKeepingDto {
  id?: string;
  hoursWorked?: number;
  date?: string;
  note?: string;
  isDayOff?: boolean;
  character?: string;
  type?: TIME_SHEET_TYPE;
  totalCompletionPercent?: number;
}

// Tạo Bản Ghi Chấm Công
export interface IPostTimeKeepingDto {
  employeeId?: string;
  hoursWorked: number;
  date: string;
  isOverride?: boolean;
  isDayOff?: boolean;
  character?: string;
  type?: TIME_SHEET_TYPE;
}

// Bảng chấm công chi tiết
export interface ITimeSheetDetail {
  uuid: string;
  employee: string;
  'check-in': string;
  date: string;
}

// Tạo Bản Ghi Chấm Công Chi Tiết
export interface IPostTimeKeepingRecordDto {
  employee: string;
  'check-in': string;
  note?: string;
  type?: TIME_SHEET_TYPE;
  workSpaceTypes?: WORKSPACE_TYPE;
}

// Cập Nhật Bản Ghi Chấm Công Chi Tiết
export interface IPutTimeKeepingRecordDto {
  // 'check-in': string;
  // date: string;
  hoursWorked: number;
  date: string;
}

// Xuất Chấm Công Theo Nhân Viên Và Ngày
export interface IExportTimeSheet {
  message: string;
  file: string;
}

export interface ITimeSheetGroupByEmployee {
  id: string;
  firstName: string;
  lastName: string;
  timeKeepings: ITimeSheet[];
}

export type ITimeSheetGroupByEmployeeByObject = {
  [employeeId: string]: Omit<ITimeSheetGroupByEmployee, 'timeKeepings'> & { times: { [isoTime: string]: ITimeSheet } };
};

export type TimeSheetGroupByEmployeeConveted = ITimeSheetGroupByEmployee & { times: Record<string, ITimeSheet> };
