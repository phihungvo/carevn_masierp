import { z } from "zod";

export const TableFilterSchema = z.object({
    deliveryDate: z.string({
        message: 'Ngày bắt đầu là bắt buộc',
    }),
    expectedReceiveDate: z.string({
        message: 'Ngày kết thúc là bắt buộc',
    }),
});

export type TableFilter = z.infer<typeof TableFilterSchema>;