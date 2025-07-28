import z, { date } from 'zod'

export const generalInfoSchema = z.object({
    generalInformation: z.object({
        registerCode: z.string({
            message: 'Số đăng ký không được để trống'
        }),
        registerDate: z.string({
            message: 'Ngày đăng ký không được để trống'
        }),
        numberHandover: z.string({
            message: 'Số bàn giao không được để trống'
        }),
        handoverDate: z.string({
            message: 'Ngày bàn giao không được để trống'
        }),
        handoverPersonId: z.string({
            message: 'Người bàn giao không được để trống'
        }),
        whoUseId: z.string({
            message: 'Người sử dụng không được để trống'
        }),
        locationId: z.string({
            message: 'Vị trí không được để trống'
        }),
        series: z.string({
            message: 'Số series không được để trống'
        }),
        usingDate: z.string({
            message: 'Ngày sử dụng không được để trống'
        }),
        numberBill: z.string({
            message: 'Số hóa đơn không được để trống'
        }),
        billDate: z.string({
            message: 'Ngày hóa đơn không được để trống'
        }),
        warehouseId: z.string({
            message: 'Kho không được để trống'
        }),
        note: z.string(),
        status: z.string({
            message: 'Trạng thái không được để trống'
        }),
        liquidationDate: z.string({
            message: 'Ngày thanh lý không được để trống'
        }),
        quantity: z.coerce.number({
            message: 'Số lượng không được để trống'
        }),
        unitCalculateId: z.string({
            message: 'Đơn vị tính không được để trống'
        }),
        usageYear: z.coerce.number().optional(),
        usageMonth: z.coerce.number().optional(),
        warrantyPeriod: z.coerce.string({
            message: 'Thời gian bảo hành không được để trống'
        }),
        manufacturerId: z.string({
            message: 'Nhà sản xuất không được để trống'
        }),
        isDomestic: z.boolean().default(false),
        dateManufacture: z.string({
            message: 'Ngày sản xuất không được để trống'
        }),
        parameter: z.string({
            message: 'Thông số không được để trống'
        }),
        suspensionDay: z.string({
            message: 'Ngày đình chỉ không được để trống'
        }),
        reason: z.string({
            message: 'Lý do không được để trống'
        }),
    })
}).deepPartial()

export type GeneralInfoSchemaType = z.infer<typeof generalInfoSchema>