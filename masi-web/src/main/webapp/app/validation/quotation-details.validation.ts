import { z } from 'zod';

const DESCRIPTION_MESSAGE = 'Vui lòng nhập mô tả';
const QUANTITY_MESSAGE = 'Vui lòng nhập số lượng';
const UNIT_PRICE_MESSAGE = 'Vui lòng nhập đơn giá';

export const quotationDetailsKimLongSchema = z.object({
  quotationDetails: z.array(
    z.object({
      id: z.string().optional(),
      materialId: z
        .string({ message: 'Vui lòng chọn loại hàng' })
        .refine(value => value.trim() !== '', { message: 'Vui lòng chọn loại hàng' }),
      note: z
        .string({ message: 'Vui lòng nhập thông tin sản phẩm' })
        .refine(value => value.trim() !== '', { message: 'Vui lòng nhập thông tin sản phẩm' }),
      priceType: z
        .string({ message: 'Vui lòng nhập hình thức giá' })
        .refine(value => value.trim() !== '', { message: 'Vui lòng nhập hình thức giá' }),
      priceTypeEn: z.string({ message: 'Vui lòng nhập hình thức giá' }).optional(),
      // .refine(value => value.trim() !== '', { message: 'Vui lòng nhập hình thức giá' }),
      weight: z
        .string({ message: 'Vui lòng nhập khối lượng' })
        .refine(value => value.trim() !== '', { message: 'Vui lòng nhập khối lượng' }),
      nitrogen180Price: z.string({ message: 'Vui lòng nhập giá TVN 180 mgN / 100g' }).optional(),
      nitrogen150Price: z.string({ message: 'Vui lòng nhập giá TVN 150 mgN / 100g' }).optional(),
    }),
  ),
});

export const quotationDetailsMMSSchema = z.object({
  quotationDetails: z.array(
    z.object({
      id: z.string().optional(),
      materialId: z
        .string({ message: 'Vui lòng chọn loại hàng' })
        .refine(value => value.trim() !== '', { message: 'Vui lòng chọn loại hàng' }),
      note: z
        .string({ message: 'Vui lòng nhập thông tin sản phẩm' })
        .refine(value => value.trim() !== '', { message: 'Vui lòng nhập thông tin sản phẩm' }),
      weight: z
        .string({ message: 'Vui lòng nhập khối lượng' })
        .refine(value => value.trim() !== '', { message: 'Vui lòng nhập khối lượng' }),
      price: z.string({ message: 'Vui lòng nhập đơn giá' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập đơn giá' }),
      materialCriteria: z
        .string({ message: 'Vui lòng nhập chỉ tiêu nguyên liệu' })
        .refine(value => value.trim() !== '', { message: 'Vui lòng nhập chỉ tiêu nguyên liệu' }),
      materialCriteriaEn: z.string({ message: 'Vui lòng nhập chỉ tiêu nguyên liệu' }).optional(),
      // .refine(value => value.trim() !== '', { message: 'Vui lòng nhập chỉ tiêu nguyên liệu' }),
    }),
  ),
});

export type QuotationDetailsKimLongSchema = z.infer<typeof quotationDetailsKimLongSchema>;
export type QuotationDetailsMMSSchema = z.infer<typeof quotationDetailsMMSSchema>;
