import { Color } from 'app/shared/model/enumerations/color.model';
import { QUOTATION_STATUS } from 'app/shared/model/enumerations/quotation.model';

const priceListMappingColors = (text: QUOTATION_STATUS): Color => {
  switch (text) {
    case QUOTATION_STATUS.NEW:
      return Color.PRIMARY;
    case QUOTATION_STATUS.WAITING_APPROVAL:
    case QUOTATION_STATUS.NEED_UPDATE:
      return Color.WARNING;
    case QUOTATION_STATUS.REJECTED:
      return Color.ERROR;
    case QUOTATION_STATUS.APPROVED:
    case QUOTATION_STATUS.CUSTOMER_APPROVED:
    case QUOTATION_STATUS.SENT:
      return Color.SUCCESS;
    default:
      return undefined;
  }
};

const priceListMappingText = (text: QUOTATION_STATUS): string => {
  switch (text) {
    case QUOTATION_STATUS.NEW:
      return 'Mới';
    case QUOTATION_STATUS.SENT:
      return 'Đã gửi KH';
    case QUOTATION_STATUS.WAITING_APPROVAL:
      return 'Đợi duyệt';
    case QUOTATION_STATUS.NEED_UPDATE:
      return 'Cần cập nhật';
    case QUOTATION_STATUS.REJECTED:
      return 'KH Từ chối';
    case QUOTATION_STATUS.APPROVED:
      return 'Đã duyệt';
    case QUOTATION_STATUS.CANCELLED:
      return 'Hủy';
    case QUOTATION_STATUS.CUSTOMER_APPROVED:
      return 'KH đã duyệt';
    default:
      return '';
  }
};

export default {
  priceListMappingColors,
  priceListMappingText,
};
