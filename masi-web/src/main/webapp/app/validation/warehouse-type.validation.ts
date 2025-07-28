import { z } from 'zod';

export const warehouseTypeSchema = z.object({
  name: z.string({ message: 'Vui lòng nhập tên loại kho' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập tên loại kho' }),
  description: z.string().optional(),
});

export type WarehouseTypeSchema = z.infer<typeof warehouseTypeSchema>;
