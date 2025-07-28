import { isValidDateObject } from 'app/validation/custom.validation';
import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';

const EMPLOYEE_ID_MESSAGE = 'Vui lòng chọn nhân viên';
const NUMBER_MONEY_MESSAGE = 'Vui lòng nhập số tiền';
const SUPPLIER_MESSAGE = 'Vui lòng chọn nhà cung cấp';

export const requestPaymentSchema = z.object({
  code: z.string().optional(),
  order: z.string().optional(),
  createdDate: z.custom<DateObject>(value => isValidDateObject(value), {
    message: 'Vui lòng chọn ngày lập',
  }),
  paymentDate: z.custom<DateObject>(value => isValidDateObject(value), {
    message: 'Vui lòng chọn ngày DN',
  }),
  note: z.string().optional(),
  content: z.string().optional(),
  status: z.string().optional(),
  supplierId: z.string({ message: SUPPLIER_MESSAGE }),
  supplierCodeName: z.string().optional(),
  workspaceName: z.string().optional(),
  departmentId: z.string().optional(),
  company: z.string().optional(),
  totalAmount: z.string().optional(),
  paidAmount: z.number().optional(),
  remainingAmount: z.number().optional(),
  paymentVoucher: z.string().optional(),
  paymentVoucherAmount: z.string().optional(),
  employeeId: z.string({ message: EMPLOYEE_ID_MESSAGE }),
  remainingBalance: z.number().optional(),
  overSpent: z.number().optional(),
  attachments: z
    .array(
      z.object({
        fileId: z.string().optional(),
        fileName: z.string().optional(),
        createdAt: z.date().optional(),
      }),
    )
    .optional(),
  requestApprovals: z
    .array(
      z.object({
        id: z.string().optional(),
        index: z.number().optional(),
        department: z.string().optional(),
        employeeId: z
          .string()
          .refine(value => value.trim() !== '', {
            message: 'Vui lòng chọn người ký',
          })
          .optional(),
        employee: z
          .object({
            code: z.string().optional(),
            fullName: z.string().optional(),
          })
          .optional(),
        updatedAt: z.string().optional(),
        approvedSign: z.string().optional(),
        approvedSignName: z.string().optional(),
        result: z.boolean().optional(),
      }),
    )
    .min(1, 'Vui lòng chọn người ký'),
  paymentDetails: z
    .array(
      z.object({
        id: z.string().optional(),
        new: z.boolean().optional(),
        invoiceId: z.string().optional(),
        incomingInvoice: z.object({
          invoiceNo: z.string().optional(),
          invoiceDate: z.string().optional(),
          content: z.string().optional(),
          totalAmount: z.string().optional(),
          currencyCode: z.string().optional(),
          currencyId: z.string().optional(),
          series: z.string().optional(),
          note: z.string().optional(),
          documentId: z.string().optional(),
        }),
      }),
      { message: 'Vui lòng chọn hóa đơn' },
    )
    .min(1, 'Vui lòng chọn hóa đơn')
    .superRefine((items, ctx) => {
      items.forEach((item, index) => {
        const value = item.incomingInvoice.totalAmount;

        const isLastItem = index === items.length - 1;
        if (items.length === 1 || !isLastItem) {
          if (value === '' || value === undefined) {
            ctx.addIssue({
              code: 'custom',
              path: [index, 'incomingInvoice', 'totalAmount'],
              message: 'Vui lòng nhập số tiền hợp lệ',
            });
          }
        }
      });
    }),
  createdBy: z.string().optional(),
  createdByName: z.string().optional(),
});

export const refundRequestSchema = z.object({
  code: z.string({
    message: 'Số CT không được để trống',
  }),
  order: z.string().optional(),
  createdDate: z.custom<DateObject>(value => isValidDateObject(value), {
    message: 'Vui lòng chọn ngày lập',
  }),
  paymentDate: z.custom<DateObject>(value => isValidDateObject(value), {
    message: 'Vui lòng chọn ngày CT',
  }),
  note: z.string().optional(),
  content: z.string().optional(),
  status: z.string().optional(),
  supplierId: z.string().optional(),
  supplierCodeName: z.string().optional(),
  workspaceName: z.string().optional(),
  departmentId: z.string().optional(),
  company: z.string().optional(),
  totalAmount: z.string().optional(),
  paidAmount: z.string().optional(),
  remainingAmount: z.string().optional(),
  paymentVoucher: z.string().optional(),
  paymentVoucherAmount: z.string().optional(),
  employeeId: z.string({ message: EMPLOYEE_ID_MESSAGE }),
  remainingBalance: z.string().optional(),
  overSpent: z.string().optional(),
  reimbursements: z
    .array(
      z.object({
        id: z.string().optional(),
        reimbursementId: z.string().optional(),
        advanceId: z.string().optional(),
        advancement: z
          .object({
            code: z.string().optional(),
            totalAmount: z.number().optional(),
            createdDate: z.string().optional(),
            note: z.string().optional(),
            content: z.string().optional(),
            paymentVoucher: z.string().optional(),
            paymentVoucherAmount: z.number().optional(),
          })
          .optional(),
      }),
    )
    .min(1, 'Vui lòng chọn phiếu tạm ứng'),
  attachments: z
    .array(
      z.object({
        fileId: z.string().optional(),
        fileName: z.string().optional(),
        createdAt: z.date().optional(),
      }),
    )
    .optional(),
  requestApprovals: z
    .array(
      z.object({
        id: z.string().optional(),
        index: z.number().optional(),
        department: z.string().optional(),
        employeeId: z
          .string()
          .refine(value => value.trim() !== '', {
            message: 'Vui lòng chọn người ký',
          })
          .optional(),
        employee: z
          .object({
            code: z.string().optional(),
            fullName: z.string().optional(),
          })
          .optional(),
        updatedAt: z.string().optional(),
        approvedSign: z.string().optional(),
        approvedSignName: z.string().optional(),
        result: z.boolean().optional(),
      }),
    )
    .min(1, 'Vui lòng chọn người ký'),
  paymentDetails: z
    .array(
      z.object({
        id: z.string().optional(),
        new: z.boolean().optional(),
        invoiceId: z.string().optional(),
        incomingInvoice: z.object({
          invoiceNo: z.string().optional(),
          invoiceDate: z.string().optional(),
          content: z.string().optional(),
          totalAmount: z.string().optional(),
          currencyCode: z.string().optional(),
          currencyId: z.string().optional(),
          series: z.string().optional(),
          note: z.string().optional(),
          documentId: z.string().optional(),
        }),
      }),
    )
    .min(1, 'Vui lòng chọn hóa đơn')
    .superRefine((items, ctx) => {
      items.forEach((item, index) => {
        const value = item.incomingInvoice.totalAmount;

        const isLastItem = index === items.length - 1;
        if (items.length === 1 || !isLastItem) {
          if (value === '' || value === undefined) {
            ctx.addIssue({
              code: 'custom',
              path: [index, 'incomingInvoice', 'totalAmount'],
              message: 'Vui lòng nhập số tiền hợp lệ',
            });
          }
        }
      });
    }),
  createdBy: z.string().optional(),
  createdByName: z.string().optional(),
});

export const advanceRequestSchema = z.object({
  code: z.string().optional(),
  order: z.string().optional(),
  createdDate: z.custom<DateObject>(value => isValidDateObject(value), {
    message: 'Vui lòng chọn ngày lập',
  }),
  paymentDate: z.custom<DateObject>(value => isValidDateObject(value), {
    message: 'Vui lòng chọn ngày CT',
  }),
  reimbursementDate: z.custom<DateObject>().optional(),
  note: z.string().optional(),
  content: z.string().optional(),
  status: z.string().optional(),
  supplierCodeName: z.string().optional(),
  workspaceName: z.string().optional(),
  departmentId: z.string().optional(),
  company: z.string().optional(),
  totalAmount: z.string({ message: NUMBER_MONEY_MESSAGE }),
  paidAmount: z.number().optional(),
  remainingAmount: z.number().optional(),
  paymentVoucher: z.string().optional(),
  paymentVoucherAmount: z.string().optional(),
  employeeId: z.string({ message: EMPLOYEE_ID_MESSAGE }),
  remainingBalance: z.number().optional(),
  overSpent: z.number().optional(),
  attachments: z
    .array(
      z.object({
        fileId: z.string().optional(),
        filePath: z.string().optional(),
        fileName: z.string().optional(),
        createdAt: z.date().optional(),
      }),
    )
    .optional(),
  requestApprovals: z
    .array(
      z.object({
        id: z.string().optional(),
        index: z.number().optional(),
        department: z.string().optional(),
        employeeId: z
          .string()
          .refine(value => value.trim() !== '', {
            message: 'Vui lòng chọn người ký',
          })
          .optional(),
        employee: z
          .object({
            code: z.string().optional(),
            fullName: z.string().optional(),
          })
          .optional(),
        updatedAt: z.string().optional(),
        approvedSign: z.string().optional(),
        approvedSignName: z.string().optional(),
        result: z.boolean().optional(),
      }),
    )
    .min(1, 'Vui lòng chọn người ký'),
  paymentDetails: z
    .array(
      z.object({
        id: z.string().optional(),
        new: z.boolean().optional(),
        invoiceId: z.string().optional(),
        incomingInvoice: z.object({
          invoiceNo: z.string().optional(),
          invoiceDate: z.string().optional(),
          content: z.string().optional(),
          totalAmount: z.number().optional(),
          currencyCode: z.string().optional(),
          currencyId: z.string().optional(),
          series: z.string().optional(),
          note: z.string().optional(),
          documentId: z.string().optional(),
        }),
      }),
    )
    .optional(),
  createdBy: z.string().optional(),
  createdByName: z.string().optional(),
});

export type RequestPaymentSchema = z.infer<typeof requestPaymentSchema>;
export type RefundRequestSchema = z.infer<typeof refundRequestSchema>;
export type AdvanceRequestSchema = z.infer<typeof advanceRequestSchema>;

export const requestPaymentFilterSchema = z.object({
  status: z.string().optional(),
  type: z.string().optional(),
  supplierId: z.string().optional(),
  paymentDate: z
    .array(z.custom<DateObject>(value => isValidDateObject(value)).optional())
    .optional(),
  employeeId: z.string().optional(),
  createdDate: z.custom<DateObject>().optional(),
});

export type RequestPaymentFilterSchema = z.infer<
  typeof requestPaymentFilterSchema
>;

export const requestPaymentFilterDateSchema = z.object({
  createdDateArr: z
    .array(z.custom<DateObject>(value => isValidDateObject(value)).optional())
    .optional(),
  startDate: z.custom<DateObject>(value => isValidDateObject(value)).optional(),
  endDate: z.custom<DateObject>(value => isValidDateObject(value)).optional(),
  createdDate: z
    .custom<DateObject>(value => isValidDateObject(value))
    .optional(),
  paymentDate: z
    .custom<DateObject>(value => isValidDateObject(value))
    .optional(),
  employeeId: z.string().optional(),
});

export type RequestPaymentFilterDateSchema = z.infer<
  typeof requestPaymentFilterDateSchema
>;
