import { GENDER, RECRUITMENT_CONTRACT_TYPE, RECRUITMENT_POSITION } from 'app/shared/model/enumerations/recruitment.model';
import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';
import { EMPLOYEE_STATUS } from 'app/shared/model/enumerations/employee.model';
import { VIETNAMESE_PHONE_NUMBER_REGEX } from 'app/constants/common';

export const employeeSchema = z.object({
  employeeCode: z.string().optional(),
  fullName: z.string({ message: 'Vui lòng nhập tên NV' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập tên NV' }),
  gender: z.nativeEnum(GENDER, { message: 'Vui lòng chọn giới tính' }),
  workspaceId: z.string({ message: 'Vui lòng chọn BP / Nhà máy' }),
  citizenId: z.string({ message: 'Vui lòng nhập CCCD' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập CCCD' }),
  citizenIssueDate: z.custom<DateObject>().optional(),
  citizenIssuePlace: z.string().optional(),
  residenceAddress: z.string().optional(),
  temporaryAddress: z.string().optional(),
  birthday: z.custom<DateObject>().optional(),
  phone: z.string().optional(),
  taxCode: z.string().optional(),
  startWorkDate: z.custom<DateObject>(value => isValidDateObject(value), { message: 'Vui lòng chọn ngày vào làm' }),
  role: z.string({ message: 'Vui lòng nhập chức vụ' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập chức vụ' }),
  position: z.nativeEnum(RECRUITMENT_POSITION, { message: 'Vui lòng chọn vị trí' }),
  bankCode: z.string().optional(),
  bankNumber: z.string().optional(),
  contractType: z.nativeEnum(RECRUITMENT_CONTRACT_TYPE, { message: 'Vui lòng chọn loại HĐ' }),
  contractTerm: z.string().optional(),
  contractNumber: z.string({ message: 'Vui lòng nhập số HĐ' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập số HĐ' }),
  contractDate: z.custom<DateObject>(value => isValidDateObject(value), { message: 'Vui lòng chọn ngày HĐ' }),
  contractEndDate: z.custom<DateObject>().optional(),
  level: z.string().optional(),
  parkingCard: z.string().optional(),
  insuranceCard: z.string().optional(),
  referrerId: z.string().optional(),
  referrerDate: z.custom<DateObject>().optional(),
  email: z.string().email().optional(),
  note: z.string().optional(),
  status: z.nativeEnum(EMPLOYEE_STATUS).optional(),
  probationDate: z.any().optional(),
  officialWorkTypeDuration: z.any().optional(),
  insurancePaymentLevel: z.any().optional(),
});

export const confirmLeaveSchema = z.object({
  submissionDate: z.custom<DateObject>(value => isValidDateObject(value), { message: 'Vui lòng chọn ngày nộp đơn nghỉ việc' }),
  reason: z.string({ message: 'Vui lòng nhập lý do nghỉ' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập lý do nghỉ' }),
  recruitmentSolution: z.string().optional(),
  hrSolution: z.string().optional(),
  leaveDate: z.custom<DateObject>(value => isValidDateObject(value), { message: 'Vui lòng chọn ngày nghỉ việc' }),
});

export type EmployeeFormSchema = z.infer<typeof employeeSchema>;
export type ConfirmLeaveFormSchema = z.infer<typeof confirmLeaveSchema>;
