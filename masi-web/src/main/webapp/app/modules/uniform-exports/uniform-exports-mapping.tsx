import { Color } from 'app/shared/model/enumerations/color.model';
import { ORDER_REVIEW_SOLUTION, ORDER_STATUS } from 'app/shared/model/enumerations/order.model';

const ordersColorMapping = (text: ORDER_STATUS): Color => {
  switch (text) {
    case ORDER_STATUS.NEW:
      return Color.PRIMARY;
    case ORDER_STATUS.NOT_APPROVE:
    case ORDER_STATUS.REJECTED:
    case ORDER_STATUS.DELETED:
      return Color.ERROR;
    case ORDER_STATUS.APPROVED:
      return Color.SUCCESS;
    case ORDER_STATUS.WAITING:
    case ORDER_STATUS.WAITING_APPROVAL:
      return Color.WARNING;
    default:
      return undefined;
  }
};

const ordersTextMapping = (text: ORDER_STATUS): string => {
  switch (text) {
    case ORDER_STATUS.NEW:
      return 'Mới';
    case ORDER_STATUS.NOT_APPROVE:
      return 'Không duyệt';
    case ORDER_STATUS.APPROVED:
      return 'Đã duyệt';
    case ORDER_STATUS.WAITING:
      return 'Chờ';
    case ORDER_STATUS.WAITING_APPROVAL:
      return 'Đợi duyệt';
    case ORDER_STATUS.REJECTED:
      return 'Từ chối';
    case ORDER_STATUS.DELETED:
      return 'Đã xóa';
    case ORDER_STATUS.CANCELLED:
      return 'Đã hủy';
    default:
      return '';
  }
};

const orderReviewSolutionTextMapping = (text: ORDER_REVIEW_SOLUTION): string => {
  switch (text) {
    case ORDER_REVIEW_SOLUTION.REJECT:
      return 'Không duyệt';
    case ORDER_REVIEW_SOLUTION.WAITING:
      return 'Chờ';
    default:
      return '';
  }
};

export default {
  ordersColorMapping,
  ordersTextMapping,
  orderReviewSolutionTextMapping,
};
