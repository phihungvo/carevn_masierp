import { z } from 'zod';

export const groupSchema = z.object({
  name: z
    .string({ message: 'Vui lòng nhập tên nhóm quyền' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập tên nhóm quyền' }),
  description: z.string({ message: 'Vui lòng nhập mô tả' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập mô tả' }),
  authorities: z.array(z.any())
});

export type GroupSchema = z.infer<typeof groupSchema>;
