import Badge from 'app/components/badge/badge';
import BadgeV2 from 'app/components/badge/badge-v2';
import { InvoiceType } from 'app/shared/model/enumerations/invoice';
import { SuppliesRequestStatus, suppliesStatus } from 'app/shared/model/enumerations/supplies-request';
import React from 'react';

const InvoiceTypeDisplayNameMapping = {
  [InvoiceType.CONTRACT]: 'Hợp đồng',
  [InvoiceType.PAYMENT_VOUCHER]: 'Phiếu chi',
  [InvoiceType.PAYMENT_REQUEST]: 'Đề nghị thanh toán',
  [InvoiceType.ADVANCE_PAYMENT_REQUEST]: 'Đề nghị tạm ứng',
  [InvoiceType.ADVANCE_PAYMENT_RETURN]: 'Hoàn tạm ứng',
};

export const invoiceTypeMapping = (type: InvoiceType) => InvoiceTypeDisplayNameMapping[type];

const mapSupplierStatusText = (status: boolean): string => {
  switch (status) {
    case true:
      return 'Hoạt động';
    case false:
      return 'Không hoạt động';
    default:
      return '';
  }
};

const mapSupplierStatusBadge = (status: boolean) => {
  switch (status) {
    case true:
      return <Badge color={'success'}>Hoạt động</Badge>;
    case false:
      return <Badge color={'error'}>Không hoạt động</Badge>;
    default:
      return <></>;
  }
};

export const suppliersRequestStatusBadge = (key: keyof typeof suppliesStatus) => {
  const label = suppliesStatus[key];
  switch (key) {
    case 'APPROVED':
      return <BadgeV2 color='success'>{label}</BadgeV2>
    case 'WAITING_APPROVE':
      return <BadgeV2 color='waiting'>{label}</BadgeV2>
    case 'NEW':
      return <BadgeV2 color='warning'>{label}</BadgeV2>
    case 'COMPLETED':
      return <BadgeV2 color='success'>{label}</BadgeV2>
    case 'REJECTED':
      return <BadgeV2 color='warning'>{label}</BadgeV2>
    case 'IN_PROGRESS':
      return <BadgeV2 color='warning'>{label}</BadgeV2>
    case 'CANCELLED':
      return <BadgeV2 color='warning'>{label}</BadgeV2>
    default:
      break;
  }
}

export default {
  mapSupplierStatusText,
  mapSupplierStatusBadge,
};
