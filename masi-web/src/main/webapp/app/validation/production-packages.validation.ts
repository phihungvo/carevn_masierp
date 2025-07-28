import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';

const PACKAGE_CODE_MESSAGE = 'Vui lòng nhập mã gói sản phẩm';
const WORK_ORDER_MESSAGE = 'Vui lòng chọn công đoạn SX';
const QUANTITY_MESSAGE = 'Vui lòng nhập số lượng';

export const productionPackageSchema = z.object({
  packageCode: z
    .string({ message: PACKAGE_CODE_MESSAGE })
    .refine(value => value.trim() !== '', { message: PACKAGE_CODE_MESSAGE }),
  manufactureOrderId: z.string({ message: WORK_ORDER_MESSAGE }),
  quantity: z
    .string({ message: QUANTITY_MESSAGE })
    .refine(value => value !== '', { message: QUANTITY_MESSAGE })
    .refine(
      value => {
        const floatValue = parseFloat(value);
        return !isNaN(floatValue) && floatValue >= 0;
      },
      {
        message: QUANTITY_MESSAGE,
      },
    ),
  status: z.string({ message: 'Vui lòng chọn trạng thái' }),
  note: z.string().optional(),
  weight: z.string().optional(),
  packageBy: z.string({ message: 'Vui lòng chọn người đóng gói' }),
  packageAt: z.custom<DateObject>(value => isValidDateObject(value), {
    message: 'Vui lòng chọn ngày đóng gói',
  }),
  statusU: z.string().optional(),
});

export type ProductionPackageFormSchema = z.infer<
  typeof productionPackageSchema
>;

export const productionPackageFilterSchema = z.object({
  status: z.string().optional(),
  manufactureOrderId: z.string().optional(),
});

export type ProductionPackageFilterSchema = z.infer<
  typeof productionPackageFilterSchema
>;
