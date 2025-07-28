import { depriciationTableSchema } from "app/modules/depreciation/validations/depriciation-table.validation"
import { requestApprovalSchemaRequire } from "app/validation/supplies-request.validation"
import { z } from "zod"

export const allociationFormSchema = z.object({
  code: z.string({
    message: 'Kỳ phân bổ không được để trống',
  }), // Kỳ phân bổ
  attribute: z.string().optional(),
  name: z.string().optional(), // Tên người tính
  depreciationDate: z.string({
    message: 'Ngày tính phân bổ không được để trống',
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

export type AllociationFormSchemaType = z.infer<typeof allociationFormSchema>
