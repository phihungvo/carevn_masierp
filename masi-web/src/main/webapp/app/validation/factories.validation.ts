import { z } from 'zod';

export const factoriesSchema = z.object({
  code: z
    .string({ message: 'Vui lòng nhập mã nhà máy' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập mã nhà máy',
    }),
  name: z
    .string({ message: 'Vui lòng nhập tên nhà máy' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập mã nhà máy',
    }),
  address: z
    .string({ message: 'Vui lòng nhập địa chỉ' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập địa chỉ',
    }),
  locationX: z.optional(z.string()),
  locationY: z.optional(z.string()),
  employeeOwnerId: z
    .string({ message: 'Vui lòng chọn người quản lý' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng chọn người quản lý',
    }),
  company: z
    .string({ message: 'Vui lòng chọn trực thuộc' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng chọn trực thuộc',
    }),
  status: z.string({ message: 'Vui lòng chọn tình trạng' }),
  note: z.optional(z.string()),
});

export type FactoriesSchema = z.infer<typeof factoriesSchema>;
