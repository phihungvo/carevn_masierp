import z from 'zod'

export const propertyList = z.object({
    propertyList: z.array(z.object({
        id: z.string({
            message: 'Tài sản là bắt buộc'
        }),
        content: z.string().optional(),
        ttcp: z.string({
            message: 'TTCP là bắt buộc'
        }),
        codeVtcc: z.string({
            message: 'Mã VTCC là bắt buộc'
        }),
        quantity: z.coerce.number({
            message: 'Số lượng là bắt buộc'
        }),
        originalPrice: z.coerce.number({
            message: 'Nguyên giá là bắt buộc'
        }),
        depreciation: z.coerce.number({
            message: 'Khấu hao là bắt buộc'
        }),
        remainingValue: z.coerce.number({
            message: 'Giá trị còn lại là bắt buộc'
        }),
        tkThanhLyGiam: z.string({
            message: 'TK thanh lý/giảm là bắt buộc'
        }),
        providerId: z.string({
            message: 'Nhà cung cấp là bắt buộc'
        }),
        inventoriesStorageId: z.string({
            message: 'Kho lưu trữ là bắt buộc'
        }),
        note: z.string().optional()
    }))
})

export type PropertyListSchemaType = z.infer<typeof propertyList>