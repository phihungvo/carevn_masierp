import { CONTRACT_STATUS, CONTRACT_TYPE } from 'app/shared/model/enumerations/contract.model';
import { DateObject } from 'react-multi-date-picker';
import { optional, z } from 'zod';
import { isValidDateObject } from './custom.validation';
import { UNIT } from 'app/shared/model/enumerations/unit.model';
import { convertDate } from 'app/shared/util/convertDate';

const CONTRACT_NAME_MESSAGE = 'Vui lòng nhập tên HĐ';
const CUSTOMER_ID = 'Vui lòng chọn công ty';
const CONTRACT_TYPE_MESSAGE = 'Vui lòng chọn loại HĐ';
const CONTRACT_VALID_FROM_MESSAGE = 'Vui lòng chọn thời hạn HĐ từ';
const CONTRACT_VALID_TO_MESSAGE = 'Vui lòng chọn thời hạn HĐ đến';
const CONTRACT_TOTAL_MESSAGE = 'Vui lòng nhập giá trị HĐ';
const CONTRACT_STATUS_MESSAGE = 'Vui lòng chọn tình trạng HĐ';
const CONTRACT_OWNER_MESSAGE = 'Vui lòng chọn người phụ trách';
const CONTRACT_PROTEIN_MESSAGE = 'Vui lòng nhập thông số đạm';
// const DELIVERY_TERM_MESSAGE = 'Vui lòng nhập thời hạn giao hàng';

export const contractSchema = z
  .object({
    contractName: z.string({ message: CONTRACT_NAME_MESSAGE }).refine(value => value.trim() !== '', { message: CONTRACT_NAME_MESSAGE }),
    contractType: z.nativeEnum(CONTRACT_TYPE, { message: CONTRACT_TYPE_MESSAGE }),
    contractTotal: z.string({ message: CONTRACT_TOTAL_MESSAGE }).refine(
      value => {
        const floatValue = parseFloat(value);
        return !isNaN(floatValue) && floatValue >= 0;
      },
      {
        message: CONTRACT_TOTAL_MESSAGE,
      },
    ),
    status: z.nativeEnum(CONTRACT_STATUS, { message: CONTRACT_STATUS_MESSAGE }),
    contractOwner: z.string({ message: CONTRACT_OWNER_MESSAGE }),
    proteinPercent: z.string({ message: CONTRACT_PROTEIN_MESSAGE }).optional(),
    customerId: z.string({ message: CUSTOMER_ID }).refine(value => value.trim() !== '', { message: CUSTOMER_ID }),
    // deliveryTerm: z.custom<DateObject[]>(value => isValidDateObject(value?.[0]), { message: DELIVERY_TERM_MESSAGE }),
    // payTerm: z
    //   .string({ message: 'Vui lòng nhập thời hạn thanh toán' })
    //   .refine(value => value.trim() !== '', { message: 'Vui lòng nhập thời hạn thanh toán' }),
    // payCondition: z
    //   .string({ message: 'Vui lòng nhập điều kiện thanh toán' })
    //   .refine(value => value.trim() !== '', { message: 'Vui lòng nhập điều kiện thanh toán' }),
    // deliveryLocation: z
    //   .string({ message: 'Vui lòng nhập địa điểm nhận hàng' })
    //   .refine(value => value.trim() !== '', { message: 'Vui lòng nhập địa điểm nhận hàng' }),
    monetaryUnit: z.nativeEnum(UNIT, { message: 'Vui lòng chọn đơn vị' }),
    exchangeRate: z.string({ message: 'Vui lòng nhập tỷ giá' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập tỷ giá' }),
    additives: z
      .array(
        z.object({
          idMaterial: z
            .string({ message: 'Vui lòng chọn nguyên liệu' })
            .refine(value => value.trim() !== '', { message: 'Vui lòng chọn nguyên liệu' }),
          price: z.string({ message: 'Vui lòng nhập đơn giá' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập đơn giá' }),
          quantity: z
            .string({ message: 'Vui lòng nhập số lượng' })
            .refine(value => value.trim() !== '', { message: 'Vui lòng nhập số lượng' }),
          proteinParameters: z
            .string({ message: 'Vui lòng nhập thông số' })
            .refine(value => value.trim() !== '', { message: 'Vui lòng nhập thông số' }),
          nameMaterialNew: z.string().optional(),
          unit: z.string({ message: 'Vui lòng chọn đơn vị' }).refine(value => value.trim() !== '', { message: 'Vui lòng chọn đơn vị' }),
          total: z.any().optional(),
        }),
      )
      .optional(),

    contractValidFrom: z.custom<DateObject>(value => isValidDateObject(value), { message: CONTRACT_VALID_FROM_MESSAGE }),
    contractValidTo: z.custom<DateObject>(value => isValidDateObject(value), { message: CONTRACT_VALID_TO_MESSAGE }),
    quotationId: z.string().optional(),
    // products: z
    //   .array(z.custom<BaseOption>(), { message: 'Vui lòng chọn sản phẩm' })
    //   .refine(value => value.length > 0, { message: 'Vui lòng chọn sản phẩm' })
  })
  // .refine(
  //   data => {
  //     if (!data.deliveryTerm) {
  //       return true;
  //     }

  //     const deliveryTermTo = data.deliveryTerm?.[1] ? data.deliveryTerm?.[1] : data.deliveryTerm?.[0];
  //     const diff = deliveryTermTo.toDate().getDate();
  //     const diffMonth = deliveryTermTo.toDate().getMonth();
  //     const diffYear = deliveryTermTo.toDate().getFullYear();
  //     if (diffYear < 0) {
  //       return false;
  //     }

  //     if (diffYear > 0) {
  //       return true;
  //     }

  //     if (diffMonth < 0) {
  //       return false;
  //     }

  //     if (diffMonth > 0) {
  //       return true;
  //     }

  //     return diff >= 0;
  //   },
  //   {
  //     message: 'Thời gian giao hàng phải ít nhất sớm hơn thời hạn hợp đồng',
  //     path: ['deliveryTerm'],
  //   },
  // )
  .refine(
    data => {
      if (!data.contractValidFrom || !data.contractValidTo) return false;

      const check: boolean = convertDate(data.contractValidFrom, data.contractValidTo);
      return check;
    },
    {
      message: 'Thời gian giao HĐ từ ít nhất sớm hơn thời hạn HĐ đến',
      path: ['contractValidFrom'],
    },
  )
  .refine(
    data => {
      if (!data.contractValidFrom || !data.contractValidTo) return false;

      const check: boolean = convertDate(data.contractValidFrom, data.contractValidTo);
      return check;
    },
    {
      message: 'Thời gian giao HĐ đến lớn nhất sớm hơn thời hạn HĐ từ',
      path: ['contractValidTo'],
    },
  );

export const approveContractSchema = z.object({
  contractStatus: z.nativeEnum(CONTRACT_STATUS, { message: CONTRACT_STATUS_MESSAGE }),
});

export const rejectContractSchema = z.object({
  rejectNote: z
    .string({ message: 'Vui lòng nhập lý do từ chối' })
    .max(200, { message: 'Lý do từ chối không vượt quá 200 ký tự' })
    .refine(value => value.trim() !== '', { message: 'Vui lòng nhập lý do từ chối' }),
});

export const createContractReviewSchema = z.object({
  employeesOne: z.string({ message: 'Vui lòng chọn người duyệt 1' }).refine(value => value.trim() !== '', { message: 'Vui lòng chọn người duyệt 1' }),
  employeesTwo: z.string({ message: 'Vui lòng chọn người duyệt 2' }).refine(value => value.trim() !== '', { message: 'Vui lòng chọn người duyệt 2' }),
}).refine(
  data => {
    if (!data?.employeesTwo) return true;
    if (data?.employeesTwo === data?.employeesOne) return false;
    return true;
  },
  {
    message: 'Không thể chọn trùng người duyệt!',
    path: ['employeesTwo'],
  },
)

export const proposeApproveContractsSchema = z.object({
  employeesOne: z.string({ message: 'Vui lòng chọn người duyệt 1' }).refine(value => value.trim() !== '', { message: 'Vui lòng chọn người duyệt 1' }),
  employeesTwo: z.string().optional(),
  employeesThree: z.string().optional(),
  employeesFour: z.string().optional(),
}).refine(
  data => {
    if (!data?.employeesTwo) return true;
    if (data?.employeesTwo === data?.employeesOne || data?.employeesTwo === data?.employeesThree || data?.employeesTwo === data?.employeesFour) return false;
    return true;
  },
  {
    message: 'Không thể chọn trùng người duyệt!',
    path: ['employeesTwo'],
  },
).refine(
  data => {
    if (!data?.employeesThree) return true;
    if (data?.employeesThree === data?.employeesOne || data?.employeesThree === data?.employeesTwo || data?.employeesThree === data?.employeesFour) return false;
    return true;
  },
  {
    message: 'Không thể chọn trùng người duyệt!',
    path: ['employeesThree'],
  },
).refine(
  data => {
    if (!data?.employeesFour) return true;
    if (data?.employeesFour === data?.employeesOne || data?.employeesFour === data?.employeesTwo || data?.employeesFour === data?.employeesThree) return false;
    return true;
  },
  {
    message: 'Không thể chọn trùng người duyệt!',
    path: ['employeesFour'],
  },
);

export type ContractFormSchema = z.infer<typeof contractSchema>;
export type ApproveContractFormSchema = z.infer<typeof approveContractSchema>;
export type RejectContractFormSchema = z.infer<typeof rejectContractSchema>;
export type CreateContractReviewSchema = z.infer<typeof createContractReviewSchema>
export type ProposeApprovalContractsSchema = z.infer<typeof proposeApproveContractsSchema>