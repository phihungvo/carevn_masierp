import { LEAVE_REQUEST_DAY_TYPE, LEAVE_REQUEST_TYPE } from 'app/shared/model/enumerations/leave-request.model';
import dayjs from 'dayjs';
import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';

const FROM_DATE_MESSAGE = 'Vui lòng chọn ngày cuối cùng làm việc';
const TO_DATE_MESSAGE = 'Vui lòng chọn ngày trở lại làm việc';
const REASON_MESSAGE = 'Vui lòng nhập lý do';
const REASON_MAX_LENGTH_MESSAGE = 'Lý do không được vượt quá 200 ký tự';
const LEAVE_REQUEST_TYPE_MESSAGE = 'Vui lòng chọn loại nghỉ phép';
const LEAVE_REQUEST_DAY_TYPE_MESSAGE = 'Vui lòng chọn loại ngày nghỉ';
const CENSOR_MESSAGE = 'Vui lòng chọn người duyệt';
const SUBSTITUTE_MESSAGE = 'Vui lòng chọn người thay thế';
const FROM_TIME_MESSAGE = 'Vui lòng chọn thời gian bắt đầu';
const TO_TIME_MESSAGE = 'Vui lòng chọn thời gian kết thúc';
const DIFF_TO_FROM_DATE_FULL_MESSAGE = 'Ngày trở lại làm việc phải sau ngày cuối cùng làm việc ít nhất 2 ngày';
const DIFF_TO_FROM_DATE_HALF_MESSAGE = 'Ngày trở lại làm việc phải sau ngày cuối cùng làm việc ít nhất 1 ngày';

export const leaveRequestSchema = z
  .object({
    fromDate: z.custom<DateObject>(value => isValidDateObject(value), { message: FROM_DATE_MESSAGE }),
    // .refine(data => !dayjs(data.toDate()).isBefore(dayjs().add(2, 'day')), {
    //   message: 'Ngày cuối cùng làm việc phải sau ngày hiện tại 3 ngày',
    // }),
    toDate: z.custom<DateObject>(value => isValidDateObject(value), { message: TO_DATE_MESSAGE }),
    fromTime: z.string({ message: FROM_TIME_MESSAGE }).optional(),
    toTime: z.string({ message: TO_TIME_MESSAGE }).optional(),
    reason: z.string({ message: REASON_MESSAGE }).max(200, { message: REASON_MAX_LENGTH_MESSAGE }).optional(),
    leaveRequestType: z.nativeEnum(LEAVE_REQUEST_TYPE, { message: LEAVE_REQUEST_TYPE_MESSAGE }),
    leaveRequestDayType: z.nativeEnum(LEAVE_REQUEST_DAY_TYPE, { message: LEAVE_REQUEST_DAY_TYPE_MESSAGE }),
    substituteId: z.string({ message: SUBSTITUTE_MESSAGE }).optional(),
    fileAttachment: z.string().optional(),
    fileAttachmentContentType: z.string().optional(),
    reviewerIds: z.string({ message: CENSOR_MESSAGE }),
    remains: z.string().optional(),
    emergency: z.boolean().optional(),
    leaveDiff: z.string().optional(),
  })
  .refine(
    data => {
      const diff = data.toDate.toDate().getDate() - data.fromDate.toDate().getDate();
      const diffMonth = data.toDate.toDate().getMonth() - data.fromDate.toDate().getMonth();
      const diffYear = data.toDate.toDate().getFullYear() - data.fromDate.toDate().getFullYear();

      if (diffYear < 0) {
        return false;
      }

      if (diffYear > 0) {
        return true;
      }

      if (diffMonth < 0) {
        return false;
      }

      if (diffMonth > 0) {
        return true;
      }

      if (data?.leaveRequestDayType === LEAVE_REQUEST_DAY_TYPE.FULL_DAY) {
        return diff >= 2;
      }

      return true;
    },
    {
      message: DIFF_TO_FROM_DATE_FULL_MESSAGE,
      path: ['toDate'],
    },
  )
  .refine(
    data => {
      const diff = data.toDate.toDate().getDate() - data.fromDate.toDate().getDate();
      const diffMonth = data.toDate.toDate().getMonth() - data.fromDate.toDate().getMonth();
      const diffYear = data.toDate.toDate().getFullYear() - data.fromDate.toDate().getFullYear();

      if (diffYear < 0) {
        return false;
      }

      if (diffYear > 0) {
        return true;
      }

      if (diffMonth < 0) {
        return false;
      }

      if (diffMonth > 0) {
        return true;
      }

      if (data?.leaveRequestDayType === LEAVE_REQUEST_DAY_TYPE.HALF_DAY) {
        return diff >= 1;
      }

      return true;
    },
    {
      message: DIFF_TO_FROM_DATE_HALF_MESSAGE,
      path: ['toDate'],
    },
  )
  .refine(
    data => {
      if (
        data?.leaveRequestType === LEAVE_REQUEST_TYPE.FUNERAL_LEAVE ||
        data?.leaveRequestType === LEAVE_REQUEST_TYPE.SICK_LEAVE ||
        data?.emergency
      ) {
        return true;
      }

      return !dayjs(data.fromDate.toDate()).isBefore(dayjs());
    },
    {
      message: 'Ngày nghỉ phép phải sau ngày hiện tại 1 ngày',
      path: ['fromDate'],
    },
  );

export const leaveRequestRejectSchema = z.object({
  reason: z.string({ message: REASON_MESSAGE }).max(200, { message: REASON_MAX_LENGTH_MESSAGE }),
});

export type LeaveRequestFormSchema = z.infer<typeof leaveRequestSchema>;
export type LeaveRequestRejectFormSchema = z.infer<typeof leaveRequestRejectSchema>;
