import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';
import { ORDER_REVIEW_SOLUTION } from 'app/shared/model/enumerations/order.model';

const ORDER_CODE_MESSAGE = 'Vui lòng nhập mã số';
// const NUMBER_ORDER_MESSAGE = 'Vui lòng nhập số lần ban hành';
const DATE_ORDER_MESSAGE = 'Vui lòng chọn ngày ban hành';
const CONTRACT_MESSAGE = 'Vui lòng chọn HĐ';
const PACKAGE_TYPE_MESSAGE = 'Vui lòng nhập quy cách đóng gói';
// const QUANTITY_MESSAGE = 'Vui lòng nhập số lượng';
// const PROTEIN_MESSAGE = 'Vui lòng nhập chỉ số đạm';
// const HUMIDITY_MESSAGE = 'Vui lòng nhập chỉ số độ ẩm';
// const ASHING_MESSAGE = 'Vui lòng nhập chỉ số tro';
// const FAT_MESSAGE = 'Vui lòng nhập chỉ số chất béo';
// const SALT_MESSAGE = 'Vui lòng nhập chỉ số muối';
// const TVN_MESSAGE = 'Vui lòng nhập chỉ số TVN';
// const IMPURITIES_MESSAGE = 'Vui lòng nhập chỉ số tạp chất tự nhiên từ hàng hóa';
const FINISH_DATE_MESSAGE = 'Vui lòng chọn ngày hoàn thành';
const APPROVAL_STATUS_NOTE_MESSAGE = 'Vui lòng nhập lý do';
const APPROVAL_SOLUTION_MESSAGE = 'Vui lòng nhập giải pháp';
const AWAITING_DATE_MESSAGE = 'Vui lòng chọn ngày chờ';
const DELIVERY_TERM_MESSAGE = 'Vui lòng chọn thời hạn giao hàng';
const PLEASE_APPROVEL_MESSAGE = 'Vui lòng chọn 2 người duyệt';

export const orderSchema = z
  .object({
    orderCode: z.string({ message: ORDER_CODE_MESSAGE }).refine(value => value.trim() !== '', { message: ORDER_CODE_MESSAGE }),
    // numberOrder: z.string({ message: NUMBER_ORDER_MESSAGE }).refine(
    //   value => {
    //     const floatValue = parseFloat(value);
    //     return !isNaN(floatValue) && floatValue >= 0;
    //   },
    //   {
    //     message: NUMBER_ORDER_MESSAGE,
    //   },
    // ),
    dateOrder: z.custom<DateObject>(value => isValidDateObject(value), { message: DATE_ORDER_MESSAGE }),
    contractId: z.string({ message: CONTRACT_MESSAGE }),
    packageType: z.string({ message: PACKAGE_TYPE_MESSAGE }).refine(value => value.trim() !== '', { message: PACKAGE_TYPE_MESSAGE }),
    finishDate: z.custom<DateObject>(value => isValidDateObject(value), { message: FINISH_DATE_MESSAGE }),
    deliveryTerm: z.custom<DateObject[]>(value => isValidDateObject(value?.[0]), { message: DELIVERY_TERM_MESSAGE }),
    payTerm: z
      .string({ message: 'Vui lòng nhập thời hạn thanh toán' })
      .refine(value => value.trim() !== '', { message: 'Vui lòng nhập thời hạn thanh toán' }),
    payCondition: z
      .string({ message: 'Vui lòng chọn điều kiện thanh toán' })
      .refine(value => value.trim() !== '', { message: 'Vui lòng chọn điều kiện thanh toán' }),
    deliveryLocation: z
      .string({ message: 'Vui lòng nhập địa điểm nhận hàng' })
      .refine(value => value.trim() !== '', { message: 'Vui lòng nhập địa điểm nhận hàng' }),
    note: z.any().optional(),
    qualityIndexes: z
      .array(
        z.object({
          id: z.string().optional(),
          name: z.string({ message: 'Vui lòng nhập chỉ tiêu' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập chỉ tiêu' }),
          value: z.string({ message: 'Vui lòng nhập giá trị' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập giá trị' }),
        }),
      )
      .optional(),
  })
  .refine(
    data => {
      if (!data.deliveryTerm) {
        return true;
      }

      const deliveryTermTo = data.deliveryTerm?.[1] ? data.deliveryTerm?.[1] : data.deliveryTerm?.[0];
      const diff = deliveryTermTo.toDate().getDate();
      const diffMonth = deliveryTermTo.toDate().getMonth();
      const diffYear = deliveryTermTo.toDate().getFullYear();
      if (diffYear < 0) {
        return false;
      }

      if (diffYear > 0) {
        return true;
      }

      if (diffMonth < 0) {
        return false;
      }

      if (diffMonth > 0) {
        return true;
      }

      return diff >= 0;
    },
    {
      message: 'Thời gian giao hàng phải ít nhất sớm hơn thời hạn hợp đồng',
      path: ['deliveryTerm'],
    },
  );

export const createOrderReviewSchema = z.object({
  documentId: z.string().optional(),
  employeesHCNS: z.array(z.any(), { message: PLEASE_APPROVEL_MESSAGE }).refine((data) => data?.length >= 2, { message: PLEASE_APPROVEL_MESSAGE }),
  employeesSALE: z.array(z.any(), { message: PLEASE_APPROVEL_MESSAGE }).refine((data) => data?.length >= 2, { message: PLEASE_APPROVEL_MESSAGE }),
  employeesWORKER: z.array(z.any(), { message: PLEASE_APPROVEL_MESSAGE }).refine((data) => data?.length >= 2, { message: PLEASE_APPROVEL_MESSAGE }),
  employeesLOGPUR: z.array(z.any(), { message: PLEASE_APPROVEL_MESSAGE }).refine((data) => data?.length >= 2, { message: PLEASE_APPROVEL_MESSAGE }),
});

export const patchOrderReviewSchema = z.union([
  z.object({
    approvalStatusNote: z
      .string({ message: APPROVAL_STATUS_NOTE_MESSAGE })
      .refine(value => value.trim() !== '', { message: APPROVAL_STATUS_NOTE_MESSAGE }),
    approvalSolution: z.nativeEnum(ORDER_REVIEW_SOLUTION).refine(value => value.trim() !== '', { message: APPROVAL_SOLUTION_MESSAGE }),
    awaitingDate: z.custom<DateObject>(value => isValidDateObject(value), { message: AWAITING_DATE_MESSAGE }).optional(),
  }),
  z.object({
    approvalStatusNote: z
      .string({ message: APPROVAL_STATUS_NOTE_MESSAGE })
      .refine(value => value.trim() !== '', { message: APPROVAL_STATUS_NOTE_MESSAGE }),
    approvalSolution: z.nativeEnum(ORDER_REVIEW_SOLUTION).refine(value => value.trim() !== '', { message: APPROVAL_SOLUTION_MESSAGE }),
    awaitingDate: z.custom<DateObject>(value => isValidDateObject(value), { message: AWAITING_DATE_MESSAGE }),
  }),
]);

export type OrderFormSchema = z.infer<typeof orderSchema>;
export type CreateOrderReviewFormSchema = z.infer<typeof createOrderReviewSchema>;
export type PatchOrderReviewFormSchema = z.infer<typeof patchOrderReviewSchema>;
