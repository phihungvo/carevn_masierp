import { z } from 'zod';

const DECIMAL_REGEX = /^\d+(\.\d{1,2})?$/;

const HOURS_MESSAGE_REQUIRED = 'Vui lòng nhập số giờ';
const HOURS_MESSAGE_INVALID = 'Số giờ phải lớn hơn hoặc bằng 0 và nhỏ hơn hoặc bằng 16';

export const updateTimeSheetSchema = z.object({
  hoursWorked: z
    .string()
  // .refine(value => !!value, { message: HOURS_MESSAGE_REQUIRED })
  // .refine(
  //   value => {
  //     const floatValue = parseFloat(value);
  //     return value.match(DECIMAL_REGEX) && !isNaN(floatValue) && floatValue >= 0 && floatValue <= 16;
  //   },
  //   {
  //     message: HOURS_MESSAGE_INVALID,
  //   },
  // ),
  ,
  date: z.string().optional(),
});

export type UpdateTimeSheetFormSchema = z.infer<typeof updateTimeSheetSchema>;
