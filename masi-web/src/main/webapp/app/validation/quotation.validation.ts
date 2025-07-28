import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';

const DESCRIPTION_MESSAGE = 'Vui lòng nhập mô tả';
const QUANTITY_MESSAGE = 'Vui lòng nhập số lượng';
const UNIT_PRICE_MESSAGE = 'Vui lòng nhập đơn giá';

export const quotationKimLongSchema = z.object({
  name: z
    .string({ message: 'Vui lòng nhập tên bảng báo giá' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập tên bảng báo giá' }),
  paymentMethod: z
    .string({ message: 'Vui lòng nhập phương thức thanh toán' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập phương thức thanh toán' }),
  paymentMethodEn: z.string({ message: 'Vui lòng nhập phương thức thanh toán' }).optional(),
  // .refine(value => value.trim() !== '', { message: 'Vui lòng nhập hình thức giá' }),
  deliveryLocation: z
    .string({ message: 'Vui lòng nhập địa chỉ giao hàng' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập địa chỉ giao hàng' }),
  deliveryLocationEn: z.string({ message: 'Vui lòng nhập địa chỉ giao hàng' }).optional(),
  // .refine(value => value.trim() !== '', { message: 'Vui lòng nhập địa chỉ giao hàng' }),
  // deliveryDate: z.custom<DateObject>(value => isValidDateObject(value), { message: 'Vui lòng chọn thời gian giao hàng' }),
  packaging: z.string({ message: 'Vui lòng nhập đóng gói' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập đóng gói' }),
  packagingEn: z.string({ message: 'Vui lòng nhập đóng gói' }).optional(),
  // .refine(value => value.trim() !== '', { message: 'Vui lòng nhập đóng gói' }),
  // minimumWeight: z
  //   .string({ message: 'Vui lòng nhập khối lượng giao hàng tối thiểu' })
  //   .refine(value => value.trim() !== '', { message: 'Vui lòng nhập khối lượng giao hàng tối thiểu' }),
  // priceType: z
  //   .string({ message: 'Vui lòng nhập hình thức giá' })
  //   .refine(value => value.trim() !== '', { message: 'Vui lòng nhập hình thức giá' }),
  // priceTypeEn: z.string({ message: 'Vui lòng nhập hình thức giá' }).optional(),
  // .refine(value => value.trim() !== '', { message: 'Vui lòng nhập hình thức giá' }),
  quotationDetails: z.array(
    z.union([
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
        // nitrogen180Price: z
        //   .string({ message: 'Vui lòng nhập giá TVN 180 mgN / 100g' })
        //   .refine(value => value.trim() !== '', { message: 'Vui lòng nhập giá TVN 180 mgN / 100g' }),
        // nitrogen150Price: z.string({ message: 'Vui lòng nhập giá TVN 150 mgN / 100g' }).optional(),
      }),
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
        // nitrogen180Price: z.string({ message: 'Vui lòng nhập giá TVN 180 mgN / 100g' }).optional(),
        // nitrogen150Price: z
        //   .string({ message: 'Vui lòng nhập giá TVN 150 mgN / 100g' })
        //   .refine(value => value.trim() !== '', { message: 'Vui lòng nhập giá TVN 150 mgN / 100g' }),
      }),
    ]),
  ),
  customerId: z
    .string({ message: 'Vui lòng chọn khách hàng' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng chọn khách hàng' }),
});

export const quotationMMSSchema = z.object({
  name: z
    .string({ message: 'Vui lòng nhập tên bảng báo giá' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập tên bảng báo giá' }),
  paymentMethod: z
    .string({ message: 'Vui lòng nhập phương thức thanh toán' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập phương thức thanh toán' }),
  paymentMethodEn: z.string({ message: 'Vui lòng nhập phương thức thanh toán' }).optional(),
  // .refine(value => value.trim() !== '', { message: 'Vui lòng nhập hình thức giá' }),
  packaging: z.string({ message: 'Vui lòng nhập đóng gói' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập đóng gói' }),
  packagingEn: z.string({ message: 'Vui lòng nhập đóng gói' }).optional(),
  // .refine(value => value.trim() !== '', { message: 'Vui lòng nhập đóng gói' }),
  materialCriteria: z
    .string({ message: 'Vui lòng nhập chỉ tiêu nguyên liệu' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập chỉ tiêu nguyên liệu' }),
  materialCriteriaEn: z.string({ message: 'Vui lòng nhập chỉ tiêu nguyên liệu' }).optional(),
  // .refine(value => value.trim() !== '', { message: 'Vui lòng nhập chỉ tiêu nguyên liệu' }),
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
      deliveryDate: z.custom<DateObject>(value => isValidDateObject(value), { message: 'Vui lòng chọn thời gian giao hàng' }),
      deliveryLocation: z
        .string({ message: 'Vui lòng nhập địa chỉ giao hàng' })
        .refine(value => value.trim() !== '', { message: 'Vui lòng nhập địa chỉ giao hàng' }),
      deliveryLocationEn: z.string({ message: 'Vui lòng nhập địa chỉ giao hàng' }).optional(),
      // .refine(value => value.trim() !== '', { message: 'Vui lòng nhập địa chỉ giao hàng' }),
    }),
  ),
  customerId: z
    .string({ message: 'Vui lòng chọn khách hàng' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng chọn khách hàng' }),
});

export const quotationUpdateSchema = z.object({
  name: z
    .string({ message: 'Vui lòng nhập tên bảng báo giá' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập tên bảng báo giá' }),
  paymentMethod: z
    .string({ message: 'Vui lòng nhập phương thức thanh toán' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập phương thức thanh toán' }),
  paymentMethodEn: z.string({ message: 'Vui lòng nhập phương thức thanh toán' }).optional(),
  // .refine(value => value.trim() !== '', { message: 'Vui lòng nhập hình thức giá' }),
  deliveryDate: z.custom<DateObject>(value => isValidDateObject(value), { message: 'Vui lòng chọn thời gian giao hàng' }),
  deliveryLocation: z
    .string({ message: 'Vui lòng nhập địa chỉ giao hàng' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập địa chỉ giao hàng' }),
  deliveryLocationEn: z.string({ message: 'Vui lòng nhập địa chỉ giao hàng' }).optional(),
  // .refine(value => value.trim() !== '', { message: 'Vui lòng nhập hình thức giá' }),
  packaging: z.string({ message: 'Vui lòng nhập đóng gói' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập đóng gói' }),
  packagingEn: z.string({ message: 'Vui lòng nhập đóng gói' }).optional(),
  // .refine(value => value.trim() !== '', { message: 'Vui lòng nhập đóng gói' }),
  minimumWeight: z
    .string({ message: 'Vui lòng nhập khối lượng giao hàng tối thiểu' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập khối lượng giao hàng tối thiểu' }),
});

export const internalRejectSchema = z.object({
  rejectNote: z
    .string({ message: 'Vui lòng nhập lý do từ chối' })
    .max(200, { message: 'Lý do từ chối không vượt quá 200 ký tự' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập lý do từ chối' }),
});

export type QuotationFormKimLongSchema = z.infer<typeof quotationKimLongSchema>;
export type QuotationMMSSchema = z.infer<typeof quotationMMSSchema>;
export type QuotationUpdateSchema = z.infer<typeof quotationUpdateSchema>;
export type InternalRejectFormSchema = z.infer<typeof internalRejectSchema>;
