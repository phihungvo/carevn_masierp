import { z } from 'zod';

const LEAVE_AFTER_PROBATION_MESSAGE = 'Vui lòng nhập số phép sau thử việc';
const LEAVE_PER_YEAR_MESSAGE = 'Vui lòng nhập số phép sau mỗi năm';
const LEAVE_MONTHLY_ACCUMULATED_MESSAGE = 'Vui lòng chọn tháng ngày phép được cộng dồn';

export const annualLeaveSchema = z.object({
  annualLeave_FACTORY: z.object({
    leaveAfterProbation: z.string({ message: LEAVE_AFTER_PROBATION_MESSAGE }).refine(value => value !== '', {
      message: LEAVE_AFTER_PROBATION_MESSAGE,
    }),
    leavePerYear: z.string({ message: LEAVE_PER_YEAR_MESSAGE }).refine(value => value !== '', {
      message: LEAVE_PER_YEAR_MESSAGE,
    }),
    carryForwardMonth: z.string({ message: LEAVE_MONTHLY_ACCUMULATED_MESSAGE }).refine(value => value !== '', {
      message: LEAVE_MONTHLY_ACCUMULATED_MESSAGE,
    }),
  }),
  annualLeave_OFFICE: z.object({
    leaveAfterProbation: z.string({ message: LEAVE_AFTER_PROBATION_MESSAGE }).refine(value => value !== '', {
      message: LEAVE_AFTER_PROBATION_MESSAGE,
    }),
    leavePerYear: z.string({ message: LEAVE_PER_YEAR_MESSAGE }).refine(value => value !== '', {
      message: LEAVE_PER_YEAR_MESSAGE,
    }),
    carryForwardMonth: z.string({ message: LEAVE_MONTHLY_ACCUMULATED_MESSAGE }).refine(value => value !== '', {
      message: LEAVE_MONTHLY_ACCUMULATED_MESSAGE,
    }),
  }),
});

export type AnnualLeaveFormSchema = z.infer<typeof annualLeaveSchema>;
