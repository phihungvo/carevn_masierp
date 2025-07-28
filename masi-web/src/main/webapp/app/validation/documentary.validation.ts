import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';
import { isValidDateObject } from './custom.validation';
import { DOCUMENTARY_GROUP, DOCUMENTARY_TYPE } from 'app/shared/model/enumerations/documentary';

const DOCUMENT_NUMBER_MESSAGE = 'Vui lòng nhập số công văn';
const DATE_START_MESSAGE = 'Vui lòng chọn ngày';
const GROUP_MESSAGE = 'Vui lòng chọn nhóm công văn';
const TYPE_MESSAGE = 'Vui lòng chọn loại công văn';
const CONTENT_MESSAGE = 'Vui lòng nhập nội dung';
const SIGNER_MESSAGE = 'Vui lòng chọn người ký';
const RECIPIENT_MESSAGE = 'Vui lòng nhập nơi nhận';
const ARCHIVE_LOCATION_MESSAGE = 'Vui lòng nhập nơi lưu hồ sơ';

export const documentarySchema = z.object({
  documentNumber: z.string({ message: DOCUMENT_NUMBER_MESSAGE }).refine(value => value.trim() !== '', { message: DOCUMENT_NUMBER_MESSAGE }),
  dateStart: z.custom<DateObject>(value => isValidDateObject(value), { message: DATE_START_MESSAGE }),
  group: z.nativeEnum(DOCUMENTARY_GROUP, { message: GROUP_MESSAGE }),
  type: z.nativeEnum(DOCUMENTARY_TYPE, { message: TYPE_MESSAGE }),
  content: z.string({ message: CONTENT_MESSAGE }).refine(value => value.trim() !== '', { message: CONTENT_MESSAGE }),
  signer: z.string({ message: SIGNER_MESSAGE }).refine(value => value.trim() !== '', { message: SIGNER_MESSAGE }),
  recipient: z.string({ message: RECIPIENT_MESSAGE }).refine(value => value.trim() !== '', { message: RECIPIENT_MESSAGE }),
  archiveLocation: z
    .string({ message: ARCHIVE_LOCATION_MESSAGE })
    .refine(value => value.trim() !== '', { message: ARCHIVE_LOCATION_MESSAGE }),
  senderOrReceiver: z.string().optional(),
});

export const rejectDocumentarySchema = z.object({
  rejectNote: z
    .string({ message: 'Vui lòng nhập lý do từ chối' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập lý do từ chối' }),
});

export type DocumentarySchema = z.infer<typeof documentarySchema>;
export type RejectDocumentarySchema = z.infer<typeof rejectDocumentarySchema>;
