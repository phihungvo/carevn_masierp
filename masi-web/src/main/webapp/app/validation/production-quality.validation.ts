import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';

const SAMPLING_DATE_MESSAGE = 'Vui lòng chọn ngày lấy mẫu';
const SAMPLE_NO_MESSAGE = 'Vui lòng nhập mã mẫu';
const PRODUCT_TYPE_MESSAGE = 'Vui lòng nhập loại hàng';
const SAMPLE_WEIGHT_MESSAGE = 'Vui lòng nhập số lượng mẫu/khối lượng';
const CUSTOMER_MESSAGE = 'Vui lòng nhập khách hàng';
const INTERNAL_HUM_MESSAGE = 'Vui lòng nhập độ ẩm nội bộ';
const INTERNAL_TVN_MESSAGE = 'Vui lòng nhập TVN nội bộ';
const INTERNAL_ASH_MESSAGE = 'Vui lòng nhập tro nội bộ';
const INTERNAL_PROTEIN_MESSAGE = 'Vui lòng nhập protein nội bộ';
const EXTERNAL_HUM_MESSAGE = 'Vui lòng nhập độ ẩm đối tác';
const EXTERNAL_TVN_MESSAGE = 'Vui lòng nhập TVN đối tác';
const EXTERNAL_ASH_MESSAGE = 'Vui lòng nhập tro đối tác';
const EXTERNAL_PROTEIN_MESSAGE = 'Vui lòng nhập protein đối tác';
const SAMPLING_EMPLOYEE_MESSAGE = 'Vui lòng chọn người lưu mẫu';
const PRODUCTION_COMMAND_MESSAGE = 'Vui lòng chọn lệnh sản xuất';
const PACKAGES_ID = 'Vui lòng chọn mã đóng gói';

// Lập đơn huỷ mẫu kiểm thử
const REQUEST_DATE_MESSAGE = 'Vui lòng nhập ngày tạo đơn';
const INVOLVE_EMPLOYEE_NAME_MESSAGE = 'Vui lòng nhập họ tên';
const POSITION_MESSAGE = 'Vui lòng nhập chức vụ';
const DISPOSAL_NOTE_MESSAGE = 'Vui lòng nhập lý do';
const DISPOSAL_NOTE_MESSAGE_LENGTH = 'Lý do không được vượt quá 200 ký tự';
const QUANTITY_STT_MESSAGE = 'Vui lòng nhập STT';
const QUANTITY_SAMPLE_NAME_MESSAGE = 'Vui lòng nhập tên mẫu/hàng';
const QUANTITY_SAMPLE_CODE_MESSAGE = 'Vui lòng nhập mã mẫu/hàng';
const QUANTITY_MESSAGE = 'Vui lòng nhập số lượng';
const QUANTITY_SAVE_DATE_MESSAGE = 'Vui lòng nhập ngày lưu';
const QUANTITY_RELEASE_DATE_MESSAGE = 'Vui lòng chọn ngày xả mẫu';
const DISPOSAL_METHOD_MESSAGE = 'Vui lòng nhập phương pháp huỷ';
const DISPOSAL_RESULT_MESSAGE = 'Vui lòng nhập kết quả huỷ';
const REVIEWER_MESSAGE = 'Vui lòng chọn người phê duyệt';

// Xác nhận huỷ mẫu kiểm thử
const DISPOSAL_REVIEW_NOTE = 'Vui lòng nhập lý do';
const DISPOSAL_REVIEW_NOTE_MAX_MESSAGE =
  'Ghi chú không được vượt quá 200 ký tự';

export const productionQualitySchema = z.object({
  samplingDate: z.custom<DateObject>(value => isValidDateObject(value), {
    message: SAMPLING_DATE_MESSAGE,
  }),
  sampleNo: z
    .string({ message: SAMPLE_NO_MESSAGE })
    .refine(value => value.trim() !== '', { message: SAMPLE_NO_MESSAGE }),
  productType: z
    .string({ message: PRODUCT_TYPE_MESSAGE })
    .refine(value => value.trim() !== '', { message: PRODUCT_TYPE_MESSAGE }),
  sampleWeight: z
    .string({ message: SAMPLE_WEIGHT_MESSAGE })
    .refine(value => value.trim() !== '', { message: SAMPLE_WEIGHT_MESSAGE })
    .refine(
      value => {
        const floatValue = parseFloat(value);
        return !isNaN(floatValue) && floatValue >= 0;
      },
      {
        message: SAMPLE_WEIGHT_MESSAGE,
      },
    ),
  customer: z.string().optional(),
  reason: z.string().optional(),
  sampleReleaseDate: z.custom<DateObject>().optional(),
  internalHum: z.string({ message: INTERNAL_HUM_MESSAGE }),
  internalTvn: z.string({ message: INTERNAL_TVN_MESSAGE }),
  internalAsh: z.string({ message: INTERNAL_ASH_MESSAGE }),
  internalProtein: z.string({ message: INTERNAL_PROTEIN_MESSAGE }),
  externalHum: z.string({ message: EXTERNAL_HUM_MESSAGE }),
  externalTvn: z.string({ message: EXTERNAL_TVN_MESSAGE }),
  externalAsh: z.string({ message: EXTERNAL_ASH_MESSAGE }),
  externalProtein: z.string({ message: EXTERNAL_PROTEIN_MESSAGE }),
  samplingEmployeeId: z.string({ message: SAMPLING_EMPLOYEE_MESSAGE }),
  manufactureOrderId: z
    .string({ message: PRODUCTION_COMMAND_MESSAGE })
    .refine(value => value.trim() !== '', {
      message: PRODUCTION_COMMAND_MESSAGE,
    }),

  packageId: z
    .string({ message: PACKAGES_ID })
    .refine(value => value.trim() !== '', { message: PACKAGES_ID }),

  status: z.string().optional(),

  itemId: z.string().optional(),
  proteinPercentageApply: z
    .string()
    .optional()
    .refine(
      value => {
        if (!value) return true;

        if (!/^(\d+(\.\d{1,2})?)?$/.test(value)) return false;

        const floatValue = parseFloat(value);
        return !isNaN(floatValue) && floatValue > 0 && floatValue <= 100;
      },
      { message: 'Số đạm không hợp lệ' },
    ),

  statusM: z.string().optional(),
  attributes: z.object({ isDone: z.boolean().optional() }).optional(),
});

export const productionQualityCancelSchema = z.object({
  id: z.string().optional(),
  requestDate: z.custom<DateObject>(value => isValidDateObject(value), {
    message: REQUEST_DATE_MESSAGE,
  }),
  disposalNote: z.string({ message: DISPOSAL_NOTE_MESSAGE }),
  quantitySampleNo: z.string({ message: QUANTITY_SAMPLE_CODE_MESSAGE }),
  quantitySaveDate: z.custom<DateObject>(value => isValidDateObject(value), {
    message: QUANTITY_SAVE_DATE_MESSAGE,
  }),
  quantityReleaseDate: z.custom<DateObject>(value => isValidDateObject(value), {
    message: QUANTITY_RELEASE_DATE_MESSAGE,
  }),
  disposalMethod: z.string({ message: 'Vui lòng nhập phương pháp hủy' }),
  disposalResult: z.string().optional(),
  reviewerId: z.string({ message: REVIEWER_MESSAGE }),
  requesterId: z.string({ message: 'Chọn người tạo đơn' }),
});

export const qualityDisposalReviewSchema = z.object({
  reviewerNote: z.string().optional(),
  reviewerSign: z.string().optional(),
  reviewerSignFile: z.string().optional(),
});

export type ProductionQualityFormSchema = z.infer<
  typeof productionQualitySchema
>;
export type ProductionQualityCancelFormSchema = z.infer<
  typeof productionQualityCancelSchema
>;
export type QualityDisposalReviewFormSchema = z.infer<
  typeof qualityDisposalReviewSchema
>;
