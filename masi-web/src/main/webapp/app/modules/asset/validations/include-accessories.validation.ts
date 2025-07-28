import z from 'zod'

export const includeAccessoriesSchema = z.object({
    includeAccessoriesSchema: z.array(z.object({
        code: z.string({
            message: 'Mã phụ kiện không được để trống'
        }),
        name: z.string({
            message: 'Tên phụ kiện không được để trống'
        }),
        unitId: z.string({
            message: 'Đơn vị tính không được để trống'
        }),
        quantity: z.coerce.number({
            message: 'Số lượng không được để trống'
        }),
        price: z.coerce.number({
            message: 'Giá trị không được để trống'
        }),
        statusId: z.string({
            message: 'Trạng thái không được để trống'
        }),
        brokenDate: z.string({
            message: 'Ngày hỏng không được để trống'
        }),
        addedDate: z.string({
            message: 'Ngày thêm không được để trống'
        }),
        save: z.string({
            message: 'Ngày lưu kho không được để trống'
        }),
        note: z.string({
            message: 'Ghi chú không được để trống'
        })
    }))
}).deepPartial()

export type IncludeAccessoriesSchemaType = z.infer<typeof includeAccessoriesSchema>

export type IncludeAccessoriesArr = IncludeAccessoriesSchemaType['includeAccessoriesSchema']

export type IncludeAccessories = IncludeAccessoriesArr[number]