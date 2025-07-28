import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';

const NAME_MESSAGE = 'Vui lòng nhập tên định tuyến';
const QUANTITY_MESSAGE = 'Vui lòng nhập số lượng';
const UNIT_MESSAGE = 'Vui lòng chọn đơn vị';
const FACTORY_MESSAGE = 'Vui lòng chọn nơi SX';
const STORAGE_MESSAGE = 'Vui lòng chọn nơi lưu trữ';
const PACKAGE_MESSAGE = 'Vui lòng chọn lô hàng';
const PRODUCTION_MAINTAIN_ID = 'Vui lòng chọn mã bảo trì';

export const productionRoutingSchema = z.object({
  name: z
    .string({ message: NAME_MESSAGE })
    .refine(value => value.trim() !== '', { message: NAME_MESSAGE }),
  quantity: z.string().optional(),
  warehouseDate: z.custom<DateObject>(value => isValidDateObject(value), {
    message: 'Vui lòng chọn lưu kho',
  }),
  storageId: z.string({ message: STORAGE_MESSAGE }),
  productMaintainId: z.string({ message: PRODUCTION_MAINTAIN_ID }),
  productPackageName: z.string().optional(),
  productPackageWeight: z.string().optional(),

  typeManufactureOrder: z.string().optional(),

  statusM: z.string().optional(), // Status Manufacture Order
});

export type ProductionRoutingFormSchema = z.infer<
  typeof productionRoutingSchema
>;
