import { z } from 'zod';

import { DateObject } from 'react-multi-date-picker';
import { isValidDateObject } from 'app/validation/custom.validation';
import { WORKSPACE_TYPE } from 'app/shared/model/enumerations/workspace.model';

export const leaveTrackingSchema = z.object({
  workspaceType: z.nativeEnum(WORKSPACE_TYPE, { message: 'Vui lòng chọn loại' }),
  year: z.custom<DateObject>(value => isValidDateObject(value), { message: 'Vui lòng chọn năm' }),
});

export type LeaveTrackingFormSchema = z.infer<typeof leaveTrackingSchema>;
