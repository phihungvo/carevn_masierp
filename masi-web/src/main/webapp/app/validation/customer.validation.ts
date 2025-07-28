import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';

const CUSTOMER_CODE_MESSAGE = 'Vui lòng nhập mã khách hàng';
const ADDRESS_MESSAGE = 'Vui lòng nhập địa chỉ';
const NAME_MESSAGE = 'Vui lòng nhập đầy đủ họ tên';
const BIRTHDAY_MESSAGE = 'Vui lòng nhập ngày sinh và hợp lệ';
const PHONE_NUMBER_MESSAGE = 'Vui lòng nhập số điện thoại';
const PHONE_NUMBER_VALID_MESSAGE = 'Vui lòng nhập số điện thoại hợp lệ';
const EMAIL_MESSAGE = 'Vui lòng nhập email hợp lệ';
const CUSTOMER_OWNER_MESSAGE = 'Vui lòng chọn người phụ trách';
const DATE_VALID_MESSAGE = 'Thời gian kết thúc phải sau thời gian bắt đầu';
const TRANSFER_CUSTOMER_MESSAGE = 'Vui lòng chọn nhân viên cần chuyển giao';

export const customerSchema = z
  .object({
    customerCode: z.string({ message: CUSTOMER_CODE_MESSAGE }).optional(),
    companyName: z
      .string({ message: 'Vui lòng nhập tên công ty' })
      .refine(value => value.trim() !== '', { message: 'Vui lòng nhập tên công ty' }),
    address: z.string().optional(),
    taxCode: z.any().optional(),
    // firstName: z.string({ message: NAME_MESSAGE }).refine(value => value.trim() !== '', { message: NAME_MESSAGE }),
    // lastName: z.string({ message: NAME_MESSAGE }).refine(value => value.trim() !== '', { message: NAME_MESSAGE }),
    fullName: z.string({ message: NAME_MESSAGE }).refine(value => value.trim() !== '', { message: NAME_MESSAGE }),
    birthday: z.custom<DateObject>().optional(),
    phoneNumber: z.string().optional(),
    // regex(VIETNAMESE_PHONE_NUMBER_REGEX, { message: PHONE_NUMBER_VALID_MESSAGE }),
    email: z
      .string({ message: EMAIL_MESSAGE })
      // .email({ message: EMAIL_MESSAGE })
      .refine(value => value.trim() !== '', { message: EMAIL_MESSAGE }),
    position: z.any().optional(),
    customerOwner: z.string({ message: CUSTOMER_OWNER_MESSAGE }),
    contractSigned: z.custom<DateObject>(value => isValidDateObject(value)).optional(),
    contractFrom: z.custom<DateObject>().optional(),
    contractTo: z.custom<DateObject>().optional(),
    note: z.any().optional(),
  })
  .refine(
    data => {
      if (!data.contractFrom || !data.contractTo) return true;
      return data.contractFrom?.toDate() <= data.contractTo?.toDate();
    },
    {
      message: DATE_VALID_MESSAGE,
      path: ['contractTo'],
    },
  );

export const customerTransferSchema = z.object({
  customerOwner: z.string({ message: TRANSFER_CUSTOMER_MESSAGE }),
});

export type CustomerFormSchema = z.infer<typeof customerSchema>;
export type CustomerTransferFormSchema = z.infer<typeof customerTransferSchema>;
