import { z } from 'zod';

export const warehouseSchema = z.object({
  name: z.string({ message: 'Vui lòng nhập tên kho' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập tên kho' }),
  address: z.string({ message: 'Vui lòng nhập địa chỉ' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập địa chỉ' }),
  warehouseTypePage: z
    .string({ message: 'Vui lòng chọn loại kho' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng chọn loại kho' }),
});

export type WarehouseSchema = z.infer<typeof warehouseSchema>;
