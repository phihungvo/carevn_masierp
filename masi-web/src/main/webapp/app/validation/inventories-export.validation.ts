import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';
import { InventoriesWarehouse } from 'app/shared/model/enumerations/warehouse.model';

export const inventoriesExportSchema = z
  .object({
    code: z.string().optional(),
    incomingWarehouseId: z.string({ message: 'Vui lòng chọn kho' }),
    inventoriesTypeId: z.string({ message: 'Vui lòng chọn loại kho' }),
    dateCreate: z.custom<DateObject>(value => isValidDateObject(value), {
      message: 'Vui lòng chọn ngày tạo',
    }),
    createdBy: z.string().optional(),
    createdByName: z.string().optional(),
    employeeName: z.string().optional(),
    employeeId: z.string({ message: 'Vui lòng chọn nhân viên' }),

    warehouseGroupType: z.any().optional(),

    workspaceName: z.string().optional(),
    isNoReview: z.boolean().optional(),
    createdAt: z
      .custom<DateObject>(value => isValidDateObject(value))
      .optional(),
    note: z.string().optional(),

    workspaceId: z.string().optional(),

    customerId: z.string().optional(),
    shipper: z.string().optional(),
    shipperAddress: z.string().optional(),
    receiverName: z.string().optional(),
    receiverPhone: z.string().optional(),

    inventoriesItemDetails: z
      .array(
        z.object({
          id: z.string().optional(),
          code: z.string().optional(),
          itemId: z.string().optional(),
          quantity: z.string().optional(),
          price: z.string().optional(),
          note: z.string().optional(),
          uomId: z.string()?.optional(),
          totalPrice: z.any().optional(),

          vatId: z.string().optional(),
          vatRate: z.string().optional(),
          vatAmount: z.string().optional(),

          // Xuất kho
          registerDate: z
            .custom<DateObject>(value => isValidDateObject(value))
            .optional(), // Ngày DK
          depreciationDate: z
            .custom<DateObject>(value => isValidDateObject(value))
            .optional(), // Ngày khấu hao
          departmentId: z.string().optional(), // Bộ phận SD
          groupName: z.string().optional(), // Nhóm
          usageMonth: z.string().optional(), // Tháng SD
          holder: z.string().optional(), // TK TS
          depreciationAllocation: z.string().optional(), // TK KH/PB
          expenseAccount: z.string().optional(), // TK chi phí
          costElements: z.string().optional(), // Yếu tố chi phí
          unitPrice: z.string().optional(), // Nguyên giá
        }),
      )
      .optional(),

    inventoriesMaterialDetails: z
      .array(
        z.object({
          id: z.string().optional(),
          code: z.string().optional(),
          itemId: z.string().optional(),
          quantity: z.string().optional(),
          price: z.string().optional(),
          note: z.string().optional(),
          uomId: z.string().optional(),
          totalPrice: z.any().optional(),

          vatId: z.string().optional(),
          vatRate: z.string().optional(),
          vatAmount: z.string().optional(),

          registerDate: z
            .custom<DateObject>(value => isValidDateObject(value))
            .optional(), // Ngày DK
          depreciationDate: z
            .custom<DateObject>(value => isValidDateObject(value))
            .optional(), // Ngày khấu hao
          departmentId: z.string().optional(), // Bộ phận SD
          groupName: z.string().optional(), // Nhóm
          usageMonth: z.string().optional(), // Tháng SD
          holder: z.string().optional(), // TK TS
          depreciationAllocation: z.string().optional(), // TK KH/PB
          expenseAccount: z.string().optional(), // TK chi phí
          costElements: z.string().optional(), // Yếu tố chi phí
          unitPrice: z.string().optional(), // Nguyên giá
        }),
      )
      .optional(),

    orderId: z.string().optional(),
    paymentRequestId: z.string().optional(),

    status: z.string().optional(),
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
        }),
      )
      .optional(),
    file: z
      .array(
        z.object({
          fileId: z.string().optional(),
          fileName: z.string().optional(),
          createdAt: z.date().optional(),
        }),
      )
      .optional(),
  })
  .superRefine((data, ctx) => {
    if ( Boolean(data.isNoReview) === false && (!data.requestApprovals || data.requestApprovals.length < 1) ) {
      ctx.addIssue({ code: 'custom', path: ['requestApprovals'], message: 'Vui lòng chọn người ký', });
    }
    if ( data?.warehouseGroupType === (InventoriesWarehouse.WAREHOUSE_COMMERCE_EXPORT as string) ) {
      if (!data?.customerId)
        ctx.addIssue({ code: 'custom', path: ['customerId'], message: 'Vui lòng chọn KH', });
      if (!data?.shipper)
        ctx.addIssue({ code: 'custom', path: ['shipper'], message: 'Vui lòng nhập KH giao', });
      if (!data?.shipperAddress)
        ctx.addIssue({ code: 'custom', path: ['shipperAddress'], message: 'Vui lòng nhập địa chỉ', });
      if (!data?.receiverName)
        ctx.addIssue({ code: 'custom', path: ['receiverName'], message: 'Vui lòng nhập người nhận', });
      if (!data?.receiverPhone)
        ctx.addIssue({ code: 'custom', path: ['receiverPhone'], message: 'Vui lòng nhập số điện thoại', });
      if (data?.inventoriesItemDetails?.length === 0)
        ctx.addIssue({ code: 'custom', path: ['inventoriesItemDetails'], message: 'Vui lòng thêm hàng hóa', });
      if(data?.inventoriesItemDetails?.length && data?.warehouseGroupType === InventoriesWarehouse.WAREHOUSE_COMMERCE_EXPORT as string) {
        data?.inventoriesItemDetails.forEach((item, index) => {
          if (item.itemId === '' || item.itemId === undefined) ctx.addIssue({ code: 'custom', path: ["inventoriesItemDetails", index, 'itemId'], message: 'Vui lòng chọn sản phẩm' }); 
          if (item.quantity === '' || item.quantity === undefined) ctx.addIssue({ code: 'custom', path: ["inventoriesItemDetails", index, 'quantity'], message: 'Vui lòng nhập số lượng' }); 
          if (item.price === '' || item.price === undefined) ctx.addIssue({ code: 'custom', path: ["inventoriesItemDetails", index, 'price'], message: 'Vui lòng nhập đơn giá' }); 
          if (!item.vatId) ctx.addIssue({ code: 'custom', path: ["inventoriesItemDetails", index, 'vatId'], message: 'Vui lòng chọn %VAT' }); 
        });
      }
    } else {
      // if (!data?.workspaceId)
      //   ctx.addIssue({ code: 'custom', path: ['workspaceId'], message: 'Vui lòng chọn nơi nhận', });
      if(data?.inventoriesItemDetails?.length === 0 && data?.inventoriesMaterialDetails?.length === 0)
        ctx.addIssue({ code: 'custom', path: ['inventoriesItemDetails'], message: 'Vui lòng thêm hàng hóa', });
      if(data?.inventoriesItemDetails?.length && data?.warehouseGroupType === InventoriesWarehouse.WAREHOUSE_DEPRECIATION_EXPORT as string) {
        data?.inventoriesItemDetails.forEach((item, index) => {
          if (item.itemId === '' || item.itemId === undefined) ctx.addIssue({ code: 'custom', path: ["inventoriesItemDetails", index, 'itemId'], message: 'Vui lòng chọn sản phẩm' }); 
          if (item.quantity === '' || item.quantity === undefined) ctx.addIssue({ code: 'custom', path: ["inventoriesItemDetails", index, 'quantity'], message: 'Vui lòng nhập số lượng' });
        });
      }
    }

    // workspaceId: z.string().optional(),
  });

export const inventoriesExportFilterSchema = z.object({
  createdAt: z
    .array(z.custom<DateObject>(value => isValidDateObject(value)).optional())
    .optional(),
  status: z.string().optional(),
  inventoriesTypeId: z.string().optional(),
  isInvoice: z.boolean().optional(),
  customerId: z.string().optional(),
  incomingWarehouseId: z.string().optional(),
});

export type InventoriesExportSchema = z.infer<typeof inventoriesExportSchema>;
export type InventoriesExportFilterSchema = z.infer<
  typeof inventoriesExportFilterSchema
>;
