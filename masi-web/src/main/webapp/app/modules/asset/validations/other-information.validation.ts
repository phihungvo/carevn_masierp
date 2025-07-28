import { update } from 'lodash'
import z from 'zod'

export const otherInformationSchema = z.object({
    otherInformation: z.object({
        createdBy: z.string().optional(),
        createdAt: z.string().optional(),
        updatedBy: z.string().optional(),
        updatedAt: z.string().optional(),
        software: z.string().optional(),
        pxEmployee: z.string().optional(),
        pxInformation: z.string().optional(),
    })
}).deepPartial()

export type OtherInformationSchemaType = z.infer<typeof otherInformationSchema>

export type OtherInformation = OtherInformationSchemaType['otherInformation']