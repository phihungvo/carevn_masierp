import { WORK_CENTER_STATUS } from 'app/shared/model/enumerations/work-center.model';
import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';

const CODE_MESSAGE = 'Vui lòng nhập mã';
const NAME_MESSAGE = 'Vui lòng nhập tên';
const STATUS_MESSAGE = 'Vui lòng chọn trạng thái';

export const productionWorkCenterSchema = z.object({
  code: z
    .string({ message: CODE_MESSAGE })
    .refine(value => value.trim() !== '', { message: CODE_MESSAGE }),
  name: z
    .string({ message: NAME_MESSAGE })
    .refine(value => value.trim() !== '', { message: NAME_MESSAGE }),
  status: z.nativeEnum(WORK_CENTER_STATUS, { message: STATUS_MESSAGE }),
  lastCheckedAt: z.custom<DateObject>(value => isValidDateObject(value), {
    message: 'Vui lòng chọn ngày',
  }),
  note: z.string().optional(),
});

export type ProductionWorkCenterFormSchema = z.infer<
  typeof productionWorkCenterSchema
>;
