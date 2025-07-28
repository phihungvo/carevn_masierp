import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';

export const companySchema = z.object({
  code: z.string().optional(),

  name: z.string({ message: 'Vui lòng nhập tên công ty' }),
  normalizedName: z.string({ message: 'Vui lòng nhập mã công ty' }),
  taxCode: z.string().optional(),
  parentId: z.string().optional(),

  description: z.string().optional(),

  website: z.string().optional(),
  callcenter: z.string().optional(),
  address: z.string().optional(),

  representativeName: z.string().optional(),
  representativePhone: z.string().optional(),
  representativeEmail: z.string().optional(),
  representativeIdNumber: z.string().optional(),
  imageId: z.string().optional(),
  employeeCreatedId: z.string().optional(),

  representativeDob: z.custom<DateObject>(value => isValidDateObject(value), {
    message: 'Vui lòng chọn ngày sinh',
  }),

  createdBy: z.string().optional(),
  createdAt: z.string().optional(),

  isActive: z.boolean().optional(),
});

export const companyFilterSchema = z.object({
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

export type CompanySchema = z.infer<typeof companySchema>;
export type CompanyFilterSchema = z.infer<typeof companyFilterSchema>;
