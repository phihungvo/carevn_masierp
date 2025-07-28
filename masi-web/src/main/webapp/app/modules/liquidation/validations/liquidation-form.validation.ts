import { requestApprovalSchemaRequire } from 'app/validation/supplies-request.validation';
import { z } from 'zod';
import { humanResourceTableSchema } from './human-resource-table.validation';
import { propertyList } from './property-list.validation';

export const liquidationFormSchema = z.object({
  reflectNumber: z.string().optional(), // Số tham chiếu
  createdAt: z.string().optional(), // Ngày tạo
  reason: z.string({
    message: 'Lý do không được để trống',
  }), // lý do thanh lý
  liquidationDate: z.string(), // Ngày thanh lý
  description: z.string().optional(), // Diễn giải
  createdBy: z.string().optional(), // Người tạo
}).merge(humanResourceTableSchema).merge(propertyList).merge(requestApprovalSchemaRequire)

export type LiquidationFormSchemaType = z.infer<typeof liquidationFormSchema>;
