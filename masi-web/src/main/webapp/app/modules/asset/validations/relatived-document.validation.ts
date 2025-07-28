import z from 'zod'

export const relativedDocumentSchema = z.object({
    relativedDocument: z.array(z.object({
        resource: z.string({
            message: 'Nguồn không được để trống'
        }),
        documentCode: z.string({
            message: 'Mã chứng từ không được để trống'
        }),
        documentNumber: z.coerce.number({
            message: 'Số chứng từ không được để trống'
        }),
        date: z.coerce.string({
            message: 'Ngày chứng từ không được để trống'
        }),
        price: z.coerce.number({
            message: 'Số tiền không được để trống'
        }),
        note: z.string({
            message: 'Ghi chú không được để trống'
        })
    }))
}).deepPartial()

export type RelativedDocumentSchema = z.infer<typeof relativedDocumentSchema>

export type RelativedDocumentArr = RelativedDocumentSchema['relativedDocument']

export type RelativedDocument = RelativedDocumentArr[number]