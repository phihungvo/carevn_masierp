import { z } from "zod";

export const schema = z.object({
  // title modal
  contractCode: z.string().nullable().optional(),
  itemCode: z.string().nullable().optional(),
  supplierCode: z.string().nullable().optional(),
  itemId: z.string().nullable().optional(),
  orderCode: z.string().nullable().optional(),
  // body
  id: z.string(),
  orderId: z.string({
    message: 'Đơn hàng là bắt buộc',
  }),
  contractId: z.string({
    message: 'Hợp đồng là bắt buộc',
  }),
  deliveryDate: z.string({
    message: 'Ngày giao dự kiến là bắt buộc',
  }),
  quantity: z.coerce.number({
    message: 'Số lượng dự kiến là bắt buộc',
  }),
  actualDeliveryDate: z.string({
    message: 'Ngày giao thực tế là bắt buộc',
  }),
  actualQuantity: z.coerce.number({
    message: 'Số lượng thực tế là bắt buộc',
  }),
  deliveryLocation: z.string({
    message: 'Địa chỉ là bắt buộc',
  }),
  note: z.string().optional().nullable(),
  deliveryDetail: z.array(
    z.object({
      id: z.string(), // item id
      contractDetailId: z.any(),
      name: z.string().optional(), // item name
      contractQuantity: z.coerce.number().optional(),
      isChecked: z.boolean().default(false),
      deliveryQuantity: z.coerce.number({
        message: 'Số lượng dự kiến là bắt buộc',
      }),
    })
    .refine((value) => {
      if (!value.isChecked) return true
      return value.deliveryQuantity > 0
    }, { message: 'Số lượng dự kiến phải lớn hơn 0' }
    ),
  ).refine((value) => {
    return value.some((item) => item.isChecked)
  }, {
    message: 'Cần chọn tối thiểu 1 hàng hóa',
  })
})

export const createSchema = schema.omit({ quantity: true, actualQuantity: true, actualDeliveryDate: true })

export const updateSchema = schema.partial({ deliveryDetail: true })

export const createDeliverySchema = z.object({
  type: z.literal('CREATE'),
  body: z.discriminatedUnion('mode', [
    z.object({
      mode: z.literal('sell'),
    }).merge(createSchema.partial({ id: true, contractId: true })),
    z.object({
      mode: z.literal('purchase'),
    }).merge(createSchema.omit({ orderId: true }).partial({ id: true })),
  ]),
})

export const updateDeliverySchema = z.object({
  type: z.literal('UPDATE'),
  body: z.discriminatedUnion('mode', [
    z.object({
      mode: z.literal('sell'),
    }).merge(updateSchema.required({ id: true }).partial({ contractId: true })).omit({ deliveryDetail: true }),
    z.object({
      mode: z.literal('purchase'),
    }).merge(updateSchema.required({ id: true }).partial({ contractId: true, orderId: true })).omit({ deliveryDetail: true }),
  ]),
})

export const deliveryScheduleSchema = z.discriminatedUnion('type', [
  createDeliverySchema,
  updateDeliverySchema,
])

export type DeliveryScheduleSchema = z.infer<typeof deliveryScheduleSchema>