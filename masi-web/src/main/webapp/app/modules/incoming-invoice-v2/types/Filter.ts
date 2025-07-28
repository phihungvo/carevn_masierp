import { DateObject } from "react-multi-date-picker";

export type Filter = {
  'date': DateObject[],
  'startDate': string,
  'endDate': string,
  search: string,
  page: number,
  size: number,
  supplierId: string
  hasInvoice?: boolean
}

export type ImportFilter = {
  'customerId.equals': string,
  'code.contains': string,
  'createdAt.greaterThanOrEqual': DateObject,
  'createdAt.lessThanOrEqual': DateObject,
  page: number,
  size: number,
  'invoiceId.specified': boolean,
  search: string,
  'contractCode.contains': string,
}
