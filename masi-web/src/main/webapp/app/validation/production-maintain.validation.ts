import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';
import dayjs from 'dayjs';

const PRODUCT_BATCH_CODE_MESSAGE = 'Vui lòng nhập mã lô hàng';
const PRODUCT_BATCH_NAME_MESSAGE = 'Vui lòng nhập tên lô hàng';
const MANUFACTURE_DATE_MESSAGE = 'Vui lòng chọn ngày SX';
const EXPIRED_DATE_MESSAGE = 'Vui lòng chọn thời hạn sử dụng';

export const productionMaintainSchema = z
  .object({
    productBatchCode: z
      .string({ message: PRODUCT_BATCH_CODE_MESSAGE })
      .refine(value => value.trim() !== '', {
        message: PRODUCT_BATCH_CODE_MESSAGE,
      }),
    productBatchName: z
      .string({ message: PRODUCT_BATCH_NAME_MESSAGE })
      .refine(value => value.trim() !== '', {
        message: PRODUCT_BATCH_NAME_MESSAGE,
      }),
    manufactureDate: z.custom<DateObject>(value => isValidDateObject(value), {
      message: MANUFACTURE_DATE_MESSAGE,
    }),
    expiredDate: z.custom<DateObject>(value => isValidDateObject(value), {
      message: EXPIRED_DATE_MESSAGE,
    }),
    productPackageId: z.string({ message: 'Vui lòng chọn mã đóng gói' }),
    productPackageQty: z.string().optional(),
    productPackageWeight: z.string().optional(),
    note: z.string().optional(),

    statusM: z.string().optional(), // Status Manufacture Order
  })
  .superRefine((data, ctx) => {
    if (
      dayjs(data?.manufactureDate.toDate()) > dayjs(data?.expiredDate.toDate())
    ) {
      ctx.addIssue({
        code: 'custom',
        path: ['expiredDate'],
        message: 'Thời hạn sử dụng không hợp lệ',
      });
    }
  });

export type ProductionMaintainFormSchema = z.infer<
  typeof productionMaintainSchema
>;

export const productionMaintainFilterSchema = z.object({
  manufactureDate: z
    .array(z.custom<DateObject>(value => isValidDateObject(value)).optional())
    .optional(),
  expiredDate: z
    .array(z.custom<DateObject>(value => isValidDateObject(value)).optional())
    .optional(),
});

export type ProductionMaintainFilterSchema = z.infer<
  typeof productionMaintainFilterSchema
>;
