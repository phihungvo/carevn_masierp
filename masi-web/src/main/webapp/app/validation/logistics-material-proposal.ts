import { z } from 'zod';
import { DateObject } from 'react-multi-date-picker';

import { isValidDateObject } from './custom.validation';

const NUMBER_VOUCHER_MESSAGE = 'Vui lòng nhập số chứng từ';
const VOUCHER_DATE_MESSAGE = 'Vui lòng nhập ngày chứng từ';
const EMPLOYEE_ID_MESSAGE = 'Vui lòng chọn nhân viên';
const WORK_SPANCE_ID_MESSAGE = 'Vui lòng chọn bộ phận';
const PRODUCT_NAME_MESSAGE = 'Vui lòng nhập tên hàng hóa';
const UNITS_MESSAGE = 'Vui lòng chọn đơn vị';
const QUANTITY_MESSAGE = 'Vui lòng nhập số lượng';
const UNIT_PRICE_MESSAGE = 'Vui lòng nhập đơn giá';
const SUPPLIER_MESSAGE = 'Vui lòng nhập nhà cung cấp';
const NOTE_MESSAGE = 'Vui lòng nhập ghi chú';

export const logisticsMaterialProposalSchema = z.object({
    numberVoucher: z.string({ message: NUMBER_VOUCHER_MESSAGE }).refine(value => value.trim() !== '', { message: NUMBER_VOUCHER_MESSAGE }),
    voucherDate: z.custom<DateObject>(value => isValidDateObject(value), { message: VOUCHER_DATE_MESSAGE }),
    employeeId: z.string({ message: EMPLOYEE_ID_MESSAGE }).refine(value => value.trim() !== '', { message: EMPLOYEE_ID_MESSAGE }),
    workspaceId: z.string({ message: WORK_SPANCE_ID_MESSAGE }).refine(value => value.trim() !== '', { message: WORK_SPANCE_ID_MESSAGE }),
    productName: z.string({ message: PRODUCT_NAME_MESSAGE }).refine(value => value.trim() !== '', { message: PRODUCT_NAME_MESSAGE }),
    units: z.string({ message: UNITS_MESSAGE }).refine(value => value.trim() !== '', { message: UNITS_MESSAGE }),
    quantity: z.string({ message: QUANTITY_MESSAGE }).refine(value => value.trim() !== '', { message: QUANTITY_MESSAGE }),
    unitPrice: z.string({ message: UNIT_PRICE_MESSAGE }).refine(value => value.trim() !== '', { message: UNIT_PRICE_MESSAGE }),
    supplier: z.string({ message: SUPPLIER_MESSAGE }).refine(value => value.trim() !== '', { message: SUPPLIER_MESSAGE }),
    note: z.string({ message: NOTE_MESSAGE }).refine(value => value.trim() !== '', { message: NOTE_MESSAGE }),
});

export const logisticsMaterialProposalApproveSchema = z.object({
    employeeId1: z.string({ message: 'Vui lòng chọn người duyệt 1' }),
    employeeId2: z.string().optional(),
    employeeId3: z.string().optional(),
    employeeId4: z.string().optional(),
})

export type LogisticsMaterialProposalSchema = z.infer<typeof logisticsMaterialProposalSchema>;
export type LogisticsMaterialProposalApproveSchema = z.infer<typeof logisticsMaterialProposalApproveSchema>;