import { TIME_KEEPING_VIOLATION_TYPE } from 'app/shared/model/enumerations/time-keeping-violation.model';

const mapTimeKeepingViolationTypeToText = (type: TIME_KEEPING_VIOLATION_TYPE): string => {
  switch (type) {
    case TIME_KEEPING_VIOLATION_TYPE.ABSENT_WITHOUT_REQUEST:
      return 'Vắng không phép';
    case TIME_KEEPING_VIOLATION_TYPE.INSUFFICIENT_WORKING_TIME:
      return 'Chấm công không đúng giờ quy định';
    case TIME_KEEPING_VIOLATION_TYPE.MISSING_CHECKOUT:
      return 'Quên chấm công';
    case TIME_KEEPING_VIOLATION_TYPE.OVERTIME:
      return 'Làm thêm';
    case TIME_KEEPING_VIOLATION_TYPE.NO_VIOLATION:
      return 'Không vi phạm';
    default:
      return '';
  }
};

export default {
  mapTimeKeepingViolationTypeToText,
};
