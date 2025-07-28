import { attachments, convertedGoods, importConvertedGoods, importSelectedGoods, selectedGoods } from 'app/validation/supplies-request.validation'
import { z } from "zod"
import { InvoiceType } from '../constants/status'
import { DateObject } from 'react-multi-date-picker'
import { isValidDateObject } from 'app/validation/custom.validation'

const currencySchema = z.object({
  id: z.string({
    message: 'Vui lòng chọn loại tiền',
  }).uuid(),
  code: z.string(),
  rate: z.coerce.number({
    message: 'Tỉ giá là bắt buộc và phải là số',
  }).positive({
    message: 'Tỉ giá phải lớn hơn 0',
  }),
}, {
  message: 'Vui lòng chọn đơn vị tiền tệ',
})

const supplies = z.object({
  supplierId: z.string({
    message: 'Vui lòng chọn nhà cung cấp',
  }).uuid({
    message: 'Nhà cung cấp không đúng định dạng',
  }),
  supplierTaxCode: z.string().optional(),
  debtDays: z.coerce.number({
    message: 'Số ngày công nợ là bắt buộc và phải là số',
  }).positive({
    message: 'Số ngày công nợ phải lớn hơn 0',
  }),
  supplierAddress: z.string({
    message: 'Vui lòng nhập địa chỉ',
  }),
  supplierName: z.string({
    message: 'Vui lòng nhập tên nhà cung cấp',
  }),
  supplierPhone: z.string({
    message: 'Vui lòng nhập số điện thoại',
  }),
})

export const relativedFees = z.object({
  relativedFees: z.array(z.object({
    id: z.string().optional(),
    invoiceId: z.string(),
    invoiceDate: z.coerce.date(),
    createdAt: z.coerce.date(),
    supplierId: z.string(),
    taxCode: z.string(),
    paymentMethodId: z.string(),
    paymentMethodCode: z.string(),
    paymentMethodName: z.string(),
    vatPercent: z.number().optional(),
    cost: z.number().optional(),
    vat: z.number().optional(),
    totalAmountAfterVat: z.number().optional(),
    grandTotal: z.number().optional(),
    debtDays: z.coerce.number(),
    note: z.string().optional(),
  })).min(1, {
    message: 'Vui lòng chọn ít nhất 1 phí liên quan',
  })
})

export type RelativedFees = z.infer<typeof relativedFees>

export const incomingInvoice = z.object({
  summary: z.object({
    patternNo: z.string({
      message: 'Vui lòng nhập mẫu số',
    }),
    series: z.string().optional(),
    invoiceNo: z.string({
      message: 'Vui lòng điền số hoá đơn',
    }),
    paymentMethod: z.string({
      message: 'Vui lòng chọn hình thức thanh toán',
    }),
    currency: currencySchema,
    content: z.string().optional(),
    employeeId: z.string().uuid().optional(),
    departmentName: z.string().optional(),
  }),
  provider: supplies,
  attachments,
  inventoryIds: z.string().array().min(1, {
    message: 'Vui lòng chọn ít nhất 1 phiếu nhập kho',
  }),
  inventoryCodes: z.string().array(),
  supplierContractId: z.string({
    message: 'Vui lòng chọn hợp đồng',
  }).uuid({
    message: 'Vui lòng chọn hợp đồng',
  }),
  supplierContractCode: z.string(),
  invoiceDate: z.any().optional(),
  createdAt: z.any().optional(),
  orderCreatedAt: z.custom<DateObject>(value => isValidDateObject(value),{
    message: "Vui lòng chọn ngày phát sinh",
  }),
  isInvoice: z.boolean().default(false),
  totalRoot: z.coerce.number().optional(),
})

export const invoiceSupplies = z.object({
  invoiceSupplies: z.array(convertedGoods).min(1, {
    message: 'Vui lòng chọn ít nhất 1 danh sách hàng hóa',
  }),
  totalInvoice: z.coerce.number().optional(),
})

export const importInvoiceSupplies = z.object({
  invoiceSupplies: z.array(importConvertedGoods).min(1, {
    message: 'Vui lòng chọn ít nhất 1 danh sách hàng hóa',
  }),
  totalInvoice: z.coerce.number().optional(),
})

const tmp = z.object({
  tmp: z.object({
    selectedGoods,
  })
})

const importTmp = z.object({
  tmp: z.object({
    selectedGoods: importSelectedGoods,
  })
})

export const incomingInvoiceV2Schema = z.discriminatedUnion('invoiceType', [
  z.object({
    invoiceType: z.literal(InvoiceType.INVOICE),
  }).merge(incomingInvoice).merge(invoiceSupplies),
  z.object({
    invoiceType: z.literal(InvoiceType.IMPORT_INVOICE),
  }).merge(incomingInvoice).merge(importInvoiceSupplies).merge(relativedFees),
]).default({ invoiceType: InvoiceType.INVOICE })

export type IncomingInvoiceV2SchemaType = z.infer<typeof incomingInvoiceV2Schema>
