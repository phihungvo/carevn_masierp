import dayjs from 'dayjs';
import { z } from 'zod';

import {
  INTERVIEW_MODE,
  INTERVIEW_RESULT,
  RECRUITMENT_CONTRACT_TYPE,
  RECRUITMENT_POSITION,
  RECRUITMENT_PROCESS,
} from 'app/shared/model/enumerations/recruitment.model';
import { DateObject } from 'react-multi-date-picker';
import { isValidDateObject } from './custom.validation';

const REQUEST_DESCRIPTION_MESSAGE = 'Vui lòng chọn bộ phận yêu cầu';
const POSITION_MESSAGE = 'Vui lòng chọn vị trí';
const JOB_TITLE_MESSAGE = 'Vui lòng nhập chức vụ';
const QUANTITY_MESSAGE = 'Vui lòng nhập số lượng';
const LEVEL_MESSAGE = 'Vui lòng chọn cấp bậc';
const WAGE_MESSAGE = 'Vui lòng nhập mức lương';
const START_DATE_MESSAGE = 'Vui lòng chọn ngày hợp lệ';
const RECRUITMENT_PURPOSES_MESSAGE = 'Vui lòng chọn mục đích tuyển dụng';

export const recruitmentSchema = z.union([
  z.object({
    departmentId: z
      .string({ message: REQUEST_DESCRIPTION_MESSAGE })
      .refine(value => value.trim() !== '', { message: REQUEST_DESCRIPTION_MESSAGE }),
    position: z.nativeEnum(RECRUITMENT_POSITION, { message: POSITION_MESSAGE }),
    jobTitle: z.string({ message: JOB_TITLE_MESSAGE }).refine(value => value.trim() !== '', { message: JOB_TITLE_MESSAGE }),
    quantity: z.string({ message: QUANTITY_MESSAGE }).refine(value => value.trim() !== '', { message: QUANTITY_MESSAGE }),
    level: z.string({ message: LEVEL_MESSAGE }).refine(value => value.trim() !== '', { message: LEVEL_MESSAGE }),
    wage: z.string({ message: WAGE_MESSAGE }).refine(value => value.trim() !== '', { message: WAGE_MESSAGE }),
    startDate: z.custom<DateObject>(value => isValidDateObject(value), { message: START_DATE_MESSAGE }),
    deadline: z.custom<DateObject>(value => isValidDateObject(value), { message: START_DATE_MESSAGE }),
    deadlineOld: z.custom<DateObject>(value => isValidDateObject(value), { message: START_DATE_MESSAGE }).optional(),
    recruitmentPurposes: z
      .string({ message: RECRUITMENT_PURPOSES_MESSAGE })
      .refine(value => value.trim() !== '', { message: RECRUITMENT_PURPOSES_MESSAGE }),
    requestNotes: z
      .string({ message: 'Vui lòng nhập yêu cầu cho ứng viên' })
      .refine(value => value !== '', { message: 'Vui lòng nhập yêu cầu cho ứng viên' }),
    description: z
      .string({ message: 'Vui lòng nhập mô tả công việc' })
      .refine(value => value !== '', { message: 'Vui lòng nhập mô tả công việc' }),
    contractType: z.nativeEnum(RECRUITMENT_CONTRACT_TYPE, {
      message: 'Vui lòng chọn loại HĐ',
    }),
    salaryUnit: z.string().optional(),
    salaryUnitNote: z.string().optional(),
    replaceForId: z.any().optional(),
  }),
  z.object({
    departmentId: z
      .string({ message: REQUEST_DESCRIPTION_MESSAGE })
      .refine(value => value.trim() !== '', { message: REQUEST_DESCRIPTION_MESSAGE }),
    position: z.nativeEnum(RECRUITMENT_POSITION, { message: POSITION_MESSAGE }),
    jobTitle: z.string({ message: JOB_TITLE_MESSAGE }).refine(value => value.trim() !== '', { message: JOB_TITLE_MESSAGE }),
    quantity: z.string({ message: QUANTITY_MESSAGE }).refine(value => value.trim() !== '', { message: QUANTITY_MESSAGE }),
    level: z.string({ message: LEVEL_MESSAGE }).refine(value => value.trim() !== '', { message: LEVEL_MESSAGE }),
    wage: z.string({ message: WAGE_MESSAGE }).refine(value => value.trim() !== '', { message: WAGE_MESSAGE }),
    startDate: z.custom<DateObject>(value => isValidDateObject(value), { message: START_DATE_MESSAGE }),
    deadline: z.custom<DateObject>(value => isValidDateObject(value), { message: START_DATE_MESSAGE }),
    deadlineOld: z.custom<DateObject>(value => isValidDateObject(value), { message: START_DATE_MESSAGE }).optional(),
    recruitmentPurposes: z
      .string({ message: RECRUITMENT_PURPOSES_MESSAGE })
      .refine(value => value.trim() !== '', { message: RECRUITMENT_PURPOSES_MESSAGE }),
    requestNotes: z
      .string({ message: 'Vui lòng nhập yêu cầu cho ứng viên' })
      .refine(value => value !== '', { message: 'Vui lòng nhập yêu cầu cho ứng viên' }),
    description: z
      .string({ message: 'Vui lòng nhập mô tả công việc' })
      .refine(value => value !== '', { message: 'Vui lòng nhập mô tả công việc' }),
    contractType: z.nativeEnum(RECRUITMENT_CONTRACT_TYPE, {
      message: 'Vui lòng chọn loại HĐ',
    }),
    salaryUnit: z.string().optional(),
    salaryUnitNote: z.string().optional(),
    replaceForId: z
      .string({ message: 'Vui lòng chọn người thay thế' })
      .refine(value => value.trim() !== '', { message: 'Vui lòng chọn người thay thế' }),
  }),
]).refine(
  data => {
    if (data.recruitmentPurposes === 'Thay thế') {
      return data.replaceForId !== undefined && data.replaceForId.trim() !== '';
    }

    return true;
  },
  {
    message: 'Vui lòng chọn người thay thế',
    path: ['replaceForId'],
  },
);

export const recruitmentRejectSchema = z.object({
  rejectNote: z.string({ message: 'Vui lòng nhập lý do từ chối' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập lý do từ chối' }),
});

export const interviewSchema = z.object({
  cvFile: z.string().optional(),
  candidateName: z
    .string({ message: 'Vui lòng nhập tên ứng viên' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập tên ứng viên' }),
  interviewDate: z.custom<DateObject>(value => isValidDateObject(value), { message: 'Vui lòng chọn ngày' }),
  interviewTime: z.string({ message: 'Vui lòng nhập giờ' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập giờ' }),
  interviewerId: z
    .string({ message: 'Vui lòng chọn người phỏng vấn' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng chọn người phỏng vấn' }),
  process: z.nativeEnum(RECRUITMENT_PROCESS).optional(),
  interviewMode: z.nativeEnum(INTERVIEW_MODE).optional(),
  email: z.string().email({ message: 'Email không hợp lệ' }).optional(),
  phoneNumber: z.string().optional(),
});

export const interviewResultSchema = z.object({
  interviewResult: z.nativeEnum(INTERVIEW_RESULT, { message: 'Vui lòng chọn kết quả' }),
  rate: z.string({ message: 'Vui lòng nhập đánh giá' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập đánh giá' }),
});

export const recruitmentRenewSchema = z
  .object({
    deadline: z.custom<DateObject>(value => isValidDateObject(value), { message: START_DATE_MESSAGE }),
    deadlineOld: z.custom<DateObject>(value => isValidDateObject(value), { message: START_DATE_MESSAGE }).optional(),
    employeeId1: z.string({ message: 'Vui lòng chọn người duyệt 1' }),
    employeeId2: z.string().optional(),
    employeeId3: z.string().optional(),
    employeeId4: z.string().optional(),
  })
  .refine(
    data => {
      const deadline = dayjs(new DateObject(data?.deadline)?.toDate());
      const deadlineOld = dayjs(new DateObject(data?.deadlineOld)?.toDate());
      const today = dayjs();

      if (deadline.isSame(deadlineOld, 'day') && deadline.isSame(deadlineOld, 'month') && deadline.isSame(deadlineOld, 'year')) return false;
      if (deadline.isBefore(deadlineOld)) return false;
      if (deadline.isBefore(today, 'day')) return false;

      return true;
    },
    {
      message: START_DATE_MESSAGE,
      path: ['deadline'],
    },
  ).refine(
    data => {
      if (!data?.employeeId2) return true;
      if (data?.employeeId2 === data?.employeeId1 || data?.employeeId2 === data?.employeeId3 || data?.employeeId2 === data?.employeeId4) return false;
      return true;
    },
    {
      message: 'Không thể chọn trùng người duyệt!',
      path: ['employeeId2'],
    },
  ).refine(
    data => {
      if (!data?.employeeId3) return true;
      if (data?.employeeId3 === data?.employeeId1 || data?.employeeId3 === data?.employeeId2 || data?.employeeId3 === data?.employeeId4) return false;
      return true;
    },
    {
      message: 'Không thể chọn trùng người duyệt!',
      path: ['employeeId3'],
    },
  ).refine(
    data => {
      if (!data?.employeeId4) return true;
      if (data?.employeeId4 === data?.employeeId1 || data?.employeeId4 === data?.employeeId2 || data?.employeeId4 === data?.employeeId3) return false;
      return true;
    },
    {
      message: 'Không thể chọn trùng người duyệt!',
      path: ['employeeId4'],
    },
  );

export type RecruitmentFormSchema = z.infer<typeof recruitmentSchema>;
export type RecruitmentRenewFormSchema = z.infer<typeof recruitmentRenewSchema>;
export type RecruitmentRejectSchema = z.infer<typeof recruitmentRejectSchema>;
export type InterviewSchema = z.infer<typeof interviewSchema>;
export type InterviewResultSchema = z.infer<typeof interviewResultSchema>;
