import { InvoiceType } from "app/shared/model/enumerations/invoice";


 const InvoiceTypeDisplayNameMapping = {
  [InvoiceType.CONTRACT]: 'Hợp đồng',
  [InvoiceType.PAYMENT_VOUCHER]: 'Phiếu chi',
  [InvoiceType.PAYMENT_REQUEST]: 'Đề nghị thanh toán',
  [InvoiceType.ADVANCE_PAYMENT_REQUEST]: 'Đề nghị tạm ứng',
  [InvoiceType.ADVANCE_PAYMENT_RETURN]: 'Hoàn tạm ứng',
};
export const invoiceTypeMapping = (type: InvoiceType) => InvoiceTypeDisplayNameMapping[type];

