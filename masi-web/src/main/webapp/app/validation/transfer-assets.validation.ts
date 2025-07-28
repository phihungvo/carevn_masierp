import { zhCN } from 'react-day-picker/locale';
import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';

export const transferAssetsSchema = z.object({
  code: z
    .string({ message: 'Vui lòng nhập mã điều chuyển' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập mã điều chuyển',
    }),
  transactionTypeId: z
  .string({ message: 'Vui lòng chọn loại phát sinh' })
  .refine(value => value.trim() !== '', {
    message: 'Vui lòng chọn loại phát sinh',
  }),
  transferDate: z.custom<DateObject>(value => isValidDateObject(value), { message:  'Vui lòng chọn ngày điều chuyển'}),
  fromDepartmentId: z
  .string({ message: 'Vui lòng chọn loại phát sinh' })
  .refine(value => value.trim() !== '', {
    message: 'Vui lòng chọn loại phát sinh',
  }),

  toDepartmentId: z
  .string({ message: 'Vui lòng chọn loại phát sinh' })
  .refine(value => value.trim() !== '', {
    message: 'Vui lòng chọn loại phát sinh',
  }),

  toAddress: z.optional(z.string()),
  toPersonId: z.optional(z.any()),
  attribute: z.optional(z.object({
    description:z.optional(z.string()),
  })),
  createdBy: z.optional(z.string()),
  description: z.optional(z.string()),
  createdAt: z.optional(z.date()),
  createdByName: z.optional(z.string()),
  assetTransferDetailsDTOS: z.array(z.object({
    employeeFromId: z.optional(z.any()),
    employeeToId:z.optional(z.any()),
    inventoriesStorageId: z.string(),
    attribute: z.optional(z.object({
      description:z.optional(z.string()),
    }))
  }),{
    message: "Vui lòng chọn tài sản",
  }).min(1, {
    message: "Vui lòng chọn tài sản",
  })
});

export type TransferAssetsSchema = z.infer<typeof transferAssetsSchema>;
