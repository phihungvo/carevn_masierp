import z from 'zod'

export const humanResourceTableSchema = z.object({
    humanResource: z.array(z.object({
        name: z.string().optional(),
        employeeId: z.string({
            message: 'Nhân viên không được để trống'
        }),
        position: z.string({
            message: 'Chức vụ không được để trống'
        }),
        representative: z.string({
            message: 'Đại diện không được để trống'
        }),
        role: z.string({
            message: 'Chức vụ không được để trống'
        }),
        humanId: z.string().optional()
    }), {
        message: 'Danh sách nhân sự không được để trống'
    })
}, {
    message: 'Danh sách nhân sự không được để trống'
})

export type HumanResourceSchemaType = z.infer<typeof humanResourceTableSchema>

export type HumanResourceItem = HumanResourceSchemaType['humanResource'][number]