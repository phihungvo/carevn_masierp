import dayjs from 'dayjs';
import { z } from 'zod';
import { DateObject } from 'react-multi-date-picker';

import { isValidDateObject } from './custom.validation';
import { LEAVE_REQUEST_DAY_TYPE, LEAVE_REQUEST_TYPE } from 'app/shared/model/enumerations/leave-request.model';

const LEAVE_REQUEST_DAY_TYPE_MESSAGE = 'Vui lòng chọn loại ngày nghỉ';
// const FROM_DATE_MESSAGE = 'Vui lòng chọn ngày cuối cùng làm việc';
// const TO_DATE_MESSAGE = 'Vui lòng chọn ngày trở lại làm việc';
const FROM_TIME_MESSAGE = 'Vui lòng chọn thời gian bắt đầu';
const TO_TIME_MESSAGE = 'Vui lòng chọn thời gian kết thúc';
// const DIFF_TO_FROM_DATE_HALF_MESSAGE = 'Ngày trở lại làm việc phải sau ngày cuối cùng làm việc ít nhất 1 ngày';
// const DIFF_TO_FROM_DATE_FULL_MESSAGE = 'Ngày trở lại làm việc phải sau ngày cuối cùng làm việc ít nhất 2 ngày';


export const leaveRegimeSchema = z
  .object({
    leaveType: z.nativeEnum(LEAVE_REQUEST_TYPE, { message: 'Vui lòng chọn loại nghỉ phép' }),
    lastWorkDate: z.custom<DateObject>(value => isValidDateObject(value), { message: 'Vui lòng chọn ngày hợp lệ' }),
    returnWorkDate: z.custom<DateObject>(value => isValidDateObject(value), {
      message: 'Vui lòng chọn ngày hợp lệ',
    }),
    leaveDays: z.string().optional(),
    substituteId: z.string().optional(),
    employeeId: z.string({ message: 'Vui lòng chọn nhân viên' }),
    leaveRegisterDayType: z.nativeEnum(LEAVE_REQUEST_DAY_TYPE, { message: LEAVE_REQUEST_DAY_TYPE_MESSAGE }),
    fromTime: z.string({ message: FROM_TIME_MESSAGE }).optional(),
    toTime: z.string({ message: TO_TIME_MESSAGE }).optional(),
  })
// .refine(
//   data => {
//     const lastWorkDate = dayjs(data.lastWorkDate.toDate()).startOf('date');
//     const returnWorkDate = dayjs(data.returnWorkDate.toDate()).startOf('date');

//     const diffDate = returnWorkDate.diff(lastWorkDate, 'day');

//     return diffDate > 1;
//   },
//   {
//     message: 'Tổng số ngày nghỉ phải lớn hơn 0',
//     path: ['leaveDays'],
//   },
// );

export const processLeaveRegimeSchema = z.object({
  employeeId1: z.string({ message: 'Vui lòng chọn người duyệt 1' }),
  employeeId2: z.string().optional(),
  employeeId3: z.string().optional(),
  employeeId4: z.string().optional(),
});

export const rejectLeaveRegimeSchema = z.object({
  reason: z.string({ message: 'Vui lòng nhập lý do' }).max(200, { message: 'Lý do từ chối không được vượt quá 200 ký tự' }),
});

export type LeaveRegimeFormSchema = z.infer<typeof leaveRegimeSchema>;
export type ProcessLeaveRegimeSchema = z.infer<typeof processLeaveRegimeSchema>;
export type RejectLeaveRegimeSchema = z.infer<typeof rejectLeaveRegimeSchema>;
