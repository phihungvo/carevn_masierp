import { WORK_CENTER_STATUS } from './enumerations/work-center.model';
import { PaginationParams } from './pagination.model';

export interface IWorkCenter {
  id: string;
  code: string;
  name: string;
  status: WORK_CENTER_STATUS;
  companyId: string;
  lastCheckedAt: string;
  note?: string;
}

export interface IPostWorkCenterDto {
  code: string;
  name: string;
  status: WORK_CENTER_STATUS;
  lastCheckedAt: string;
  note?: string;
}

export interface IWorkCenterParams extends PaginationParams {
  search?: string;
  companyId?: string;
  status?: WORK_CENTER_STATUS[];
}
