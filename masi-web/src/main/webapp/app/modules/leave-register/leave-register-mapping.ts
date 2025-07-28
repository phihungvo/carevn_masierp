import { Color } from 'app/shared/model/enumerations/color.model';
import { LEAVE_REGIME_STATUS } from 'app/shared/model/enumerations/leave-regime.model';
import { LEAVE_REQUEST_DAY_TYPE } from 'app/shared/model/enumerations/leave-request.model';

export const leaveRegimeStatusTextMapping = (text: LEAVE_REGIME_STATUS): string => {
  switch (text) {
    case LEAVE_REGIME_STATUS.NEW:
      return 'Mới';
    case LEAVE_REGIME_STATUS.WAITING_APPROVAL:
      return 'Đợi duyệt';
    case LEAVE_REGIME_STATUS.APPROVED:
      return 'Đã duyệt';
    case LEAVE_REGIME_STATUS.REJECTED:
      return 'Từ chối';
    case LEAVE_REGIME_STATUS.CANCEL:
      return 'Hủy';
    default:
      return '';
  }
};

export const leaveRegimeStatusColorMapping = (text: LEAVE_REGIME_STATUS): Color => {
  switch (text) {
    case LEAVE_REGIME_STATUS.NEW:
      return Color.PRIMARY;
    case LEAVE_REGIME_STATUS.WAITING_APPROVAL:
      return Color.WARNING;
    case LEAVE_REGIME_STATUS.APPROVED:
      return Color.SUCCESS;
    case LEAVE_REGIME_STATUS.REJECTED:
      return Color.ERROR;
    default:
      return undefined;
  }
};

export const leaveRegimeTextMapping = (text: LEAVE_REQUEST_DAY_TYPE): string => {
  switch (text) {
    case LEAVE_REQUEST_DAY_TYPE.FULL_DAY:
      return 'Cả ngày';
    case LEAVE_REQUEST_DAY_TYPE.HALF_DAY:
      return 'Nửa ngày';
    default:
      return undefined;
  }
};


export default {
  leaveRegimeStatusTextMapping,
  leaveRegimeStatusColorMapping,
  leaveRegimeTextMapping
};
