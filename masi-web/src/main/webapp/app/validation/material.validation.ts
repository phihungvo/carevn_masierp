import { z } from 'zod';

export const materialSchema = z.object({
  materialName: z
    .string({ message: 'Vui lòng nhập tên nguyên liệu' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập tên nguyên liệu' }),
  materialNameEn: z
    .string({ message: 'Vui lòng nhập tên nguyên liệu' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập tên nguyên liệu' }),
});

export type MaterialSchema = z.infer<typeof materialSchema>;
