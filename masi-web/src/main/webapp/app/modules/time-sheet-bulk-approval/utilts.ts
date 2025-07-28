import { TIME_SHEET_TYPE } from 'app/shared/model/enumerations/time-keeping.model';
import { WORKSPACE_TYPE } from 'app/shared/model/enumerations/workspace.model';
import { PaginationResponse } from 'app/shared/model/pagination.model';
import { ITimeKeepingMonthly } from 'app/shared/model/time-keeping-monthly.model';
import { generateColumns } from './components/generate-columns';
import { Control, FieldValues } from 'react-hook-form';
import { generateColumnsFactory } from './components/generate-columns-factory';
import { generateColumnsDriver } from './components/generate-columns-driver';
import { generateColumnsOvertime } from './components/generate-columns-overtime';
import { generateColumnsUnloading } from './components/generate-columns-unloading';

export function calculateTotals(
  data: PaginationResponse<ITimeKeepingMonthly>,
  index: number,
  allDaysInMonth: {
    dayOfWeek: string;
    dateNumber: number;
    fullDate: string;
  }[],
) {
  return allDaysInMonth?.map(item => {
    const findDate = data?.data?.[index]?.timeKeepings?.find(record => record?.date === item.fullDate);

    if (findDate) {
      return parseFloat(findDate?.hoursWorked?.toFixed(1));
    }

    return 0;
  });
}

export function calculateTotalHoursByType(
  data: PaginationResponse<ITimeKeepingMonthly>,
  dataTotalHours: number[],
  type: TIME_SHEET_TYPE,
  workspaceType: WORKSPACE_TYPE,
  index: number,
) {
  const commonDataOffice = [
    ...(dataTotalHours ? dataTotalHours : []),
    roundedHours(data?.data?.[index]?.shiftHours, 1),
    data?.data?.[index]?.holiday300,
    data?.data?.[index]?.offDay,
    data?.data?.[index]?.totalWorkFromHome,
    data?.data?.[index]?.annualLeave,
    roundedHours(data?.data?.[index]?.totalHoursAtFactory, 1),
    roundedHours(data?.data?.[index]?.totalWorkAtFactory),
    roundedHours(data?.data?.[index]?.totalWork),
    data?.data?.[index]?.offDayInMonth,
  ];

  const commonDataFactory = [
    ...(dataTotalHours ? dataTotalHours : []),
    roundedHours(data?.data?.[index]?.shiftHours, 1),
    data?.data?.[index]?.holiday300,
    data?.data?.[index]?.offDay,
    data?.data?.[index]?.annualLeave,
    roundedHours(data?.data?.[index]?.totalHoursAtFactory, 1),
    roundedHours(data?.data?.[index]?.totalWorkAtFactory),
    roundedHours(data?.data?.[index]?.totalWork),
    data?.data?.[index]?.offDayInMonth,
  ];

  const commonDataDriver = [
    ...(dataTotalHours ? dataTotalHours : []),
    roundedHours(data?.data?.[index]?.shiftHours, 1),
    data?.data?.[index]?.holiday300,
    data?.data?.[index]?.totalWorkFromHome,
    data?.data?.[index]?.annualLeave,
    roundedHours(data?.data?.[index]?.totalHoursAtFactory, 1),
    roundedHours(data?.data?.[index]?.totalWorkAtFactory),
    roundedHours(data?.data?.[index]?.totalWork),
    data?.data?.[index]?.offDayInMonth,
  ];

  switch (workspaceType) {
    case WORKSPACE_TYPE.OFFICE:
      switch (type) {
        case TIME_SHEET_TYPE.OVERTIME:
        case TIME_SHEET_TYPE.LOADING_UNLOADING:
          return [...(dataTotalHours ? dataTotalHours : []), roundedHours(data?.data?.[index]?.totalHoursAtFactory, 1)];
        case TIME_SHEET_TYPE.DRIVER:
          return commonDataDriver;
        default:
          return commonDataOffice;
      }

    case WORKSPACE_TYPE.FACTORY:
      switch (type) {
        case TIME_SHEET_TYPE.OVERTIME:
        case TIME_SHEET_TYPE.LOADING_UNLOADING:
          return [...(dataTotalHours ? dataTotalHours : []), roundedHours(data?.data?.[index]?.totalHoursAtFactory, 1)];
        case TIME_SHEET_TYPE.DRIVER:
          return commonDataDriver;
        default:
          return commonDataFactory;
      }
  }
}

export function calculateTotalWorkByType(
  data: PaginationResponse<ITimeKeepingMonthly>,
  dataTotalWorks: number[],
  type: TIME_SHEET_TYPE,
  workspaceType: WORKSPACE_TYPE,
  index: number,
) {
  const commonData = [...(dataTotalWorks ? dataTotalWorks : []), roundedHours(data?.data?.[index]?.shiftHours, 1)];

  switch (workspaceType) {
    case WORKSPACE_TYPE.OFFICE:
      switch (type) {
        case TIME_SHEET_TYPE.OVERTIME:
        case TIME_SHEET_TYPE.LOADING_UNLOADING:
          return [...(dataTotalWorks ? dataTotalWorks : []), roundedHours(data?.data?.[index]?.totalWorkAtFactory)];
        default:
          return commonData;
      }

    case WORKSPACE_TYPE.FACTORY:
      switch (type) {
        case TIME_SHEET_TYPE.OVERTIME:
        case TIME_SHEET_TYPE.LOADING_UNLOADING:
          return [...(dataTotalWorks ? dataTotalWorks : []), roundedHours(data?.data?.[index]?.totalWorkAtFactory)];
        default:
          return commonData;
      }
  }
}

export function roundedHours(hours: number, digit: number = 2) {
  return parseFloat(hours?.toFixed(digit));
}

export const renderTimeSheetByType = (
  type: TIME_SHEET_TYPE = TIME_SHEET_TYPE.HOUR,
  workspaceType: WORKSPACE_TYPE = WORKSPACE_TYPE.OFFICE,
  allDaysInMonth: { dayOfWeek: string; dateNumber: number; fullDate: string }[],
  control: Control<FieldValues, any>,
  setSelectedRecord: (id: string) => void,
  toggleDetail: () => void,
) => {
  switch (workspaceType) {
    case WORKSPACE_TYPE.OFFICE:
      switch (type) {
        case TIME_SHEET_TYPE.DRIVER:
          return generateColumnsDriver(allDaysInMonth, control, setSelectedRecord, toggleDetail);
        case TIME_SHEET_TYPE.OVERTIME:
          return generateColumnsOvertime(allDaysInMonth, control, setSelectedRecord, toggleDetail);
        case TIME_SHEET_TYPE.LOADING_UNLOADING:
          return generateColumnsUnloading(allDaysInMonth, control, setSelectedRecord, toggleDetail);
        case TIME_SHEET_TYPE.MIXING_FLOUR:
          return generateColumnsUnloading(allDaysInMonth, control, setSelectedRecord, toggleDetail)
        default:
          return generateColumns(allDaysInMonth, control, setSelectedRecord, toggleDetail);
      }
    case WORKSPACE_TYPE.FACTORY:
      switch (type) {
        case TIME_SHEET_TYPE.DRIVER:
          return generateColumnsDriver(allDaysInMonth, control, setSelectedRecord, toggleDetail);
        case TIME_SHEET_TYPE.OVERTIME:
          return generateColumnsOvertime(allDaysInMonth, control, setSelectedRecord, toggleDetail);
        case TIME_SHEET_TYPE.LOADING_UNLOADING:
          return generateColumnsUnloading(allDaysInMonth, control, setSelectedRecord, toggleDetail);
        case TIME_SHEET_TYPE.MIXING_FLOUR:
          return generateColumnsUnloading(allDaysInMonth, control, setSelectedRecord, toggleDetail)
        default:
          return generateColumnsFactory(allDaysInMonth, control, setSelectedRecord, toggleDetail);
      }
    default:
      return generateColumns(allDaysInMonth, control, setSelectedRecord, toggleDetail);
  }
};
