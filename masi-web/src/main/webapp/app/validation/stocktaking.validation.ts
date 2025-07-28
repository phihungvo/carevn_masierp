import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';

export const stocktakingSchema = z.object({
  code: z.string().optional(),

  createdBy: z.string().optional(),
  createdByName: z.string().optional(),
  createdAt: z.custom<DateObject>(value => isValidDateObject(value)).optional(),
  note: z.string().optional(),

  checkDate: z.custom<DateObject>(value => isValidDateObject(value), {
    message: 'Vui lòng chọn ngày lập',
  }),

  warehouseId: z.string({ message: 'Vui lòng chọn NCC' }),

  inventoryPerson: z
    .array(
      z.object({
        id: z.string().optional(),
        code: z.string().optional(),
        fullName: z.string().optional(),
        position: z.string().optional(),
      }),
    )
    .refine(data => data[0]?.id, { message: 'Vui lòng nhập tên người kiểm' }),

  itemInventories: z
    .array(
      z.object({
        id: z.string().optional(),
        itemId: z.string({ message: 'Vui lòng chọn hàng hóa' }),
        uomName: z.string().optional(),
        totalQty: z.string().optional(),
        totalQtyActual: z.string({ message: 'Nhập SL tồn thực tế' }),
        totalQtyDifference: z.string().optional(),
        note: z.string().optional(),
      }),
    )
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
    .min(1, 'Vui lòng chọn người ký'),
  attachments: z
    .array(
      z.object({
        fileId: z.string().optional(),
        fileName: z.string().optional(),
        createdAt: z.date().optional(),
      }),
      { message: 'Vui lòng chọn tệp đính kèm' },
    )
    .min(1, 'Vui lòng chọn tệp đính kèm'),
});

export const stocktakingFilterSchema = z.object({
  createdAt: z
    .array(z.custom<DateObject>(value => isValidDateObject(value)).optional())
    .optional(),
  status: z.string().optional(),
  supplierId: z.string().optional(),
});

export type StocktakingSchema = z.infer<typeof stocktakingSchema>;
export type StocktakingFilterSchema = z.infer<typeof stocktakingFilterSchema>;
