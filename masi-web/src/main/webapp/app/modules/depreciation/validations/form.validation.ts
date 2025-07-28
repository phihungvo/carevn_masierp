import { z } from "zod"
import { depriciationTableSchema } from "./depriciation-table.validation"
import { requestApprovalSchemaRequire } from "app/validation/supplies-request.validation"

export const formSchema = z.object({
  code: z.string({
    message: 'Kỳ khấu hao không được để trống',
  }), // Kỳ khấu hao
  attribute: z.string().optional(),
  name: z.string().optional(), // Tên người tính
  depreciationDate: z.string({
    message: 'Ngày tính khấu hao không được để trống',
  }), // Ngày tính khấu hao
  accountingDate: z.string({
    message: 'Ngày hạch toán không được để trống',
  }), // Ngày hạch toán
  employeeId: z.string({
    message: 'Người tính không được để trống',
  }), // Người tính
  description: z.string().optional(), // Diễn giải
  typePageDepreciation: z.string().optional(),
  isDeleteAll: z.boolean().default(false),

  createdAt: z.string().optional(), // Ngày tạo
  createdBy: z.string().optional(), // Người tạo
}).merge(depriciationTableSchema).merge(requestApprovalSchemaRequire)

export type FormSchemaType = z.infer<typeof formSchema>
