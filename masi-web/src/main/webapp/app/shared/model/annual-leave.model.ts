export enum WORK_PLACE {
  OFFICE = 'OFFICE',
  FACTORY = 'FACTORY',
}

export interface IAnnualLeave {
  id?: string;
  leaveAfterProbation: number;
  leavePerYear: number;
  carryForwardMonth: number;
  employeeId?: string;
  workPlace?: WORK_PLACE;
}

export interface IAnnualLeaveResponse {
  annualLeave_FACTORY: IAnnualLeave;
  annualLeave_OFFICE: IAnnualLeave;
}

export interface IAnnualLeaveEmployee {
  id: string;
  numberDaysOff: number;
  year: number;
  employeeId: string;
  annualLeave: string;
}
