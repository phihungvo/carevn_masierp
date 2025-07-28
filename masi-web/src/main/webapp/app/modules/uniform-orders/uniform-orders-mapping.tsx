import { Color } from 'app/shared/model/enumerations/color.model';
import { UNIFORM_ORDER_STATUS } from 'app/shared/model/enumerations/uniform.model';

const uniformOrderStatusMapping = (text: UNIFORM_ORDER_STATUS): string => {
  switch (text) {
    case UNIFORM_ORDER_STATUS.REJECTED:
      return 'Từ chối';
    case UNIFORM_ORDER_STATUS.APPROVED:
      return 'Đã duyệt';
    case UNIFORM_ORDER_STATUS.CANCELLED:
      return 'Đã huỷ';
    case UNIFORM_ORDER_STATUS.WAITING:
      return 'Đợi duyệt';
    case UNIFORM_ORDER_STATUS.STOCKED:
      return 'Đã nhập kho';
    case UNIFORM_ORDER_STATUS.PROCESSING:
      return 'Đang nhập kho';
    default:
      return '';
  }
};

const uniformOrderStatusColorMapping = (text: UNIFORM_ORDER_STATUS): Color => {
  switch (text) {
    case UNIFORM_ORDER_STATUS.REJECTED:
      return Color.ERROR;
    case UNIFORM_ORDER_STATUS.APPROVED:
      return Color.SUCCESS;
    case UNIFORM_ORDER_STATUS.CANCELLED:
      return Color.ERROR;
    case UNIFORM_ORDER_STATUS.WAITING:
      return Color.WARNING;
    case UNIFORM_ORDER_STATUS.PROCESSING:
      return Color.PRIMARY;
    default:
      return undefined;
  }
};

export default { uniformOrderStatusMapping, uniformOrderStatusColorMapping };
