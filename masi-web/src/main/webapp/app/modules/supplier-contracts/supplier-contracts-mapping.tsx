import BadgeV2 from 'app/components/badge/badge-v2';
import { SUPPLIER_CONTRACT_STATUS } from 'app/shared/model/supplier-contract.model';
import { ReactNode } from 'react';

export const SupplierContractsStatusBadgeMapping = (
  text: SUPPLIER_CONTRACT_STATUS,
): ReactNode => {
  switch (text) {
    case SUPPLIER_CONTRACT_STATUS.WAITING_APPROVE:
      return <BadgeV2 className="bv2 pr-waiting_approve">Chờ duyệt</BadgeV2>;
    case SUPPLIER_CONTRACT_STATUS.WAITING_LIQUIDATION:
      return (
        <BadgeV2 className="bv2 pr-waiting_approve">Chờ duyệt (TL)</BadgeV2>
      );
    case SUPPLIER_CONTRACT_STATUS.NEW:
      return <BadgeV2 className="bv2 pr-new">Mới</BadgeV2>;
    case SUPPLIER_CONTRACT_STATUS.APPROVED:
      return <BadgeV2 className="bv2 pr-contract-complete">Đã duyệt</BadgeV2>;
    case SUPPLIER_CONTRACT_STATUS.REJECTED:
      return <BadgeV2 className="bv2 pr-rejected">Từ chối</BadgeV2>;
    case SUPPLIER_CONTRACT_STATUS.REJECTED_LIQUIDATION:
      return <BadgeV2 className="bv2 pr-rejected">Từ chối (TL)</BadgeV2>;
    case SUPPLIER_CONTRACT_STATUS.CANCELLED:
      return <BadgeV2 className="bv2 pr-cancelled">Hủy</BadgeV2>;
    case SUPPLIER_CONTRACT_STATUS.LIQUIDATED:
      return <BadgeV2 className="bv2 pr-liquidation">Thanh lý</BadgeV2>;
    case SUPPLIER_CONTRACT_STATUS.EXPIRED:
      return <BadgeV2 className="bv2 pr-expired">Hết hạn</BadgeV2>;
    default:
      return '';
  }
};

export const SupplierContractsDeliveryStatusBadgeMapping = (
  text: string,
): ReactNode => {
  switch (text) {
    case 'NOT_DELIVERED':
      return <BadgeV2 className="bv2 pr-waiting_approve">Chưa giao</BadgeV2>;
    case 'DELIVERING':
      return <BadgeV2 className="bv2 pr-new">Đang giao</BadgeV2>;
    case 'DELIVERED':
      return <BadgeV2 className="bv2 pr-approved">Đã giao</BadgeV2>;
    default:
      return '';
  }
};

export const supplierContractStatusTextMapping = (
  status: SUPPLIER_CONTRACT_STATUS,
): string => {
  switch (status) {
    case SUPPLIER_CONTRACT_STATUS.NEW:
      return 'Mới';
    case SUPPLIER_CONTRACT_STATUS.WAITING_APPROVE:
      return 'Chờ duyệt';
    case SUPPLIER_CONTRACT_STATUS.WAITING_LIQUIDATION:
      return 'Chờ duyệt (TL)';
    case SUPPLIER_CONTRACT_STATUS.APPROVED:
      return 'Đã duyệt';
    case SUPPLIER_CONTRACT_STATUS.REJECTED:
      return 'Từ chối';
    case SUPPLIER_CONTRACT_STATUS.CANCELLED:
      return 'Hủy';
    case SUPPLIER_CONTRACT_STATUS.LIQUIDATED:
      return 'Thanh lý';
    case SUPPLIER_CONTRACT_STATUS.EXPIRED:
      return 'Hết hạn';
    default:
      return '';
  }
};

export const SupplierContractsStatusOptions = [
  { image_icon: 'content/images/vuesax/linear/play.svg', label_target: 'btn-cancel', value: SUPPLIER_CONTRACT_STATUS.NEW, label: 'Mới' },
  // { image_icon: 'content/images/vuesax/linear/printer.svg', label_target: 'btn-cancel', value: SUPPLIER_CONTRACT_STATUS.WAITING_APPROVE, label: 'Chờ duyệt' },
  // { image_icon: 'content/images/vuesax/linear/printer.svg', label_target: 'btn-cancel', value: SUPPLIER_CONTRACT_STATUS.WAITING_LIQUIDATION, label: 'Chờ duyệt (TL)' },
  { image_icon: 'content/images/vuesax/linear/x.svg', label_target: 'btn-reject', value: SUPPLIER_CONTRACT_STATUS.REJECTED, label: 'Từ chối' },
  { image_icon: 'content/images/vuesax/linear/x.svg', label_target: 'btn-reject', value: SUPPLIER_CONTRACT_STATUS.REJECTED_LIQUIDATION, label: 'Từ chối (TL)' },
  { image_icon: 'content/images/vuesax/linear/x-circle.svg', label_target: 'btn-cancel', value: SUPPLIER_CONTRACT_STATUS.CANCELLED, label: 'Hủy' },
  { image_icon: 'content/images/vuesax/linear/check_green.svg', label_target: 'btn-approve', value: SUPPLIER_CONTRACT_STATUS.LIQUIDATED, label: 'Thanh lý' },
  { image_icon: 'content/images/vuesax/linear/check_green.svg', label_target: 'btn-approve', value: SUPPLIER_CONTRACT_STATUS.APPROVED, label: 'Đã duyệt' },
  { image_icon: 'content/images/vuesax/linear/group.svg', label_target: 'btn-cancel', value: SUPPLIER_CONTRACT_STATUS.EXPIRED, label: 'Hết hạn' },
];
