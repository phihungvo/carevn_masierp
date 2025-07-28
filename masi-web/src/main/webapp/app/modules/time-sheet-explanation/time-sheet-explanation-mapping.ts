import { Color } from 'app/shared/model/enumerations/color.model';
import {
  TIME_KEEPING_EXPLANATION_REASON,
  TIME_KEEPING_EXPLANATION_STATUS,
} from 'app/shared/model/enumerations/time-keeping-explanation.model';

const mapTimeKeepingExplanationStatusToText = (type: TIME_KEEPING_EXPLANATION_STATUS): string => {
  switch (type) {
    case TIME_KEEPING_EXPLANATION_STATUS.PENDING:
      return 'Đợi duyệt';
    case TIME_KEEPING_EXPLANATION_STATUS.APPROVED:
      return 'Đã duyệt';
    case TIME_KEEPING_EXPLANATION_STATUS.REJECTED:
      return 'Từ chối';
    case TIME_KEEPING_EXPLANATION_STATUS.CANCELLED:
      return 'Đã huỷ';
    default:
      return '';
  }
};

const mapTimeKeepingExplanationStatusColor = (status: TIME_KEEPING_EXPLANATION_STATUS): Color => {
  switch (status) {
    case TIME_KEEPING_EXPLANATION_STATUS.APPROVED:
      return Color.SUCCESS;
    case TIME_KEEPING_EXPLANATION_STATUS.REJECTED:
      return Color.ERROR;
    case TIME_KEEPING_EXPLANATION_STATUS.PENDING:
      return Color.WARNING;
    default:
      return undefined;
  }
};

const mapTimeKeepingExplanationReasonToText = (type: TIME_KEEPING_EXPLANATION_REASON): string => {
  switch (type) {
    case TIME_KEEPING_EXPLANATION_REASON.ABSENT_WITHOUT_REQUEST:
      return 'Vắng không phép';
    case TIME_KEEPING_EXPLANATION_REASON.INSUFFICIENT_WORKING_TIME:
      return 'Chấm công không đúng giờ quy định';
    case TIME_KEEPING_EXPLANATION_REASON.MISSING_CHECKOUT:
      return 'Quên chấm công';
    case TIME_KEEPING_EXPLANATION_REASON.OVERTIME:
      return 'Làm thêm';
    default:
      return '';
  }
};

export default {
  mapTimeKeepingExplanationReasonToText,
  mapTimeKeepingExplanationStatusColor,
  mapTimeKeepingExplanationStatusToText,
};
