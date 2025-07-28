import z from 'zod'

export const ariseSchema = z.object({
    arises: z.array(z.object({
        job: z.string({
            message: 'Nghiệp vụ không được để trống'
        }),
        date: z.string({
            message: 'Ngày không được để trống'
        }),
        reflectNumber: z.string({
            message: 'Số tham chiếu không được để trống'
        }),
        ttcp: z.coerce.string({
            message: 'TTCP không được để trống'
        }),
        quantity: z.coerce.number({
            message: 'Số lượng không được để trống'
        }),
        price: z.coerce.number({
            message: 'Nguyên giá không được để trống'
        }),
        depriciation: z.coerce.number({
            message: 'Khấu hao không được để trống'
        }),
        KhMonth: z.string({
            message: 'Tháng KH không được để trống'
        }),
        note: z.string({
            message: 'Diễn giải không được để trống'
        }),
        recourseCode: z.string({
            message: 'Từ mã gốc không được để trống'
        }),
    }))
}).deepPartial()

export type AriseSchemaType = z.infer<typeof ariseSchema>

export type AriseArr = AriseSchemaType['arises']

export type Arise = AriseArr[number]