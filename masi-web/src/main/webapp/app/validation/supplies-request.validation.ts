import { z } from 'zod';

import { IItem } from 'app/shared/model/item.model';
import { DateObject } from 'react-multi-date-picker';
import { isValidDateObject } from './custom.validation';
import { min } from 'lodash';

const uomDTOSchema = z.object({
  id: z.string().uuid(),
  name: z.string(),
});

export const suppliesItemDTOSchema = z.object({
  idItem: z.string().uuid().optional(),
  idUom: z.string().uuid().optional(),
  uomDTO: uomDTOSchema.optional(),
  suppliesId: z.string().uuid().optional(),
  quantity: z.number().optional(),
  price: z.number().optional(),
  image: z.array(z.string()).optional(),
  bankInfo: z.string().optional(),
  note: z.string().optional(),
}).optional();

export type SuppliesItemDTOSchemaType = z.infer<typeof suppliesItemDTOSchema>;

const fileSchema = z.object({
  id: z.string().uuid(),
  fileName: z.string(),
});

const supplierContractDetailsSchema = z.object({});

const supplierContractSchema = z.object({
  id: z.string().uuid(),
  contractCode: z.string(),
  contractName: z.string(),
  supplierId: z.string().uuid(),
  contractDate: z.string().datetime(),
  endDate: z.string().datetime(),
  note: z.string(),
  attachments: z.record(z.any()),
  supplierContractDetails: z.array(supplierContractDetailsSchema),
  status: z.string(),
  files: z.array(fileSchema),
}).deepPartial();

export type SupplierContract = z.infer<typeof supplierContractSchema>

export const requestApprovalSchemaOptional = z.object({
  requestApprovals: z.array(z.record(z.any())).optional()
})

export const requestApprovalSchemaRequire = z.object({
  requestApprovals: z.array(z.record(z.any())).min(1, {
    message: 'Vui lòng chọn ít nhất 1 người ký'
  })
})

export const attachments = z.array(z.object({
  fileId: z.string(),
  fileName: z.string(),
  createdAt: z.coerce.date(),
})).optional()

export const convertedGoods = z.object({
  itemDTO: z.object({
    id: z.string({
      message: 'Vui lòng chọn sản phẩm'
    }),
    name: z.string(),
    code: z.string(),
    label: z.string().optional(),
  }, {
    message: 'Vui lòng chọn sản phẩm'
  }),
  uomDTO: z.object({
    label: z.string(),
    value: z.string(),
  }, {
    message: 'Vui lòng chọn đơn vị tính'
  }),
  quantity: z.coerce.number({
    message: 'Số lượng là bắt buộc và phải là số'
  }).min(1, {
    message: 'Số lượng phải lớn hơn 0'
  }),
  price: z.coerce.number({
    message: 'Đơn giá là bắt buộc và phải là số'
  }),
  totalItem: z.coerce.number(),
  totalItemAfterVat: z.number(),
  vatDTO: z.object({
    label: z.string(),
    value: z.string(),
    percent: z.coerce.number(),
  }, {
    message: 'Vui lòng chọn thuế VAT'
  }),
  note: z.string().optional(),
});

export const importConvertedGoods = z.object({
  feesBeforeImport: z.coerce.number().optional(),
  importPercent: z.coerce.number().positive({
    message: 'Phần trăm nhập khẩu phải lớn hơn 0'
  }).max(100, {
    message: 'Phần trăm nhập khẩu không được lớn hơn 100'
  }).optional(),
  importTax: z.coerce.number().optional(),
  envPercent: z.coerce.number().positive({
    message: 'Phần trăm thuế môi trường phải lớn hơn 0'
  }).max(100, {
    message: 'Phần trăm thuế môi trường không được lớn hơn 100'
  }).optional(),
  envTax: z.coerce.number().optional(),
  feesAfterImport: z.coerce.number().optional(),
}).merge(convertedGoods);

export const selectedGoods = z.object({
  arr: z.array(
    z.object({
      id: z.string(),
    }),
  ),
  obj: z.record(z.string(), convertedGoods),
  total: z.number(),
  totalRoot: z.number(),
})

export const importSelectedGoods = z.object({
  arr: z.array(
    z.object({
      id: z.string(),
    }),
  ),
  obj: z.record(z.string(), importConvertedGoods),
  total: z.number(),
  totalRoot: z.number(),
})

export type ConvertedGoodsType = z.infer<typeof convertedGoods>;

export type ImportConvertedGoodsType = z.infer<typeof importConvertedGoods>;

export const suppliersRequest = z.object({
  code: z.string().optional(),
  requestNumber: z.string().optional(),
  requestDate: z.custom<DateObject>(value => isValidDateObject(value), {
    message: 'Vui lòng chọn ngày lập',
  }),
  requestByEmployeeId: z.string().uuid().optional(),
  departmentId: z.string().uuid().optional(),
  requestStatus: z.enum(['NEW']).optional(),
  status: z.string().optional(),
  totalAmount: z.number().optional(),
  vat: z.number().optional(),
  totalAmountAfterVat: z.number().optional(),
  note: z.string().optional(),
  createdByEmployeeId: z.string().uuid().optional(),
  createdDateByEmployee: z
    .custom<DateObject>(value => isValidDateObject(value), {
      message: 'Vui lòng chọn ngày đề nghị',
    })
    .optional(),
  context: z.string().optional(),
  issueDate: z.string().datetime().optional(),
  contractId: z.string().optional(),
  contractCode: z.string().optional(),
  contractContent: z.string().optional(),
  contractDate: z.string().datetime().optional(),
  supplierId: z.string().uuid().optional(),
  taxNumber: z.string().optional(),
  tel: z.string().optional(),
  paymentMethod: z.string().optional(),
  currency: z.string().optional(),
  exchangeRate: z.number().optional(),
  warehouseId: z.string().optional(),
  requestTypeId: z.string({
    message: 'Vui lòng chọn loại đề xuất',
  }),
  deliveredQuantity: z.number({ coerce: true }).optional(),
  remainingQuantity: z.number({ coerce: true }).optional(),
  attachedFiles: attachments,
  suppliesItemDTO: z.array(suppliesItemDTOSchema).optional(),
  supplierContracts: z.array(z.object({
    contractCode: z.string().optional(),
  })).optional(),
  invoiceSupplies: z.array(convertedGoods).min(1, {
    message: 'Vui lòng chọn ít nhất 1 danh sách hàng hóa',
  }),
  totalInvoice: z.coerce.number().optional(),
  tmp: z.object({
    employeeName: z.string().optional(),
    staffName: z.string().optional(),
    isWaiting: z.boolean().default(false),
    isApproved: z.boolean().default(false),
    isCanNotEdit: z.boolean().default(false),
  }),

  supplierTaxCode: z.any().optional(),
  supplierAddress: z.any().optional(),
  supplierFullName: z.any().optional(),
  supplierPosition: z.any().optional(),
  supplierPhone: z.any().optional(),
  supplierEmail: z.any().optional(),

});

const suppliersRequestWithoutApproval = suppliersRequest.merge(requestApprovalSchemaOptional);
const suppliersRequestRequireApproval = suppliersRequest.merge(requestApprovalSchemaRequire);

export const suppliersRequestSchemaV2 = z.discriminatedUnion('isDoNotSign', [
  z.object({
    isDoNotSign: z.literal(true),
  }).merge(suppliersRequestWithoutApproval),
  z.object({
    isDoNotSign: z.literal(false)
  }).merge(suppliersRequestRequireApproval)
])

export type SuppliersRequestSchemaV2Type = z.infer<typeof suppliersRequestSchemaV2>;

// ------------------------------------------------------------------
// ------------------------------------------------------------------

export const suppliesRequestSchema = z.object({
  code: z.string({ message: 'Vui lòng nhập số chứng từ' }).refine(value => value.trim() !== '', { message: 'Vui lòng nhập số chứng từ' }),
  requestDate: z.custom<DateObject>().optional(),
  requestByEmployeeId: z.string().optional(),
  totalAmount: z.string().optional(),
  note: z.string().optional(),
  vat: z.string().optional(),
  totalAmountAfterVat: z.string().optional(),

  supplierTaxCode: z.any().optional(),
  supplierAddress: z.any().optional(),
  supplierFullName: z.any().optional(),
  supplierPosition: z.any().optional(),
  supplierPhone: z.any().optional(),
  supplierEmail: z.any().optional(),

  suppliesItemDTO: z.array(
    z.object({
      id: z.string().optional(),
      idSuppliesRequest: z.string().optional(),
      idItem: z.string().optional(),
      quantity: z.string().optional(),
      price: z.string().optional(),
      basePrice: z.string().optional(),
      company: z.string().optional(),
      department: z.string().optional(),
      suppliesId: z.string().optional(),
      imageIds: z.string().optional(),
      image: z.array(z.string()).optional(),
      finalPrice: z.string().optional(),
      item: z.custom<IItem>().optional(),
    }).optional(),
  ).optional(),
});

const VALIDATION_MESSAGE = 'Phải chọn ít nhất 1 người dùng';

export const suppliesRequestReviewedSchema = z.object({
  employees: z.array(z.string().optional()).optional(),
}).refine(data => data?.employees[0] !== undefined, {
  message: VALIDATION_MESSAGE,
  path: ['employees.0']
});

export const suppliesRequestApproveSchema = z.object({
  documentId: z.string().optional(),
  rejectNote: z.string().optional(),
  result: z.boolean().optional(),
});

export type SuppliesRequestFormSchema = z.infer<typeof suppliesRequestSchema>;
export type SuppliesRequestReviewedSchema = z.infer<typeof suppliesRequestReviewedSchema>;
export type SuppliesRequestApproveSchema = z.infer<typeof suppliesRequestApproveSchema>;

