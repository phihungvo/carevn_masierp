import { z } from 'zod';

export const uomSchema = z.object({
  name: z.string({ message: 'Vui lòng nhập tên đơn vị' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập tên đơn vị' }),
});

export const uomGroupSchema = z.object({
  name: z.string({ message: 'Vui lòng nhập tên đơn vị' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập tên đơn vị' }),
  baseUomId: z
    .string({ message: 'Vui lòng chọn đơn vị cơ bản' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng chọn đơn vị cơ bản' }),
  uomGroupDetailsDTOs: z.array(
    z.object({
      id: z.string().optional(),
      name: z
        .string({ message: 'Vui lòng nhập tên cách quy đổi' })
        .refine(value => value.trim() !== '', { message: 'Vui lòng nhập tên đơn vị' }),
      baseUomId: z.string({ message: 'Vui lòng chọn đơn vị' }).refine(value => value.trim() !== '', { message: 'Vui lòng chọn đơn vị' }),
      baseQty: z
        .string({ message: 'Vui lòng nhập số lượng/khối lượng' })
        .refine(value => value.trim() !== '', { message: 'Vui lòng nhập số lượng/khối lượng' }),
      altUomId: z
        .string({ message: 'Vui lòng chọn đơn vị quy đổi' })
        .refine(value => value.trim() !== '', { message: 'Vui lòng chọn đơn vị quy đổi' }),
      altQty: z
        .string({ message: 'Vui lòng nhập số lượng/khối lượng quy đổi' })
        .refine(value => value.trim() !== '', { message: 'Vui lòng nhập số lượng/khối lượng quy đổi' }),
    }),
  ),
});

export type UomSchema = z.infer<typeof uomSchema>;
export type UomGroupSchema = z.infer<typeof uomGroupSchema>;
