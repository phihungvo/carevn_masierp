import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';
import dayjs from 'dayjs';

const NAME_MESSAGE = 'Vui lòng nhập tên định mức';
const DUE_DATE_MESSAGE_REQUIRED = 'Vui lòng chọn ngày hoàn thành';
const PRODUCTION_POWDER_QTY_MESSAGE = 'Vui lòng nhập khối lượng';
const QTY_MESSAGE = 'Vui lòng nhập kế hoạch thu mua';

export const productionStandardSchema = z.object({
  code: z.string().optional(),
  name: z
    .string({ message: NAME_MESSAGE })
    .refine(value => value.trim() !== '', { message: NAME_MESSAGE }),
  // dueDate: z.custom<DateObject>(value => isValidDateObject(value), {
  //   message: DUE_DATE_MESSAGE_REQUIRED,
  // }),
  dueDate: z
    .string({ message: NAME_MESSAGE })
    .refine(value => value.trim() !== '', {
      message: DUE_DATE_MESSAGE_REQUIRED,
    }),
  productionPowderQty: z
    .string()
    .optional()
    .refine(
      value => {
        const floatValue = parseFloat(value);
        return !isNaN(floatValue) && floatValue >= 0;
      },
      {
        message: PRODUCTION_POWDER_QTY_MESSAGE,
      },
    ),
  quantity: z
    .string()
    .optional()
    .refine(
      value => {
        const floatValue = parseFloat(value);
        return !isNaN(floatValue) && floatValue >= 0;
      },
      { message: QTY_MESSAGE },
    ),
  note: z.string().optional(),
});

export type ProductionStandardFormSchema = z.infer<
  typeof productionStandardSchema
>;

export const productionStandardFilterSchema = z.object({
  startDate: z.string().optional(),
  endDate: z.string().optional(),
});

export type ProductionStandardFilterSchema = z.infer<
  typeof productionStandardFilterSchema
>;
