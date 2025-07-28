import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';

export const complainSchema = z.object({
  customerId: z.string({ message: 'Vui lòng chọn khách hàng' }),
  employeeCreatedId: z.string({ message: 'Vui lòng chọn nhân viên' }),
  groupCS: z.string({ message: 'Vui lòng chọn nhóm' }),
  sourceCS: z.string({ message: 'Vui lòng chọn nguồn' }),
  phoneOfCaller: z.string({ message: 'Vui lòng nhập (SĐT, Email)' }),
  phoneOfName: z.string({ message: 'Vui lòng nhập tên' }),
  // receptionDate: z.custom<DateObject>(value => isValidDateObject(value), {
  //   message: 'Vui lòng chọn thời gian',
  // }),
  receptionDate: z
    .string({ message: 'Vui lòng chọn ngày tiếp nhận' })
    .refine(date => !isNaN(Date.parse(date)), {
      message: 'Vui lòng chọn ngày tiếp nhận',
    })
    .transform(date => new Date(date)),
  typeCS: z.string({ message: 'Vui lòng chọn loại' }),
  status: z.string().optional(),
  problemContent: z.string({ message: 'Vui lòng nhập nội dung' }),
  solutions: z
    .array(
      z.object({
        fileId: z.string().optional(),
        resolutionContent: z.string().optional(),
        responseContent: z.string().optional(),
        employeeId: z.string().optional(),
        createdAt: z.date().optional(),
      }),
    )
    .optional(),

  createdBy: z.string().optional(),
  createdAt: z.string().optional(),
});

export const complainFilterSchema = z.object({
  type: z.string().optional(),
  status: z.string().optional(),
  groupCS: z.string().optional(),
  processDate: z
    .array(z.custom<DateObject>(value => isValidDateObject(value)).optional())
    .optional(),
  receptionDate: z
    .array(z.custom<DateObject>(value => isValidDateObject(value)).optional())
    .optional(),
});

export type ComplainSchema = z.infer<typeof complainSchema>;
export type ComplainFilterSchema = z.infer<typeof complainFilterSchema>;
