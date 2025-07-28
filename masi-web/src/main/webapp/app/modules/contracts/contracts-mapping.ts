import { Color } from 'app/shared/model/enumerations/color.model';
import { CONTRACT_STATUS, CONTRACT_TYPE } from 'app/shared/model/enumerations/contract.model';

const contractTypeColorMapping = (text: CONTRACT_TYPE): Color => {
  switch (text) {
    case CONTRACT_TYPE.NEW:
    case CONTRACT_TYPE.ADDITIONAL_CONTRACT_INDEX:
      return Color.PRIMARY;
    case CONTRACT_TYPE.PRICE_UP:
      return Color.ERROR_SECONDARY;
    case CONTRACT_TYPE.PRICE_DOWN:
      return Color.WARNING;
    case CONTRACT_TYPE.EXTEND:
      return Color.SUCCESS;
    case CONTRACT_TYPE.SALES:
      return Color.WARNING;
    default:
      return undefined;
  }
};

const contractTypeTextMapping = (text: CONTRACT_TYPE): string => {
  switch (text) {
    case CONTRACT_TYPE.NEW:
      return 'Mới';
    case CONTRACT_TYPE.EXTEND:
      return 'Gia hạn';
    case CONTRACT_TYPE.PRICE_UP:
      return 'Tăng giá';
    case CONTRACT_TYPE.PRICE_DOWN:
      return 'Giảm giá';
    case CONTRACT_TYPE.ADDITIONAL_CONTRACT_INDEX:
      return ' Bổ sung PLHĐ';
    case CONTRACT_TYPE.SALES:
      return 'Hợp đồng mua bán';
    default:
      return undefined;
  }
};

const contractStatusMapping = (text: CONTRACT_STATUS): Color => {
  switch (text) {
    case CONTRACT_STATUS.DRAFT:
      return Color.PRIMARY;
    case CONTRACT_STATUS.APPROVED:
    case CONTRACT_STATUS.FINISHED:
      return Color.SUCCESS;
    case CONTRACT_STATUS.LIQUIDATED:
      return Color.ERROR_SECONDARY;
    case CONTRACT_STATUS.WAITING_APPROVAL:
    case CONTRACT_STATUS.WAITING_LIQUIDATION:
      return Color.WARNING;
    case CONTRACT_STATUS.DELETED:
      return Color.ERROR;
    case CONTRACT_STATUS.LIQUIDATE_CANCELLED:
      return Color.ERROR;
    case CONTRACT_STATUS.REJECTED:
      return Color.ERROR;
    default:
      return undefined;
  }
};

const contractStatusTextMapping = (text: CONTRACT_STATUS): string => {
  switch (text) {
    case CONTRACT_STATUS.DRAFT:
      return 'Bản thảo';
    case CONTRACT_STATUS.APPROVED:
      return 'Đã duyệt HĐ';
    case CONTRACT_STATUS.FINISHED:
      return 'Hoàn tất';
    case CONTRACT_STATUS.LIQUIDATED:
      return 'Thanh lý';
    case CONTRACT_STATUS.WAITING_APPROVAL:
      return 'Chờ duyệt';
    case CONTRACT_STATUS.WAITING_LIQUIDATION:
      return 'Chờ duyệt thanh lý';
    case CONTRACT_STATUS.DELETED:
      return 'Đã xoá';
    case CONTRACT_STATUS.LIQUIDATE_CANCELLED:
      return 'Từ chối thanh lý';
    case CONTRACT_STATUS.REJECTED:
      return 'Từ chối';
    // case CONTRACT_STATUS.CANCELLED:
    //   return 'Đã hủy';
    default:
      return undefined;
  }
};

export default {
  contractTypeColorMapping,
  contractStatusMapping,
  contractTypeTextMapping,
  contractStatusTextMapping,
};
