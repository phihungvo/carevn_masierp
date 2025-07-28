import { DEFAULT_MAX_HOUR_WORK } from 'app/constants/common';
import { Color } from 'app/shared/model/enumerations/color.model';
import { WORKSPACE_TYPE } from 'app/shared/model/enumerations/workspace.model';
import { TIME_SHEET_TYPE } from 'app/shared/model/enumerations/time-keeping.model';
import { IConfig } from 'app/shared/model/config.model';

interface MappingInfo {
  label: string | number;
  color: Color | undefined;
}

const timeKeepStatusMapping = (locked?: boolean): MappingInfo => {
  if (locked) {
    return { label: 'Đã khoá', color: Color.ERROR };
  }

  return { label: '', color: undefined };
};

const hoursWorkedMapping = (hoursWorked: number, config: IConfig): MappingInfo => {
  if (hoursWorked > Number(config?.value)) {
    return { label: hoursWorked, color: Color.ERROR };
  } else if (hoursWorked < DEFAULT_MAX_HOUR_WORK) {
    return { label: hoursWorked, color: Color.SUCCESS };
  }

  return { label: hoursWorked, color: undefined };
};

const timeKeepTypeMapping = (text: TIME_SHEET_TYPE): string => {
  switch (text) {
    case TIME_SHEET_TYPE.HOUR:
      return 'Theo giờ';
    case TIME_SHEET_TYPE.OVERTIME:
      return 'Tăng ca';
    case TIME_SHEET_TYPE.LOADING_UNLOADING:
      return 'Bốc xếp';
    case TIME_SHEET_TYPE.MIXING_FLOUR:
      return 'Trộn bột';
    case TIME_SHEET_TYPE.DRIVER:
      return 'Tài xế';
    default:
      return '';
  }
};

const workSpaceTypeMapping = (text: WORKSPACE_TYPE): string => {
  switch (text) {
    case WORKSPACE_TYPE.OFFICE:
      return 'Văn phòng';
    case WORKSPACE_TYPE.FACTORY:
      return 'Nhà máy';
    default:
      return '';
  }
};

export default {
  timeKeepStatusMapping,
  hoursWorkedMapping,
  timeKeepTypeMapping,
  workSpaceTypeMapping,
};
