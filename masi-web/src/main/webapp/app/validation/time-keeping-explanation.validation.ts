import { TIME_KEEPING_EXPLANATION_REASON } from 'app/shared/model/enumerations/time-keeping-explanation.model';
import { TIME_KEEPING_VIOLATION_TYPE } from 'app/shared/model/enumerations/time-keeping-violation.model';
import { z } from 'zod';

const REASON_MESSAGE = 'Vui lòng nhập lý do giải trình';
const EXPLANATION_MAX_LENGTH = 200;
const EXPLANATION_MESSAGE = `Giải trình không được vượt quá ${EXPLANATION_MAX_LENGTH} ký tự`;
const REVIEW_REASON_MESSAGE = 'Vui lòng nhập lý';
const REVIEW_EXPLANATION_MESSAGE = `Lý do không được vượt quá ${EXPLANATION_MAX_LENGTH} ký tự`;

export const postTimeKeepingExplanationSchema = z.object({
  // explanation: z
  //   .string({ message: REASON_MESSAGE })
  //   .max(200, { message: EXPLANATION_MESSAGE })
  //   .refine(value => value.trim() !== '', { message: REASON_MESSAGE }),
  // reason: z.nativeEnum(TIME_KEEPING_EXPLANATION_REASON),
  // violationIds: z.array(z.string()).optional(),
  // employeeId: z.string().optional(),
  listOfViolations: z.array(z.object({
    id: z.string().optional(),
    reason: z.string().optional(),
    explanation: z.string().optional(),
  }).optional()).optional(),
});

export const patchTimeKeepingExplanationSchema = z.object({
  explanation: z
    .string({ message: REASON_MESSAGE })
    .max(200, { message: EXPLANATION_MESSAGE })
    .refine(value => value.trim() !== '', { message: REASON_MESSAGE }),
  reason: z.nativeEnum(TIME_KEEPING_EXPLANATION_REASON),
});

export const reviewTimeKeepingExplanationSchema = z.object({
  reason: z
    .string({ message: REVIEW_REASON_MESSAGE })
    .max(200, { message: REVIEW_EXPLANATION_MESSAGE })
    .refine(value => value.trim() !== '', { message: REVIEW_REASON_MESSAGE }),
});

export type CreateTimeKeepingExplanationSchema = z.infer<typeof postTimeKeepingExplanationSchema>;
export type UpdateTimeKeepingExplanationSchema = z.infer<typeof patchTimeKeepingExplanationSchema>;
export type ReviewTimeKeepingExplanationSchema = z.infer<typeof reviewTimeKeepingExplanationSchema>;
