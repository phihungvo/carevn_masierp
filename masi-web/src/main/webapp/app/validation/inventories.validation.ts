import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';

export const inventoriesSchema = z
  .object({
    code: z.string().optional(),
    incomingWarehouseId: z.string({ message: 'Vui lòng chọn kho' }),
    inventoriesTypeId: z.string({ message: 'Vui lòng chọn loại kho' }),
    dateCreate: z.custom<DateObject>(value => isValidDateObject(value), {
      message: 'Vui lòng chọn ngày tạo',
    }),
    createdBy: z.string().optional(),
    createdByName: z.string().optional(),
    employeeName: z.string().optional(),
    employeeId: z.string({ message: 'Vui lòng chọn nhân viên' }),
    departmentId: z.string().optional(),
    workspaceName: z.string().optional(),
    isInvoice: z.boolean().optional(),
    isNoReview: z.boolean().optional(),
    createdAt: z
      .custom<DateObject>(value => isValidDateObject(value))
      .optional(),
    note: z.string().optional(),
    customerId: z.string({ message: 'Vui lòng chọn nhà cung cấp' }),
    taxCode: z.string().optional(),
    address: z.string().optional(),
    shipper: z.string({ message: 'Vui lòng nhập người giao' }),
    shipperPhone: z.string({ message: 'Vui lòng nhập số điện thoại' }),
    inventoriesDetails: z
      .array(
        z.object({
          id: z.string().optional(),
          code: z.string().optional(),
          itemId: z.string({ message: 'Vui lòng chọn sản phẩm' }),
          quantity: z.string({ message: 'Vui lòng nhập số lượng' }),
          price: z.string({ message: 'Vui lòng nhập đơn giá' }),
          note: z.string().optional(),
          uomId: z.string()?.optional(),
          totalPrice: z.any().optional(),
        }),
      )
      .min(1, 'Vui lòng thêm hàng hóa'),
    invoiceId: z.string().optional(),
    purchaseContractId: z.string().optional(),
    productionId: z.string().optional(),
    invoices: z
      .array(
        z.object({
          id: z.string().optional(),
          invoiceNo: z.string().optional(),
        }),
      )
      .optional(),
    invoice: z
      .object({
        id: z.string().optional(),
        invoiceNo: z.string().optional(),
      })
      .optional(),
    status: z.string().optional(),
    requestApprovals: z
      .array(
        z.object({
          id: z.string().optional(),
          index: z.number().optional(),
          department: z.string().optional(),
          employeeId: z
            .string()
            .refine(value => value.trim() !== '', {
              message: 'Vui lòng chọn người ký',
            })
            .optional(),
          employee: z
            .object({
              code: z.string().optional(),
              fullName: z.string().optional(),
            })
            .optional(),
        }),
      )
      .optional(),
    file: z
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
    if (
      Boolean(data.isNoReview) === false &&
      (!data.requestApprovals || data.requestApprovals.length < 1)
    ) {
      ctx.addIssue({
        code: 'custom',
        path: ['requestApprovals'],
        message: 'Vui lòng chọn người ký',
      });
    }
  });

export const inventoriesFilterSchema = z.object({
  createdAt: z
    .array(z.custom<DateObject>(value => isValidDateObject(value)).optional())
    .optional(),
  status: z.string().optional(),
  inventoriesTypeId: z.string().optional(),
  isInvoice: z.boolean().optional(),
  customerId: z.string().optional(),
  incomingWarehouseId: z.string().optional(),
});

export type InventoriesSchema = z.infer<typeof inventoriesSchema>;
export type InventoriesFilterSchema = z.infer<typeof inventoriesFilterSchema>;
