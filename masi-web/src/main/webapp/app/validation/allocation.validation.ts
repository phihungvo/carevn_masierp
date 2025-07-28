import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';
import { ALLOCATION_TYPE } from 'app/shared/model/enumerations/allocation';

const DEPRECIATION_PERIOD_MESSAGE = 'Vui lòng nhập kỳ khấu hao';
const DEPRECIATION_DATE_MESSAGE = 'Vui lòng chọn ngày tính khấu hao';
const TYPE_MESSAGE = 'Vui lòng chọn tình trạng';
const CALCULATOR_MESSAGE = 'Vui lòng chọn người tính';
const DESCRIPTION_MESSAGE = 'Vui lòng nhập diễn giải';

export const allocationSchema = z
    .object({
        depreciationPeriod: z.string({ message: DEPRECIATION_PERIOD_MESSAGE }).refine(value => value?.trim() !== '', { message: DEPRECIATION_PERIOD_MESSAGE }),
        depreciationDate: z.custom<DateObject>(value => isValidDateObject(value)),
        accountingDate: z.custom<DateObject>(value => isValidDateObject(value)),
        calculator: z.string({ message: CALCULATOR_MESSAGE }).refine(value => value?.trim() !== '', { message: CALCULATOR_MESSAGE }),
        description: z.string({ message: DESCRIPTION_MESSAGE }).refine(value => value?.trim() !== '', { message: DESCRIPTION_MESSAGE }),
        type: z.nativeEnum(ALLOCATION_TYPE, { message: TYPE_MESSAGE }).refine(value => value?.trim() !== '', { message: TYPE_MESSAGE }),
    })

export type AllocationSchema = z.infer<typeof allocationSchema>;
