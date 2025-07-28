import z from 'zod'

export const moveSchema = z.object({
    move: z.array(z.object({
        date: z.coerce.string({
            message: 'Ngày không được để trống'
        }),
        reflect: z.string({
            message: 'Tham chiếu được để trống'
        }),
        fromEmployee: z.string({
            message: 'Vị trí bắt đầu không được để trống'
        }),
        toEmployee: z.string({
            message: 'Vị trí đến không được để trống'
        }),
        fromPosition: z.string({
            message: 'Từ ttcp không được để trống'
        }),
        toPosition: z.string({
            message: 'Đến ttcp không được để trống'
        }),
        // fromNsd: z.string({
        //     message: 'Vị trí bắt đầu không được để trống'
        // }),
        // toNsd: z.coerce.string({
        //     message: 'Vị trí đến không được để trống'
        // }),
        personChange: z.coerce.string({
            message: 'Người đổi không được để trống'
        }),
        note: z.string({
            message: 'Diễn giải không được để trống'
        }),
    }))
}).deepPartial()

export type MoveSchemaType = z.infer<typeof moveSchema>

export type MoveArr = MoveSchemaType['move']

export type Move = MoveArr[number]