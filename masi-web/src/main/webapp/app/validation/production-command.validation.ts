import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';
import { PRODUCTION_COMMAND_TYPE } from 'app/shared/model/enumerations/production-command.model';

const COMMAND_NAME = 'Vui lòng nhập tên lệnh sản xuất';
const COMMAND_BILL = 'Vui lòng chọn đơn sản xuất';
const PRODUCTION_STANDARD_ID = 'Vui lòng chọn định mức sản xuất';
const START_DATE_MESSAGE = 'Vui lòng chọn ngày bắt đầu';
const DUE_DATE_MESSAGE = 'Vui lòng chọn ngày kết thúc';

export const commandOrdersSchema = z
  .object({
    code: z.string({ message: 'Vui lòng nhập mã lệnh sản xuất' }).refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập mã lệnh sản xuất',
    }),
    name: z.string({ message: COMMAND_NAME }).refine(value => value.trim() !== '', { message: COMMAND_NAME }),
    orderId: z.string({ message: COMMAND_BILL }),
    fromDate: z.custom<DateObject>(value => isValidDateObject(value), { message: START_DATE_MESSAGE }),
    toDate: z.custom<DateObject>(value => isValidDateObject(value), { message: DUE_DATE_MESSAGE }),
    // typeProtein: z.string({ message: TYPE_PROTEIN }).refine(value => value.trim() !== '', { message: TYPE_PROTEIN }),
    materialId: z.string({ message: 'Vui lòng chọn thành phẩm' }).refine(value => value.trim() !== '', {
      message: 'Vui lòng chọn thành phẩm',
    }),
    percentProtein: z.string().optional(),
    manufactureOrderType: z.any().optional(),
    productionStandardId: z.string().optional(),
    productionQuantity: z.string().optional(),
    warehouseIds: z
      .array(
        z
          .object({
            label: z.string().optional(),
            value: z.string().optional(),
          })
          .optional(),
      )
      .optional(),
    // selectedMaterials: z
    //   .array(z.string({ message: 'Vui lòng chọn ít nhất 2 nguyên liệu' }), { message: 'Vui lòng chọn ít nhất 2 nguyên liệu' })
    //   .min(2, 'Vui lòng chọn ít nhất 2 nguyên liệu'),
    // TODO: revert back to the above line after fixing the issue
    selectedMaterials: z.array(z.string().optional()).optional(),
    averagePercentProtein: z.string().optional(),
    materials: z
      .array(
        z
          .object({
            itemId: z.string().optional(),
            itemName: z.string().optional(),
            itemCategoryId: z.string().optional(),
            itemCategoryCode: z.string().optional(),
            itemCategoryName: z.string().optional(),
            itemCode: z.string().optional(),
            percentProtein: z.number().optional(),
            quantity: z.number().optional(),
            uomId: z.string().optional(),
            uomName: z.string().optional(),
            calculationQuantity: z.string().optional(),
            productionVolume: z.string().optional(),
            warehouseId: z.string().optional(),
            warehouseName: z.string().optional(),
            warehouseTypeId: z.string().optional(),
            warehouseTypeName: z.string().optional(),
            expireDate: z.string().optional(),
          })
          .optional(),
      )
      .optional(),
    dataInventoryMaterial: z
      .array(
        z
          .object({
            itemId: z.string().optional(),
            itemName: z.string().optional(),
            itemCategoryId: z.string().optional(),
            itemCategoryCode: z.string().optional(),
            itemCategoryName: z.string().optional(),
            itemCode: z.string().optional(),
            percentProtein: z.number().optional(),
            quantity: z.number().optional(),
            uomId: z.string().optional(),
            uomName: z.string().optional(),
            calculationQuantity: z.string().optional(),
            productionVolume: z.string().optional(),
            warehouseId: z.string().optional(),
            warehouseName: z.string().optional(),
            warehouseTypeId: z.string().optional(),
            warehouseTypeName: z.string().optional(),
            expireDate: z.string().optional(),
          })
          .optional(),
      )
      .optional(),
    addWarehouseId: z.string().optional(),
    addMaterials: z.array(z.string().optional()).optional(),
  })
  .refine(data => new Date(data.fromDate.toDate().toISOString()) <= new Date(data.toDate.toDate().toISOString()), {
    message: 'Ngày bắt đầu phải nhỏ hơn ngày kết thúc',
    path: ['fromDate'],
  })
  .refine(data => {
    if (data?.productionQuantity !== '') return true;
    return false;
  }, {
    message: 'Vui lòng nhập khối lượng',
    path: ['productionQuantity'],
  })
// .refine(
//   data => {
//     if (!data?.manufactureOrderType || data?.manufactureOrderType === PRODUCTION_COMMAND_TYPE.MANUFACTURE_ORDER_BY_ORDER) {
//       return true;
//     }

//     // return data?.productionStandardId !== '';
//     return true;
//   },
//   {
//     message: 'Vui lòng chọn định mức sản xuất',
//     path: ['productionStandardId'],
//   },
// );

export const commandStandardSchema = z
  .object({
    code: z.string({ message: 'Vui lòng nhập mã lệnh sản xuất' }).refine(value => value.trim() !== '', {
      message: 'Vui lòng nhập mã lệnh sản xuất',
    }),
    name: z.string({ message: COMMAND_NAME }).refine(value => value.trim() !== '', { message: COMMAND_NAME }),
    productionStandardId: z.string({ message: PRODUCTION_STANDARD_ID }),
    fromDate: z.custom<DateObject>(value => isValidDateObject(value), { message: START_DATE_MESSAGE }),
    toDate: z.custom<DateObject>(value => isValidDateObject(value), { message: DUE_DATE_MESSAGE }),
    // typeProtein: z.string({ message: TYPE_PROTEIN }).refine(value => value.trim() !== '', { message: TYPE_PROTEIN }),
    percentProtein: z.string().optional(),
    manufactureOrderType: z.any().optional(),
    materialId: z
      .string({ message: 'Vui lòng chọn tên sản phẩm' })
      .refine(value => value.trim() !== '', { message: 'Vui lòng chọn tên sản phẩm' }),
    productionQuantity: z.string().optional(),
    warehouseIds: z
      .array(
        z
          .object({
            label: z.string().optional(),
            value: z.string().optional(),
          })
          .optional(),
      )
      .optional(),
    // selectedMaterials: z
    //   .array(z.string({ message: 'Vui lòng chọn ít nhất 2 nguyên liệu' }), { message: 'Vui lòng chọn ít nhất 2 nguyên liệu' })
    //   .min(2, 'Vui lòng chọn ít nhất 2 nguyên liệu'),
    // TODO: revert back to the above line after fixing the issue
    selectedMaterials: z.array(z.string().optional()).optional(),
    materials: z
      .array(
        z
          .object({
            itemId: z.string().optional(),
            itemName: z.string().optional(),
            itemCategoryId: z.string().optional(),
            itemCategoryCode: z.string().optional(),
            itemCategoryName: z.string().optional(),
            itemCode: z.string().optional(),
            percentProtein: z.number().optional(),
            quantity: z.number().optional(),
            uomId: z.string().optional(),
            uomName: z.string().optional(),
            calculationQuantity: z.string().optional(),
            productionVolume: z.string().optional(),
            warehouseId: z.string().optional(),
            warehouseName: z.string().optional(),
            warehouseTypeId: z.string().optional(),
            warehouseTypeName: z.string().optional(),
            expireDate: z.string().optional(),
          })
          .optional(),
      )
      .optional(),
    dataInventoryMaterial: z
      .array(
        z
          .object({
            itemId: z.string().optional(),
            itemName: z.string().optional(),
            itemCategoryId: z.string().optional(),
            itemCategoryCode: z.string().optional(),
            itemCategoryName: z.string().optional(),
            itemCode: z.string().optional(),
            percentProtein: z.number().optional(),
            quantity: z.number().optional(),
            uomId: z.string().optional(),
            uomName: z.string().optional(),
            calculationQuantity: z.string().optional(),
            productionVolume: z.string().optional(),
            warehouseId: z.string().optional(),
            warehouseName: z.string().optional(),
            warehouseTypeId: z.string().optional(),
            warehouseTypeName: z.string().optional(),
            expireDate: z.string().optional(),
          })
          .optional(),
      )
      .optional(),
    addWarehouseId: z.string().optional(),
    addMaterials: z.array(z.string().optional()).optional(),
  })
  .refine(data => new Date(data.fromDate.toDate().toISOString()) <= new Date(data.toDate.toDate().toISOString()), {
    message: 'Ngày bắt đầu phải nhỏ hơn ngày kết thúc',
    path: ['fromDate'],
  })
  .refine(
    data => {
      if (!data?.manufactureOrderType || data?.manufactureOrderType === PRODUCTION_COMMAND_TYPE.MANUFACTURE_ORDER_BY_STANDARD) {
        return true;
      }

      // return data?.productionStandardId !== '';
      return true;
    },
    {
      message: 'Vui lòng chọn định mức sản xuất',
      path: ['productionStandardId'],
    },
  );

export type CommandOrdersSchema = z.infer<typeof commandOrdersSchema>;
export type CommandStandardSchema = z.infer<typeof commandStandardSchema>;
