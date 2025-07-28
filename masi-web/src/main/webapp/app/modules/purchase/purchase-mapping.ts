import { Color } from 'app/shared/model/enumerations/color.model';
import { PURCHASE_STATUS, PURCHASE_UNIT } from 'app/shared/model/enumerations/purchase.model';

const purchaseColorMapping = (text: PURCHASE_STATUS): Color => {
  switch (text) {
    case PURCHASE_STATUS.NEW:
      return Color.PRIMARY;
    case PURCHASE_STATUS.WAITING_APPROVAL:
    case PURCHASE_STATUS.DELIVERING:
      return Color.WARNING;
    case PURCHASE_STATUS.APPROVED:
    case PURCHASE_STATUS.FINISHED:
      return Color.SUCCESS;
    case PURCHASE_STATUS.REJECTED:
      return Color.ERROR;
    default:
      return undefined;
  }
};

const purchaseStatusTextMapping = (text: PURCHASE_STATUS): string => {
  switch (text) {
    case PURCHASE_STATUS.NEW:
      return 'Mới';
    case PURCHASE_STATUS.WAITING_APPROVAL:
      return 'Đợi duyệt';
    case PURCHASE_STATUS.APPROVED:
      return 'Đã duyệt';
    case PURCHASE_STATUS.REJECTED:
      return 'Từ chối';
    case PURCHASE_STATUS.FINISHED:
      return 'Hoàn tất';
    case PURCHASE_STATUS.DELIVERING:
      return 'Đang giao';
    default:
      return '';
  }
};

const purchaseUnitTextMapping = (text: PURCHASE_UNIT): string => {
  switch (text) {
    case PURCHASE_UNIT.KG:
      return 'Kg';
    case PURCHASE_UNIT.TON:
      return 'Tấn';
    case PURCHASE_UNIT.PIECE:
      return 'Cái';
    default:
      return '';
  }
};

export default {
  purchaseColorMapping,
  purchaseStatusTextMapping,
  purchaseUnitTextMapping,
};
