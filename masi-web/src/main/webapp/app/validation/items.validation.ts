import { z } from 'zod';

export const itemSchema = z.object({
  name: z
    .string({ message: 'Vui lòng nhập tên VT - CCDC' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập tên VT - CCDC',
    }),
  code: z
    .string({ message: 'Vui lòng nhập mã VT - CCDC' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập mã VT - CCDC',
    }),
  uomId: z
    .string({ message: 'Vui lòng chọn DVT' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng chọn DVT' }),
  itemCategoryId: z
    .string({ message: 'Vui lòng chọn nhóm' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng chọn nhóm' }),
  // percentProtein: z.optional(z.string()).transform(s=>Number(s)),
  percentProtein: z.string().optional(),
  notes: z.optional(z.string()),
  supplierId: z
    .string({ message: 'Vui lòng chọn nhà cung cấp' })
    .refine(value => value?.trim() !== '', {
      message: 'Vui lòng chọn nhà cung cấp',
    }),
  vatRate: z.number({ message: 'Vui lòng chọn thuế VAT' }),
  nameEng: z.optional(z.string()),
  origin: z.string({ message: 'Vui lòng nhập xuất xứ' }),
  unitPrice: z.string({ message: 'Vui lòng nhập đơn giá' }),
  itemTypeId: z.string({ message: 'Vui lòng chọn loại' })
  .refine(value => value.trim() !== '', { message: 'Vui lòng chọn loại' }),
});

export type ItemSchema = z.infer<typeof itemSchema>;
