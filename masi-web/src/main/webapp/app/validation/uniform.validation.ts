import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';
import { UNIFORM_RELEASE_TYPE } from 'app/shared/model/enumerations/uniform.model';

const NAME_MESSAGE = 'Vui lòng nhập tên đơn hàng';
const UNIFORM_MESSAGE = 'Vui lòng chọn loại đồng phục';
const QUANTITY_MESSAGE = 'Vui lòng nhập số lượng';
const DATE_MESSAGE = 'Vui lòng chọn ngày';
const EMPLOYEE_MESSAGE = 'Vui lòng chọn nhân viên';
const TYPE_MESSAGE = 'Vui lòng chọn loại xuất';

export const uniformOrderSchema = z.object({
  name: z.string({ message: NAME_MESSAGE }).refine(value => value.trim() !== '', { message: NAME_MESSAGE }),
  supplierId: z
    .string({ message: 'Vui lòng chọn nhà cung cấp' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng chọn nhà cung cấp' }),
  date: z.custom<DateObject>(value => isValidDateObject(value), { message: DATE_MESSAGE }),
  uniformDetails: z.array(
    z.object({
      id: z.string().optional(),
      uniformId: z.string({ message: UNIFORM_MESSAGE }).refine(value => value.trim() !== '', { message: UNIFORM_MESSAGE }),
      quantity: z.string({ message: QUANTITY_MESSAGE }).refine(value => value.trim() !== '', { message: QUANTITY_MESSAGE }),
      basePrice: z.string().optional(),
      actualPrice: z
        .string({ message: 'Vui lòng nhập giá mua' })
        .refine(value => value.trim() !== '', { message: 'Vui lòng nhập giá mua' }),
    }),
  ),
});

export const uniformReleaseSchema = z.object({
  date: z.custom<DateObject>(value => isValidDateObject(value), { message: DATE_MESSAGE }),
  employeeId: z.string({ message: EMPLOYEE_MESSAGE }).refine(value => value.trim() !== '', { message: EMPLOYEE_MESSAGE }),
  // uniformId: z.string({ message: UNIFORM_MESSAGE }).refine(value => value.trim() !== '', { message: UNIFORM_MESSAGE }),
  // quantity: z.string({ message: QUANTITY_MESSAGE }).refine(value => value.trim() !== '', { message: QUANTITY_MESSAGE }),
  note: z.any().optional(),
  fileId: z.any().optional(),
  type: z.nativeEnum(UNIFORM_RELEASE_TYPE, { message: TYPE_MESSAGE }),
  cost: z.any().optional(),
  isReturned: z.any().optional(),
  warehouseId: z.string({ message: 'Vui lòng chọn kho' }).refine(value => value.trim() !== '', { message: 'Vui lòng chọn kho' }),
  uniformDetails: z.array(
    z.object({
      id: z.string().optional(),
      uniformId: z.string({ message: UNIFORM_MESSAGE }).refine(value => value.trim() !== '', { message: UNIFORM_MESSAGE }),
      quantity: z.string({ message: QUANTITY_MESSAGE }).refine(value => value.trim() !== '', { message: QUANTITY_MESSAGE }),
      basePrice: z.string().optional(),
      actualPrice: z
        .string({ message: 'Vui lòng nhập giá mua' })
        .refine(value => value.trim() !== '', { message: 'Vui lòng nhập giá mua' }),
    }),
  ),
});

export const uniformReturnSchema = z.object({
  date: z.custom<DateObject>(value => isValidDateObject(value), { message: DATE_MESSAGE }),
  // employeeId: z.string({ message: EMPLOYEE_MESSAGE }).refine(value => value.trim() !== '', { message: EMPLOYEE_MESSAGE }),
  releaseId: z.string({ message: 'Vui lòng chọn đơn xuất' }).refine(value => value.trim() !== '', { message: 'Vui lòng chọn đơn xuất' }),
  returnDetails: z.array(
    z.object({
      id: z.string().optional(),
      uniformId: z.string({ message: UNIFORM_MESSAGE }).refine(value => value.trim() !== '', { message: UNIFORM_MESSAGE }),
      quantity: z.string({ message: QUANTITY_MESSAGE }).optional(),
      initQuantity: z.string().optional(),
      returnedQuantity: z.string().optional(),
    }),
  ),
});

export const uniformStockSchema = z.object({
  uniformOrderId: z.string().optional(),
  warehouseId: z.string({ message: 'Vui lòng chọn kho' }).refine(value => value.trim() !== '', { message: 'Vui lòng chọn kho' }),
  uniformFormDetailDTO: z.array(
    z.object({
      id: z.string().optional(),
      uniformId: z.string({ message: UNIFORM_MESSAGE }).optional(),
      quantity: z.string({ message: QUANTITY_MESSAGE }).optional(),
      initQuantity: z.string().optional(),
      returnedQuantity: z.string().optional(),
    }),
  ),
});

export const uniformSchema = z.object({
  // code: z.string({ message: 'Vui lòng nhập mã đồng phục' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập mã đồng phục' }),
  name: z
    .string({ message: 'Vui lòng nhập tên đồng phục' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập tên đồng phục' }),
  basePrice: z.string({ message: 'Vui lòng nhập đơn giá' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập đơn giá' }),
  uomId: z.string({ message: 'Vui lòng chọn đơn vị' }).refine(value => value.trim() !== '', { message: 'Vui lòng chọn đơn vị' }),
});

export type UniformOrderSchema = z.infer<typeof uniformOrderSchema>;
export type UniformReleaseSchema = z.infer<typeof uniformReleaseSchema>;
export type UniformReturnSchema = z.infer<typeof uniformReturnSchema>;
export type UniformStockSchema = z.infer<typeof uniformStockSchema>;
export type UniformSchema = z.infer<typeof uniformSchema>;
