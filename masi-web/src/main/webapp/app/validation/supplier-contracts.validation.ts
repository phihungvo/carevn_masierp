import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';
import { SUPPLIER_CONTRACT_STATUS } from 'app/shared/model/supplier-contract.model';

export const supplierContractsSchema = z
  .object({
    contractCode: z.string().optional(),
    contractName: z.string().optional(),
    createdBy: z.string().optional(),
    createdByName: z.string().optional(),
    createdAt: z
      .custom<DateObject>(value => isValidDateObject(value))
      .optional(),
    note: z.string().optional(),

    contractDate: z.custom<DateObject>(value => isValidDateObject(value), {
      message: 'Vui lòng chọn ngày lập',
    }),
    deliveryEstDate: z.custom<DateObject>(value => isValidDateObject(value), {
      message: 'Vui lòng chọn ngày giao dự kiến',
    }),
    startDate: z.custom<DateObject>(value => isValidDateObject(value), {
      message: 'Vui lòng chọn ngày bắt đầu',
    }),
    endDate: z.custom<DateObject>(value => isValidDateObject(value), {
      message: 'Vui lòng chọn ngày kết thúc',
    }),

    paymentTermNumber: z.string({
      message: 'Vui lòng nhập thời hạn thanh toán',
    }),
    contractAmount: z.string({ message: 'Vui lòng nhập giá trị hợp đồng' }),
    totalAmount: z.string().optional(),

    supplierId: z.string({ message: 'Vui lòng chọn NCC' }),
    supplierTaxCode: z.string().optional(),
    supplierAddress: z.string().optional(),
    supplierFullName: z.string({ message: 'Vui lòng nhập người đại diện' }),
    supplierPosition: z.string({ message: 'Vui lòng nhập chức vụ' }),
    supplierPhone: z.string({ message: 'Vui lòng nhập số điện thoại' }),
    supplierEmail: z.string({ message: 'Vui lòng nhập email' }),

    supplierContractDetails: z
      .array(
        z.object({
          id: z.string().optional(),
          code: z.string().optional(),
          itemId: z.string({ message: 'Vui lòng chọn sản phẩm' }),
          quantity: z.string({ message: 'Vui lòng nhập số lượng' }),
          price: z.string({ message: 'Vui lòng nhập đơn giá' }),
          note: z.string().optional(),
          uomId: z.string()?.optional(),
          totalPrice: z.string().optional(),
          vatId: z.string().optional(),
          vatRate: z.string().optional(),
          vatAmount: z.string().optional(),
        }),
      )
      .min(1, 'Vui lòng thêm hàng hóa'),

    invoiceId: z.string().optional(),
    suppliesRequestId: z.string().optional(),
    invoices: z
      .array(
        z.object({
          id: z.string().optional(),
          invoiceNo: z.string().optional(),
        }),
      )
      .optional(),

    status: z.string({ message: 'Vui lòng chọn trạng thái' }),
    requestApprovals: z.any(),
    liquidationRequestApprovals: z.any(),
    attachments: z
      .array(
        z.object({
          fileId: z.string().optional(),
          fileName: z.string().optional(),
          createdAt: z.date().optional(),
        }),
      )
      .optional(),
  })
  .superRefine((data, ctx) => {
    if (data?.endDate && data?.startDate) {
      if (data?.endDate < data?.startDate)
        ctx.addIssue({
          code: 'custom',
          path: ['endDate'],
          message: 'Ngày kết thúc không hợp lệ',
        });
    }
  });

export const supplierContractFilterSchema = z.object({
  createdAt: z
    .array(z.custom<DateObject>(value => isValidDateObject(value)).optional())
    .optional(),
  status: z.string().optional(),
  supplierId: z.string().optional(),
});

export type SupplierContractsSchema = z.infer<typeof supplierContractsSchema>;
export type SupplierContractsFilterSchema = z.infer<
  typeof supplierContractFilterSchema
>;
