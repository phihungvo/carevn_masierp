import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';

const REFERENCE_NUMBER_MESSAGE = 'Vui lòng nhập số tham chiếu';
const LIQUIDATION_REASON_MESSAGE = 'Vui lòng chọn lý do thanh lý';
const CREATOR_MESSAGE = 'Vui lòng nhập thông tin người tạo';

export const liquidationSchema = z
    .object({
        referenceNumber: z.string({ message: REFERENCE_NUMBER_MESSAGE }).refine(value => value?.trim() !== '', { message: REFERENCE_NUMBER_MESSAGE }),
        liquidationDate: z.custom<DateObject>(value => isValidDateObject(value)),
        creationDate: z.custom<DateObject>(value => isValidDateObject(value)),
        liquidationReason: z.string({ message: LIQUIDATION_REASON_MESSAGE }).refine(value => value?.trim() !== '', { message: LIQUIDATION_REASON_MESSAGE }),
        creator: z.string({ message: CREATOR_MESSAGE }).refine(value => value?.trim() !== '', { message: CREATOR_MESSAGE }),
    })

export type LiquidationSchema = z.infer<typeof liquidationSchema>;
