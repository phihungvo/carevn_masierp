import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';

export const supplierSchema = z.object({
  name: z
    .string({ message: 'Vui lòng nhập tên nhà cung cấp' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập tên nhà cung cấp',
    }),
  code: z
    .string({ message: 'Vui lòng nhập mã nhà cung cấp' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập mã nhà cung cấp',
    }),
  address: z
    .string({ message: 'Vui lòng nhập địa chỉ' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập địa chỉ' }),
  email: z.any(),
  phone: z.string({ message: 'Vui lòng nhập phone' }),
  note: z.string({ message: 'Vui lòng nhập note' }).nullable().optional(),
  taxCode: z.string({ message: 'Vui lòng nhập mã số thuế' }),
  // fax: z.string({ message: 'Vui lòng nhập fax' }),
  supplierGroupId: z.string({ message: 'Vui lòng chọn nhóm' }),
  // birthday: z.custom<DateObject>(value => isValidDateObject(value), {
  //   message: 'Vui lòng chọn ngày sinh',
  // }),
  birthday: z.custom<DateObject>(value => isValidDateObject(value)).optional(),
  paymentTermText: z.string().optional(),
  fullName: z.string({ message: 'Vui lòng nhập họ và tên người đại diện' }),
  position: z.string({ message: 'Vui lòng nhập chức vụ' }),
  addressService: z.string({
    message: 'Vui lòng nhập địa điểm cung cấp dịch vụ',
  }),
  supplierContracts: z.optional(
    z.array(
      z.object({
        id: z.string().optional(),
        contractCode: z.string().optional(),
        contractName: z.string().optional(),
        contractDate: z.string().optional(),
        endDate: z.string().optional(),
        status: z.string().optional(),
        createdAt: z.string().optional(),
        contractAmount: z.number().optional(),
      }),
    ),
  ),
  contacts: z.optional(
    z.array(
      z.object({
        id: z.string().nullable().optional(),
        contactTypeId: z.string().optional(),
        contactInfo: z.string().optional(),
        position: z.string().optional(),
        phone: z.string().optional(),
        email: z.string().optional(),
        birthDate: z.date().optional(),
      }),
    ),
  ),
  supplierTypeId: z.string().optional(),
  attachment: z
    .array(
      z.object({
        fileId: z.string().optional(),
        fileName: z.string().optional(),
        createdAt: z.date().optional(),
      }),
    )
    .optional(),
  attribute: z.optional(z.any()),
  type: z.optional(z.string()),
});

export type SupplierSchema = z.infer<typeof supplierSchema>;
