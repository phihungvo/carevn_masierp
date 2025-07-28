import { Color } from 'app/shared/model/enumerations/color.model';
import { TIME_KEEPING_MONTHLY_STATUS } from 'app/shared/model/enumerations/time-keeping-monthly.model';

const mapTimeSheetMonthlyStatusColor = (status: TIME_KEEPING_MONTHLY_STATUS): Color => {
  switch (status) {
    case TIME_KEEPING_MONTHLY_STATUS.PENDING:
      return Color.PRIMARY;
    case TIME_KEEPING_MONTHLY_STATUS.APPROVED:
      return Color.SUCCESS;
    case TIME_KEEPING_MONTHLY_STATUS.REJECTED:
      return Color.ERROR;
    default:
      return undefined;
  }
};

const mapTimeSheetMonthlyStatusText = (status: TIME_KEEPING_MONTHLY_STATUS): string => {
  switch (status) {
    case TIME_KEEPING_MONTHLY_STATUS.PENDING:
      return 'Chờ duyệt';
    case TIME_KEEPING_MONTHLY_STATUS.APPROVED:
      return 'Đã duyệt';
    case TIME_KEEPING_MONTHLY_STATUS.REJECTED:
      return 'Từ chối';
    default:
      return '';
  }
};

export default { mapTimeSheetMonthlyStatusColor, mapTimeSheetMonthlyStatusText };
