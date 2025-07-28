import { Color } from 'app/shared/model/enumerations/color.model';
import { LEAVE_REQUEST_DAY_TYPE, LEAVE_REQUEST_STATUS, LEAVE_REQUEST_TYPE } from 'app/shared/model/enumerations/leave-request.model';
import { hourToMinutes } from 'app/shared/util/generate-hours';

const mapLeaveRequestStatus = (status: LEAVE_REQUEST_STATUS) => {
  switch (status) {
    case LEAVE_REQUEST_STATUS.APPROVED:
      return 'Đã duyệt';
    case LEAVE_REQUEST_STATUS.REJECTED:
      return 'Từ chối';
    case LEAVE_REQUEST_STATUS.PENDING:
      return 'Đợi duyệt';
    case LEAVE_REQUEST_STATUS.CANCELLED:
      return 'Đã hủy';
    default:
      return '';
  }
};

const mapLeaveRequestStatusColor = (status: LEAVE_REQUEST_STATUS): Color => {
  switch (status) {
    case LEAVE_REQUEST_STATUS.APPROVED:
      return Color.SUCCESS;
    case LEAVE_REQUEST_STATUS.REJECTED:
      return Color.ERROR;
    case LEAVE_REQUEST_STATUS.PENDING:
      return Color.WARNING;
    default:
      return undefined;
  }
};

const mapLeaveRequestDayType = (dayType: LEAVE_REQUEST_DAY_TYPE) => {
  switch (dayType) {
    case LEAVE_REQUEST_DAY_TYPE.FULL_DAY:
      return 'Cả ngày';
    case LEAVE_REQUEST_DAY_TYPE.HALF_DAY:
      return 'Nửa ngày';
    default:
      return '';
  }
};

const mapDayTypeByHour = (from: string, to: string): string => {
  const fromMins = hourToMinutes(from);
  const toMins = hourToMinutes(to);
  const workingTime = toMins - fromMins;

  if ((fromMins > 720 && toMins > 720) || (fromMins < 720 && toMins < 720) || workingTime <= 480) {
    return 'Nửa ngày';
  } else {
    return 'Cả ngày';
  }
};

const mapDayTypeByHourReverseEnum = (text: string) => {
  if (text === 'Nửa ngày') {
    return LEAVE_REQUEST_DAY_TYPE.HALF_DAY;
  } else {
    return LEAVE_REQUEST_DAY_TYPE.FULL_DAY;
  }
};

const mapLeaveRequestType = (type: LEAVE_REQUEST_TYPE) => {
  switch (type) {
    case LEAVE_REQUEST_TYPE.ANNUAL_LEAVE:
      return 'Nghỉ thường niên';
    case LEAVE_REQUEST_TYPE.MATERNITY_LEAVE:
      return 'Nghỉ thai sản';
    case LEAVE_REQUEST_TYPE.SICK_LEAVE:
      return 'Nghỉ bệnh';
    case LEAVE_REQUEST_TYPE.UNPAID_LEAVE:
      return 'Nghỉ không lương';
    case LEAVE_REQUEST_TYPE.FUNERAL_LEAVE:
      return 'Nghỉ tang chế';
    case LEAVE_REQUEST_TYPE.WEDDING_LEAVE:
      return 'Nghỉ kết hôn';
    case LEAVE_REQUEST_TYPE.COMPENSATION_LEAVE:
      return 'Nghỉ bù';
    default:
      return '';
  }
};

export default {
  mapLeaveRequestDayType,
  mapLeaveRequestStatus,
  mapDayTypeByHour,
  mapDayTypeByHourReverseEnum,
  mapLeaveRequestType,
  mapLeaveRequestStatusColor,
};
