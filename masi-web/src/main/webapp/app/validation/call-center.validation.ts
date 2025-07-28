import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';

export const callCenterSchema = z
  .object({
    customerId: z.string().optional(),
    employeeCreatedId: z.string().optional(),
    groupCS: z.string({ message: 'Vui lòng chọn nhóm' }),
    phoneOfCaller: z.string({ message: 'Vui lòng nhập SĐT' }),
    phoneOfName: z.string({ message: 'Vui lòng nhập tên' }),
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
  })
  .superRefine((data, ctx) => {
    if (!data.customerId && !data.employeeCreatedId) {
      ctx.addIssue({
        code: 'custom',
        path: ['customerId'],
        message: 'Vui lòng chọn khách hàng',
      });
      ctx.addIssue({
        code: 'custom',
        path: ['employeeCreatedId'],
        message: 'Vui lòng chọn nhân viên',
      });
    }
  });

export const callCenterFilterSchema = z.object({
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

export type CallCenterSchema = z.infer<typeof callCenterSchema>;
export type CallCenterFilterSchema = z.infer<typeof callCenterFilterSchema>;
