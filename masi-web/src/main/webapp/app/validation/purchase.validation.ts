import { PURCHASE_STATUS, PURCHASE_UNIT } from 'app/shared/model/enumerations/purchase.model';
import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';

const PRODUCT_NAME_MESSAGE = 'Vui lòng nhập tên hàng hóa';
const UNIT_MESSAGE = 'Vui lòng chọn đơn vị';
const QUANTITY_MESSAGE = 'Vui lòng nhập số lượng';
const UNIT_PRICE_MESSAGE = 'Vui lòng nhập đơn giá';
const CREATE_DATE_MESSAGE = 'Vui lòng chọn ngày tạo';
const DELIVERIED_MESSAGE = 'Vui lòng nhập số lượng đã giao';
const APPROVAL_STATUS_NOTE_MESSAGE = 'Vui lòng nhập lý do';

export const purchaseSchema = z.object({
  productName: z.string({ message: PRODUCT_NAME_MESSAGE }).refine(value => value.trim() !== '', { message: PRODUCT_NAME_MESSAGE }),
  totalPrice: z.string().optional(),
  unit: z.nativeEnum(PURCHASE_UNIT, { message: UNIT_MESSAGE }),
  supplier: z.string().optional(),
  quantity: z.string({ message: QUANTITY_MESSAGE }).refine(
    value => {
      const floatValue = parseFloat(value);
      return !isNaN(floatValue) && floatValue >= 0;
    },
    {
      message: QUANTITY_MESSAGE,
    },
  ),
  createDate: z.custom<DateObject>(value => isValidDateObject(value), { message: CREATE_DATE_MESSAGE }),
  unitPrice: z.string({ message: UNIT_PRICE_MESSAGE }).refine(
    value => {
      const floatValue = parseFloat(value);
      return !isNaN(floatValue) && floatValue >= 0;
    },
    {
      message: UNIT_PRICE_MESSAGE,
    },
  ),
  note: z.any().optional(),
  requestStatus: z.nativeEnum(PURCHASE_STATUS).optional(),
});

export const purchaseDeliverySchema = z.object({
  delivered: z.string({ message: DELIVERIED_MESSAGE }).refine(
    value => {
      const floatValue = parseFloat(value);
      return !isNaN(floatValue) && floatValue >= 0;
    },
    {
      message: DELIVERIED_MESSAGE,
    },
  ),
  waitingDelivery: z.string().optional(),
});

export const createPurchaseReviewSchema = z.object({
  documentId: z.string().optional(),
  employeeId1: z.string({ message: 'Vui lòng chọn người duyệt 1' }),
  employeeId2: z.string().optional(),
  employeeId3: z.string().optional(),
  employeeId4: z.string().optional(),
});

export const patchPurchaseReviewSchema = z.object({
  approvalStatusNote: z
    .string({ message: APPROVAL_STATUS_NOTE_MESSAGE })
    .refine(value => value.trim() !== '', { message: APPROVAL_STATUS_NOTE_MESSAGE }),
});

export type PurchaseFormSchema = z.infer<typeof purchaseSchema>;
export type PurchaseDeliveryFormSchema = z.infer<typeof purchaseDeliverySchema>;
export type CreatePurchaseReviewFormSchema = z.infer<typeof createPurchaseReviewSchema>;
export type PatchPurchaseReviewFormSchema = z.infer<typeof patchPurchaseReviewSchema>;
