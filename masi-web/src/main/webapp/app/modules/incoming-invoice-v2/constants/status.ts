export const incomingStatus = {
  NEW: 'Chưa HĐ',
  CANCELLED: 'Hủy',
  PAID: 'Đã HĐ',
}

export const incommingBadge = {
  NEW: 'error',
  CANCELLED: 'gray',
  PAID: 'success',
}

export enum InvoiceType {
  INVOICE = 'INVOICE',
  IMPORT_INVOICE = 'IMPORT_INVOICE',
}
