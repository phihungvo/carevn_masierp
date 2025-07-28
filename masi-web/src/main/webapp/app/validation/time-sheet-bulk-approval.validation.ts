import { z } from 'zod';

const MONTH_MESSAGE = 'Vui lòng chọn tháng';
const REASON_MESSAGE = 'Vui lòng nhập lý do';
const REASON_MAX_LENGTH_MESSAGE = 'Lý do không được vượt quá 200 ký tự';

export const approveTimeKeepingMonthSchema = z.object({
  month: z.string({ message: MONTH_MESSAGE }),
});

export const rejectTimeKeepingMonthSchema = z.object({
  month: z.string({ message: MONTH_MESSAGE }),
  note: z.string({ message: REASON_MESSAGE }).max(200, { message: REASON_MAX_LENGTH_MESSAGE }),
});

export type ApproveTimeKeepingMonthlySchema = z.infer<typeof approveTimeKeepingMonthSchema>;
export type RejectTimeKeepingMonthlySchema = z.infer<typeof rejectTimeKeepingMonthSchema>;
