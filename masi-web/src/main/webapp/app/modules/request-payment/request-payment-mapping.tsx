import BadgeV2 from 'app/components/badge/badge-v2';
import { Color } from 'app/shared/model/enumerations/color.model';
import {
  PAYMENT_REQUEST_STATUS,
  PAYMENT_REQUEST_TYPE,
} from 'app/shared/model/enumerations/payment-request';
import { ReactNode } from 'react';

export const RequestPaymentStatusTextMapping = (
  text: PAYMENT_REQUEST_STATUS,
): string => {
  switch (text) {
    case PAYMENT_REQUEST_STATUS.WAITING_APPROVE:
      return 'Đợi duyệt';
    case PAYMENT_REQUEST_STATUS.APPROVED:
      return 'Đã duyệt';
    case PAYMENT_REQUEST_STATUS.REJECTED:
      return 'Từ chối';
    case PAYMENT_REQUEST_STATUS.COMPLETED:
      return 'Hoàn thành';
    case PAYMENT_REQUEST_STATUS.NEW:
      return 'Mới';
    case PAYMENT_REQUEST_STATUS.CANCELLED:
      return 'Hủy';
    default:
      return '';
  }
};

export const RequestPaymentStatusColorMapping = (
  text: PAYMENT_REQUEST_STATUS,
): Color => {
  switch (text) {
    case PAYMENT_REQUEST_STATUS.WAITING_APPROVE:
      return Color.WARNING;
    case PAYMENT_REQUEST_STATUS.APPROVED:
      return Color.SUCCESS;
    case PAYMENT_REQUEST_STATUS.COMPLETED:
      return Color.SUCCESS;
    case PAYMENT_REQUEST_STATUS.REJECTED:
      return Color.ERROR;
    case PAYMENT_REQUEST_STATUS.NEW:
      return Color.PRIMARY;
    default:
      return undefined;
  }
};

export const voucherPaymentRequestTextTypeMapping = (
  text: PAYMENT_REQUEST_TYPE,
): string => {
  switch (text) {
    case PAYMENT_REQUEST_TYPE.ADVANCEMENT:
      return 'Đề nghị tạm ứng';
    case PAYMENT_REQUEST_TYPE.PAYMENT:
      return 'Đề nghị thanh toán';
    case PAYMENT_REQUEST_TYPE.REIMBURSEMENT:
      return 'Đề nghị hoàn ứng';
    default:
      return undefined;
  }
};

export const RequestPaymentStatusBadgeMapping = (
  text: PAYMENT_REQUEST_STATUS,
): ReactNode => {
  switch (text) {
    case PAYMENT_REQUEST_STATUS.WAITING_APPROVE:
      return <BadgeV2 className="bv2 pr-waiting_approve">Đợi duyệt</BadgeV2>;
    case PAYMENT_REQUEST_STATUS.WAITING_APPROVED:
      return <BadgeV2 className="bv2 pr-waiting_approve">Đợi duyệt</BadgeV2>;
    case PAYMENT_REQUEST_STATUS.APPROVED:
      return <BadgeV2 className="bv2 pr-approved">Đã duyệt</BadgeV2>;
    case PAYMENT_REQUEST_STATUS.REJECTED:
      return <BadgeV2 className="bv2 pr-rejected">Từ chối</BadgeV2>;
    case PAYMENT_REQUEST_STATUS.COMPLETED:
      return <BadgeV2 className="bv2 pr-completed">Hoàn tất</BadgeV2>;
    case PAYMENT_REQUEST_STATUS.NEW:
      return <BadgeV2 className="bv2 pr-new">Mới</BadgeV2>;
    case PAYMENT_REQUEST_STATUS.CANCELLED:
      return <BadgeV2 className="bv2 pr-cancelled">Hủy</BadgeV2>;
    default:
      return '';
  }
};

export const RequestPaymentStatusOptions = [
  { value: PAYMENT_REQUEST_STATUS.WAITING_APPROVE, label: 'Đợi duyệt' },
  { value: PAYMENT_REQUEST_STATUS.APPROVED, label: 'Đã duyệt' },
  { value: PAYMENT_REQUEST_STATUS.REJECTED, label: 'Từ chối' },
  { value: PAYMENT_REQUEST_STATUS.NEW, label: 'Mới' },
  { value: PAYMENT_REQUEST_STATUS.CANCELLED, label: 'Hủy' },
];

export const RequestPaymentTypeOptions = [
  { value: PAYMENT_REQUEST_TYPE.PAYMENT, label: 'DNTT' },
  { value: PAYMENT_REQUEST_TYPE.ADVANCEMENT, label: 'DHTU' },
  { value: PAYMENT_REQUEST_TYPE.REIMBURSEMENT, label: 'DNHU' },
];
