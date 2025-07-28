import dayjs from 'dayjs';
import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';

// Sub-schemas for nested objects

const validateTableVerify = z.array(
  z
    .object({
      title: z.string().optional(),
      pass: z.boolean().optional(),
      fail: z.boolean().optional(),
      reason: z.string().optional(),
    })
    .refine(
      ({ fail, pass }) => {
        if (fail === false && pass === false) return false;
        return true;
      },
      { message: 'Vui lòng chọn kết quả', path: ['title'] },
    )
    .refine(
      ({ fail, reason }) => {
        if (fail === true && !reason) return false;
        return true;
      },
      { message: 'Vui lòng nhập lý do', path: ['reason'] },
    ),
  { message: 'Vui lòng chọn kết quả' },
);

export const rawMaterialSchema = z.object({
  id: z.string().optional(),
  inspectionDate: z
    .string({ message: 'Vui lòng chọn ngày giờ kiểm tra' })
    .transform(date => new Date(date)),
  inspectionTime: z.any().optional(),
  inspectorId: z
    .string({ message: 'Vui lòng chọn người tiếp nhận' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng chọn người tiếp nhận',
    }),

  volume: z
    .string({ message: 'Vui lòng nhập số phiếu cân' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập số phiếu cân',
    }),
  weight: z
    .string({ message: 'Vui lòng nhập khối lượng nguyên liệu' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập khối lượng nguyên liệu',
    }),
  fishHead: z
    .string({ message: 'Vui lòng nhập nguyên liệu đầu cá' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập nguyên liệu đầu cá',
    }),
  freshFish: z
    .string({ message: 'Vui lòng nhập nguyên liệu cá tươi' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập nguyên liệu cá tươi',
    }),

  attributes: validateTableVerify,
  notes: z.string().optional(),
});

export const rawMaterial2Schema = z.object({
  mixingDate: z.custom<DateObject>(value => isValidDateObject(value), {
    message: 'Vui lòng chọn ngày trộn',
  }),
  manufactureDate: z.custom<DateObject>(value => isValidDateObject(value), {
    message: 'Vui lòng chọn ngày sản xuất',
  }),
  items: z
    .array(
      z
        .object({
          id: z.string().optional(),
          productionMaintainId: z
            .string({ message: 'Vui lòng chọn lô hàng' })
            .refine(value => value.trim() !== '', {
              message: 'Vui lòng chọn lô hàng',
            }),
          percentProtein: z.string().optional(),
          material: z.string().optional(),
          quantity: z.string().optional(),
          quantityUse: z
            .string({ message: 'Vui lòng nhập KL sử dụng' })
            .refine(value => value.trim() !== '', {
              message: 'Vui lòng nhập KL sử dụng',
            }),
        })
        .refine(
          ({ quantity, quantityUse }) => {
            if (Number(quantity ?? 0) < Number(quantityUse ?? 0)) return false;
            return true;
          },
          { message: 'KL sử dụng không hợp lệ', path: ['quantityUse'] },
        ),
    )
    .optional(),
});

export const additivesSchema = z.object({
  id: z.string().optional(),
  inspectionDate: z
    .string()
    .transform(date => new Date(date))
    .optional(),
  inspectionTime: z
    .string({ message: 'Vui lòng chọn ngày giờ kiểm tra' })
    .transform(date => new Date(date)),
  inspectorId: z
    .string({ message: 'Vui lòng chọn người thực hiện' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng chọn lô hàng',
    }),
  status: z.string().optional(),
  attributes: validateTableVerify,
  batchNumber: z
    .string({ message: 'Vui lòng nhập số phiếu cân' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập số phiếu cân',
    }),
  sodiumMaterial: z
    .string({ message: 'Vui lòng nhập khối lượng nguyên liệu' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập khối lượng nguyên liệu',
    }),
  sodiumMaterialUom: z.string().optional(),
  sodiumCarbonateBatch: z
    .string({ message: 'Vui lòng nhập số lô Natri bicacbonat' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập số lô Natri bicacbonat',
    }),
  sodiumCarbonateWeight: z
    .string({ message: 'Vui lòng nhập khối lượng Natri cacbonat' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập khối lượng Natri cacbonat',
    }),
  sodiumCarbonateUom: z.string().optional(),
  sodiumBicarbonateWeight: z
    .string({ message: 'Vui lòng nhập khối lượng Natri biocacbonat' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập khối lượng Natri biocacbonat',
    }),
  sodiumBicarbonateBatch: z
    .string({ message: 'Vui lòng nhập khối lượng Natri biocacbonat' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập khối lượng Natri biocacbonat',
    }),
  sodiumBicarbonateBatchUom: z.string().optional(),
  bhtWeight: z
    .string({ message: 'Vui lòng nhập số lô BHT' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập số lô BHT',
    }),
  bhtBatch: z
    .string({ message: 'Vui lòng nhập khối lượng BHT' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập khối lượng BHT',
    }),
  bhtUom: z.string().optional(),
  notes: z.string().optional(),
});

export const additives2Schema = z.object({
  id: z.string().optional(),
  inspectionDate: z
    .string()
    .transform(date => new Date(date))
    .optional(),
  inspectionTime: z
    .string({ message: 'Vui lòng chọn ngày giờ kiểm tra' })
    .transform(date => new Date(date)),
  inspectorId: z
    .string({ message: 'Vui lòng chọn người thực hiện' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng chọn lô hàng',
    }),
  status: z.string().optional(),
  batchNumber: z
    .string({ message: 'Vui lòng nhập số phiếu cân' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập số phiếu cân',
    }),
  sodiumMaterial: z
    .string({ message: 'Vui lòng nhập khối lượng nguyên liệu' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập khối lượng nguyên liệu',
    }),
  sodiumMaterialUom: z.string().optional(),
  sodiumCarbonateBatch: z
    .string({ message: 'Vui lòng nhập số lô Natri bicacbonat' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập số lô Natri bicacbonat',
    }),
  sodiumCarbonateWeight: z
    .string({ message: 'Vui lòng nhập khối lượng Natri cacbonat' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập khối lượng Natri cacbonat',
    }),
  sodiumCarbonateUom: z.string().optional(),
  sodiumBicarbonateWeight: z
    .string({ message: 'Vui lòng nhập khối lượng Natri biocacbonat' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập khối lượng Natri biocacbonat',
    }),
  sodiumBicarbonateBatch: z
    .string({ message: 'Vui lòng nhập số lô Natri biocacbonat' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập số lô Natri biocacbonat',
    }),
  sodiumBicarbonateBatchUom: z.string().optional(),
  bhtWeight: z
    .string({ message: 'Vui lòng nhập số lô BHT' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập số lô BHT',
    }),
  bhtBatch: z
    .string({ message: 'Vui lòng nhập khối lượng BHT' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập khối lượng BHT',
    }),
  bhtUom: z.string().optional(),
  notes: z.string().optional(),
});

export const productionStep1Schema = z.object({
  inspectionDate: z
    .string()
    .transform(date => new Date(date))
    .optional(),
  inspectionTime: z
    .string({ message: 'Vui lòng chọn ngày giờ kiểm tra' })
    .transform(date => new Date(date)),
  manufactureDate: z.custom<DateObject>(value => isValidDateObject(value), {
    message: 'Vui lòng chọn ngày sản xuất',
  }),

  furnace: validateTableVerify,
  dryingFurnace: validateTableVerify,
});

export const productionStep2Schema = z.object({
  inspectionDate: z
    .string()
    .transform(date => new Date(date))
    .optional(),
  inspectionTime: z
    .string({ message: 'Vui lòng chọn ngày giờ kiểm tra' })
    .transform(date => new Date(date)),
  manufactureDate: z.custom<DateObject>(value => isValidDateObject(value), {
    message: 'Vui lòng chọn ngày sản xuất',
  }),

  volume: z
    .string({ message: 'Vui lòng nhập số phiếu cân' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập số phiếu cân',
    }),
  note: z.string().optional(),

  steamer: z.object({
    testTime: z
      .string({ message: 'Vui lòng nhập thông số áp suất' })
      .refine(value => value.trim() !== '', {
        message: 'Vui lòng nhập thông số áp suất',
      }),
    pressure: z
      .string({ message: 'Vui lòng nhập thông số nhiệt độ' })
      .refine(value => value.trim() !== '', {
        message: 'Vui lòng nhập thông số nhiệt độ',
      }),
    temperature: z
      .string({ message: 'Vui lòng nhập thông số thời gian' })
      .refine(value => value.trim() !== '', {
        message: 'Vui lòng nhập thông số thời gian',
      }),
  }),
  dryer1: z.object({
    testTime: z
      .string({ message: 'Vui lòng nhập thông số áp suất' })
      .refine(value => value.trim() !== '', {
        message: 'Vui lòng nhập thông số áp suất',
      }),
    pressure: z
      .string({ message: 'Vui lòng nhập thông số nhiệt độ' })
      .refine(value => value.trim() !== '', {
        message: 'Vui lòng nhập thông số nhiệt độ',
      }),
    temperature: z
      .string({ message: 'Vui lòng nhập thông số thời gian' })
      .refine(value => value.trim() !== '', {
        message: 'Vui lòng nhập thông số thời gian',
      }),
  }),
  dryer2: z.object({
    testTime: z
      .string({ message: 'Vui lòng nhập thông số áp suất' })
      .refine(value => value.trim() !== '', {
        message: 'Vui lòng nhập thông số áp suất',
      }),
    pressure: z
      .string({ message: 'Vui lòng nhập thông số nhiệt độ' })
      .refine(value => value.trim() !== '', {
        message: 'Vui lòng nhập thông số nhiệt độ',
      }),
    temperature: z
      .string({ message: 'Vui lòng nhập thông số thời gian' })
      .refine(value => value.trim() !== '', {
        message: 'Vui lòng nhập thông số thời gian',
      }),
  }),
});

export const productionStep3Schema = z.object({
  inspectionDate: z
    .string()
    .transform(date => new Date(date))
    .optional(),
  inspectionTime: z.custom<DateObject>(value => isValidDateObject(value), {
    message: 'Vui lòng chọn ngày giờ kiểm tra',
  }),
  manufactureDate: z.custom<DateObject>(value => isValidDateObject(value), {
    message: 'Vui lòng chọn ngày sản xuất',
  }),

  manufactureBy: z
    .string({ message: 'Vui lòng chọn người kiểm tra' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng chọn người kiểm tra',
    }),
  examiner: z
    .string({ message: 'Vui lòng chọn người thẩm tra' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng chọn người thẩm tra',
    }),

  description: z
    .string({ message: 'Vui lòng nhập mô tả thành phẩm' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập mô tả thành phẩm',
    }),
  note: z.string().optional(),

  magnet: validateTableVerify,
  floorGrid: validateTableVerify,
  grindingGrid: validateTableVerify,
});

export const productionSchema = z.object({
  id: z.string().optional(),

  monitorMachineOperation: productionStep1Schema.optional(),
  steamDryingMonitoring: productionStep2Schema.optional(),
  checkMagnetGrid: productionStep3Schema.optional(),

  attributes: z
    .object({
      step: z.number().optional(),
      stepDone: z.number().optional(),
    })
    .optional(),
});

export const production2Schema = z
  .object({
    fromDate: z
      .string({ message: 'Vui lòng chọn ngày giờ bắt đầu' })
      .transform(date => new Date(date)),
    toDate: z
      .string({ message: 'Vui lòng chọn ngày giờ kết thúc' })
      .transform(date => new Date(date)),
    employeeId: z
      .string({ message: 'Vui lòng chọn nhân viên phụ trách' })
      .refine(value => value.trim() !== '', {
        message: 'Vui lòng chọn nhân viên phụ trách',
      }),
    note: z.string().optional(),
  })
  .superRefine((data, ctx) => {
    if (dayjs(data?.fromDate) > dayjs(data?.toDate)) {
      ctx.addIssue({
        code: 'custom',
        path: ['toDate'],
        message: 'Ngày giờ kết thúc không hợp lệ',
      });
    }
  });

export const productionPackagingSchema = z.object({
  id: z.string().optional(),
  packageCode: z
    .string({ message: 'Vui lòng nhập mã đóng gói' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập mã đóng gói',
    }),
  name: z.string().optional(),
  packageAt: z.custom<DateObject>(value => isValidDateObject(value), {
    message: 'Vui lòng chọn ngày đóng gói',
  }),
  weight: z.string().optional(),
  packageBy: z
    .string({ message: 'Vui lòng chọn người đóng gói' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng chọn người đóng gói',
    }),
  quantity: z
    .string({ message: 'Vui lòng nhập số bao ' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập số bao',
    }),
  note: z.string().optional(),
  status: z
    .string({ message: 'Vui lòng chọn trạng thái' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng chọn trạng thái',
    }),
});

export const qualityCheckSampleSchema = z.object({
  id: z.string().optional(),
  samplingDate: z.custom<DateObject>(value => isValidDateObject(value), {
    message: 'Vui lòng chọn ngày lấy mẫu',
  }),
  sampleNo: z.string({ message: 'Vui lòng nhập mã mẫu' }),
  productType: z.string({ message: 'Vui lòng nhập loại hàng' }),
  sampleWeight: z
    .string({ message: 'Vui lòng nhập SL mẫu/KL' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập SL mẫu/KL',
    })
    .refine(
      value => {
        const floatValue = parseFloat(value);
        return !isNaN(floatValue) && floatValue >= 0;
      },
      {
        message: 'Vui lòng nhập SL mẫu/KL',
      },
    ),
  customer: z
    .string({ message: 'Vui lòng chọn khách hàng' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng chọn khách hàng',
    }),
  reason: z.string().optional(),
  sampleReleaseDate: z.custom<DateObject>().optional(),
  internalHum: z
    .string({ message: 'Vui lòng nhập độ ẩm' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập độ ẩm' }),
  internalTvn: z
    .string({ message: 'Vui lòng nhập TVN' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập TVN' }),
  internalAsh: z
    .string({ message: 'Vui lòng nhập tro' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập tro' }),
  internalProtein: z
    .string({ message: 'Vui lòng nhập protein' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập protein' }),
  externalHum: z
    .string({ message: 'Vui lòng nhập độ ẩm' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập độ ẩm' }),
  externalTvn: z
    .string({ message: 'Vui lòng nhập TVN' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập TVN' }),
  externalAsh: z
    .string({ message: 'Vui lòng nhập tro' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập tro' }),
  externalProtein: z
    .string({ message: 'Vui lòng nhập protein' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập protein' }),
  samplingEmployeeId: z
    .string({ message: 'Vui lòng chọn người lưu mẫu' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng chọn người lưu mẫu',
    }),
  note: z.string().optional(),
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
  itemId: z.string().optional(),

  status: z.string().optional(),
  disposal: z.object({
    id: z.string().optional(),
    qualitySampleCheckId: z.string().optional(),
    requestDate: z.string().optional(),
    involveEmployee: z.string().optional(),
    position: z.string().optional(),
    disposalNote: z.string().optional(),
    quantitySampleName: z.string().optional(),
    quantitySampleNo: z.string().optional(),
    quantitySaveDate: z.string().optional(),
    quantityReleaseDate: z.string().optional(),
    disposalMethod: z.string().optional(),
    disposalResult: z.string().optional(),
    reviewerId: z.string().optional(),
    requesterId: z.string().optional(),
    reviewerNote: z.string().optional(),
  }),

  attributes: z.object({ isDone: z.boolean().optional() }).optional(),
});

export const productionBatchSchema = z
  .object({
    id: z.string().optional(),
    productBatchCode: z
      .string({ message: 'Vui lòng nhập mã lô hàng' })
      .refine(value => value.trim() !== '', {
        message: 'Vui lòng nhập mã lô hàng',
      }),
    productBatchName: z
      .string({ message: 'Vui lòng nhập lô hàng' })
      .refine(value => value.trim() !== '', {
        message: 'Vui lòng nhập lô hàng',
      }),
    manufactureDate: z.custom<DateObject>(value => isValidDateObject(value), {
      message: 'Vui lòng chọn ngày sản xuất',
    }),
    expiredDate: z.custom<DateObject>(value => isValidDateObject(value), {
      message: 'Vui lòng chọn thời hạn sử dụng đến',
    }),
  })
  .superRefine((data, ctx) => {
    if (
      dayjs(data?.manufactureDate.toDate()) > dayjs(data?.expiredDate.toDate())
    ) {
      ctx.addIssue({
        code: 'custom',
        path: ['expiredDate'],
        message: 'Thời hạn sử dụng không hợp lệ',
      });
    }
  });

export const productionSaveInventorySchema = z.object({
  id: z.string().optional(),
  code: z.string().optional(),
  name: z
    .string({ message: 'Vui lòng nhập mã lưu kho' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập mã lưu kho',
    }),
  storageId: z
    .string({ message: 'Vui lòng chọn nơi lưu trữ' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng chọn nơi lưu trữ',
    }),
  warehouseDate: z.custom<DateObject>(value => isValidDateObject(value), {
    message: 'Vui lòng chọn ngày lưu kho',
  }),
});

// Main schema
export const manufactureOrderSchema = z.object({
  id: z.string().optional(),
  name: z.string().optional(),
  orderId: z.string().optional(),

  orderCode: z.string().optional(),
  orderDeliveryDate: z.date().optional(),

  updatedAt: z.date().optional(),

  itemId: z.string().optional(),
  itemName: z.string().optional(),
  itemPercentProtein: z.number().optional(),

  fromDate: z.custom<DateObject>().optional(),
  toDate: z.custom<DateObject>().optional(),

  note: z.string().optional(),
  status: z.string().optional(),
  typePage: z.string().optional(),

  productionStandardId: z.string().optional(),
  productionStandardName: z.string().optional(),
  productionStandardDueDate: z.string().optional(),

  productionQuantity: z.number().optional(),
  percentProtein: z.string().optional(),

  rawMaterial: rawMaterialSchema.optional(),
  rawMaterial2: rawMaterial2Schema.optional(),
  additives: additivesSchema.optional(),
  production: productionSchema.optional(),
  production2: production2Schema.optional(),
  productionPackaging: productionPackagingSchema.optional(),
  productionQuality: qualityCheckSampleSchema.optional(),
  productionBatch: productionBatchSchema.optional(),
  productionSaveInventory: productionSaveInventorySchema.optional(),
});

const START_DATE_MESSAGE = 'Vui lòng chọn ngày bắt đầu';
const DUE_DATE_MESSAGE = 'Vui lòng chọn ngày kết thúc';

export const manufactureOrderByOrderCreateSchema = z
  .object({
    id: z.string().optional(),
    code: z
      .string({ message: 'Vui lòng nhâp mã sản xuất' })
      .refine(value => value.trim() !== '', {
        message: 'Vui lòng nhâp mã sản xuất',
      }),
    name: z.string().optional(),
    orderId: z
      .string({ message: 'Vui lòng chọn đơn đặt hàng' })
      .refine(value => value.trim() !== '', {
        message: 'Vui lòng chọn đơn đặt hàng',
      }),
    orderCustomerName: z.string().optional(),
    orderDeliveryDate: z.custom<DateObject>(value => isValidDateObject(value), {
      message: 'Vui lòng chọn thời hạn giao hàng',
    }),
    itemId: z
      .string({ message: 'Vui lòng chọn mặt hàng' })
      .refine(value => value.trim() !== '', {
        message: 'Vui lòng chọn mặt hàng',
      }),

    items: z
      .array(
        z.object({
          itemId: z.string().optional(),
          itemName: z.string().optional(),
          percentProtein: z.number().optional(),
          quantity: z.number().optional(),
          parameter: z.string().optional(),
        }),
      )
      .optional(),

    fromDate: z.custom<DateObject>(value => isValidDateObject(value), {
      message: START_DATE_MESSAGE,
    }),
    toDate: z.custom<DateObject>(value => isValidDateObject(value), {
      message: DUE_DATE_MESSAGE,
    }),

    note: z.string().optional(),
    status: z.string().optional(),
    productionQuantity: z
      .string({ message: 'Khối lượng không được để trống' })
      .refine(value => value.trim() !== '', {
        message: 'Khối lượng không được để trống',
      }),
    createdBy: z.string().optional(),
    createdAt: z.date().optional(),
  })
  .superRefine((data, ctx) => {
    if (dayjs(data?.fromDate.toDate()) > dayjs(data?.toDate.toDate())) {
      ctx.addIssue({
        code: 'custom',
        path: ['toDate'],
        message: 'Ngày kết thúc không hợp lệ',
      });
    }
  });

export const manufactureOrderByStandardCreateSchema = z
  .object({
    id: z.string().optional(),
    code: z
      .string({ message: 'Vui lòng nhâp mã sản xuất' })
      .refine(value => value.trim() !== '', {
        message: 'Vui lòng nhâp mã sản xuất',
      }),
    name: z.string().optional(),
    productionStandardId: z
      .string({ message: 'Vui lòng chọn đơn đặt hàng' })
      .refine(value => value.trim() !== '', {
        message: 'Vui lòng chọn đơn đặt hàng',
      }),
    dueDate: z.string().optional(),
    productionQuantity: z.string().optional(),

    fromDate: z.custom<DateObject>(value => isValidDateObject(value), {
      message: START_DATE_MESSAGE,
    }),
    toDate: z.custom<DateObject>(value => isValidDateObject(value), {
      message: DUE_DATE_MESSAGE,
    }),

    note: z.string().optional(),
    status: z.string().optional(),

    createdBy: z.string().optional(),
    createdAt: z.date().optional(),
  })
  .superRefine((data, ctx) => {
    if (dayjs(data?.fromDate.toDate()) > dayjs(data?.toDate.toDate())) {
      ctx.addIssue({
        code: 'custom',
        path: ['toDate'],
        message: 'Ngày kết thúc không hợp lệ',
      });
    }
  });

export const modalQualityCancelSampleSchema = z.object({
  createdAt: z.custom<DateObject>(value => isValidDateObject(value), {
    message: 'Vui lòng chọn ngày tạo đơn',
  }),
  createdBy: z
    .string({ message: 'Vui lòng chọn người tạo' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng chọn người tạo',
    }),
  reason: z
    .string({ message: 'Vui lòng nhập lý do' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập lý do' }),

  sampleNo: z
    .string({ message: 'Vui lòng nhập mã mẫu' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập mã mẫu' }),
  sampleName: z
    .string({ message: 'Vui lòng nhập tên mẫu' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập tên mẫu' }),
  sampleDate: z.custom<DateObject>(value => isValidDateObject(value), {
    message: 'Vui lòng chọn ngày lưu mẫu',
  }),
  sampleReleaseDate: z.custom<DateObject>(value => isValidDateObject(value), {
    message: 'Vui lòng chọn ngày xả mẫu',
  }),

  note: z.string().optional(),

  result: z
    .string({ message: 'Vui lòng nhập kết quả hủy' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập kết quả hủy',
    }),
  approvedBy: z
    .string({ message: 'Vui lòng chọn người phê duyệt' })
    .refine(value => value.trim() !== '', {
      message: 'Vui lòng chọn người phê duyệt',
    }),
});

export type RawMaterialSchema = z.infer<typeof rawMaterialSchema>;
export type RawMaterial2Schema = z.infer<typeof rawMaterial2Schema>;
export type AdditivesSchema = z.infer<typeof additivesSchema>;
export type Additives2Schema = z.infer<typeof additives2Schema>;
export type ProductionSchema = z.infer<typeof productionSchema>;
export type ProductionStep1Schema = z.infer<typeof productionStep1Schema>;
export type ProductionStep2Schema = z.infer<typeof productionStep2Schema>;
export type ProductionStep3Schema = z.infer<typeof productionStep3Schema>;
export type Production2Schema = z.infer<typeof production2Schema>;
export type ProductionPackagingSchema = z.infer<
  typeof productionPackagingSchema
>;
export type QualityCheckSampleSchema = z.infer<typeof qualityCheckSampleSchema>;
export type ProductionBatchSchema = z.infer<typeof productionBatchSchema>;
export type ProductionSaveInventorySchema = z.infer<
  typeof productionSaveInventorySchema
>;
export type ManufactureOrderSchema = z.infer<typeof manufactureOrderSchema>;
export type ManufactureOrderByOrderCreateSchema = z.infer<
  typeof manufactureOrderByOrderCreateSchema
>;
export type ManufactureOrderByStandardCreateSchema = z.infer<
  typeof manufactureOrderByStandardCreateSchema
>;

export type ModalQualityCancelSampleSchema = z.infer<
  typeof modalQualityCancelSampleSchema
>;

export const manufactureOrderFilterSchema = z.object({
  status: z.string().optional(),
  createdAt: z
    .array(z.custom<DateObject>(value => isValidDateObject(value)).optional())
    .optional(),
});

export type ManufactureOrderFilterSchema = z.infer<
  typeof manufactureOrderFilterSchema
>;
