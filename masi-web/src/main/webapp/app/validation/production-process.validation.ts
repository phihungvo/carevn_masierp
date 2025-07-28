import { DateObject } from 'react-multi-date-picker';
import { z } from 'zod';

import { CHECKLIST_TYPE, materialType } from 'app/shared/model/enumerations/production-process.model';
import { IProductionProcess } from 'app/shared/model/production-process.model';
import { isValidDateObject } from './custom.validation';

export const MAX_NOTE_LENGTH = 200;

const DATE_MESSAGE = 'Vui lòng chọn ngày bắt đầu';
const COMMAND_MESSAGE = 'Vui lòng chọn lệnh sản xuất';
const NOTE_MESSAGE = 'Vui lòng nhập lý do';

// Tạo biểu mẫu giám sát nguyên liệu
const CHECK_DATE_MESSAGE = 'Vui lòng chọn ngày kiểm tra';
const PRODUCTION_PROCESS_DATE_MESSAGE = 'Vui lòng chọn ngày sản xuất';
const CHECK_TIME_MESSAGE = 'Vui lòng nhập thời gian kiểm tra';
const WEIGHT_NUMBER_MESSAGE = 'Vui lòng nhập số phiếu cân';
const WEIGHT_MESSAGE = 'Vui lòng nhập số cân';
const TRANSPORT_CONDITION_MESSAGE = 'Vui lòng chọn ĐK phương tiện vận chuyển';
const CHECK_STATUS_MESSAGE = 'Vui lòng chọn kết quả trạng thái sau khi kiểm tra cảm quan';
const CHECK_SMELL_MESSAGE = 'Vui lòng chọn kết quả mùi sau khi kiểm tra cảm quan';
const CHECK_IMPURITY_MESSAGE = 'Vui lòng chọn kết quả tạp chất sau khi kiểm tra cảm quan';
const CHECK_POISON_MESSAGE = 'Vui lòng chọn kết quả kiểm tra các loại cá độc';
const RECEIVER_MESSAGE = 'Vui lòng chọn người tiếp nhận';
const NOTE_MAX_LENGTH_MESSAGE = `Chú thích không được vượt quá ${MAX_NOTE_LENGTH} ký tự`;

// Tạo biểu mẫu giám sát quy trình hấp sấy
const ATM_MESSAGE = 'Vui lòng nhập thông số áp suất';
const TEMP_MESSAGE = 'Vui lòng nhập thông số nhiệt độ';
const TIME_MESSAGE = 'Vui lòng chọn thông số thời gian';
const FIN_PRODUCT_NO_MESSAGE = 'Vui lòng nhập mã thành phẩm';
const EXAMINER_MESSAGE = 'Vui lòng chọn người kiểm tra';

// Tạo biểu mẫu lựa và bổ sung phụ gia
const CHECK_DATE_MESSAGE_ADDITIVE = 'Vui lòng chọn ngày';
const CHECK_TIME_MESSAGE_ADDITIVE = 'Vui lòng nhập thời gian';
const CHECK_IMPURITY_MESSAGE_ADDITIVE = 'Vui lòng chọn kết quả lựa tạp chất';
const WEIGHT_MATERIAL_MESSAGE = 'Vui lòng nhập khối lượng nguyên liệu';
const BICABONAT_LOT_NUM_MESSAGE = 'Vui lòng nhập số lô Natri bicacbonat';
const BICABONAT_WEIGHT_MESSAGE = 'Vui lòng nhập khối lượng Natri cacbonat';
const ASSIGNEE_MESSAGE = 'Vui lòng chọn người thực hiện';

// Tạo biểu mẫu kiểm tra nam châm và lưới
const CHECK_DATE_MESSAGE_MAG = 'Vui lòng chọn ngày sản xuất';
const FIN_PRODUCT_NO_MESSAGE_MAG = 'Vui lòng nhập mã lô thành phẩm';
const MAGNET_BEGIN_MESSAGE = 'Vui lòng chọn kết quả kiểm tra đầu ca';
const MAGNET_END_MESSAGE = 'Vui lòng chọn kết quả kiểm tra cuối ca';
const SCREEN4_BEGIN_MESSAGE = 'Vui lòng chọn kết quả kiểm tra đầu ca';
const SCREEN4_END_MESSAGE = 'Vui lòng chọn kết quả kiểm tra cuối ca';
const SCREEN3_BEGIN_MESSAGE = 'Vui lòng chọn kết quả kiểm tra đầu ca';
const SCREEN3_END_MESSAGE = 'Vui lòng chọn kết quả kiểm tra cuối ca';
const EXAMINER_MESSAGE_MAG = 'Vui lòng chọn người kiểm tra';
const REEXAMINER_MESSAGE_MAG = 'Vui lòng chọn người thẩm tra';

// Tạo biểu mẫu báo cáo trộn sản phẩm
const CHECK_DATE_MESSAGE_MIX = 'Vui lòng chọn ngày trộn';
const FIN_PRODUCT_NO1_MESSAGE_MIX = 'Vui lòng nhập mã lô';
const FIN_PRODUCT_WEIGHT1_MESSAGE_MIX = 'Vui lòng nhập khối lượng';
const FIN_PRODUCT_NO2_MESSAGE_MIX = 'Vui lòng nhập mã lô';
const FIN_PRODUCT_WEIGHT2_MESSAGE_MIX = 'Vui lòng nhập khối lượng';
const BHT_NO_MESSAGE_MIX = 'Vui lòng nhập mã lô';
const BHT_WEIGHT_MESSAGE_MIX = 'Vui lòng nhập khối lượng';
const BHT_WEIGHT_PRD_MESSAGE_MIX = 'Vui lòng nhập KL thành phẩm';
const WEIGHT_PRD_NO_MESSAGE_MIX = 'Vui lòng nhập mã lô thành phẩm';
const CHECK_IMPURITY_MESSAGE_MIX = 'Vui lòng nhập kết quả kiểm tra tạp chất';
const CHECK_SMELL_MESSAGE_MIX = 'Vui lòng nhập kết quả kiểm tra mùi';
const CHECK_COLOR_MESSAGE_MIX = 'Vui lòng nhập kết quả kiểm tra màu';
const MOISTURE_MESSAGE_MIX = 'Vui lòng nhập độ ẩm';
const TVN_MESSAGE_MIX = 'Vui lòng nhập TVN';
const ASH_MESSAGE_MIX = 'Vui lòng nhập thông số tro';
const PROTEIN_MESSAGE_MIX = 'Vui lòng nhập thông số protein';
const CHECK_EMPLOYEE_MESSAGE_MIX = 'Vui lòng chọn người thực hiện';

// Tạo biểu mẫu giám sát hoạt động máy
const CHECK_TIME_MESSAGE_MACHINE = 'Vui lòng chọn giờ';
const INICINERATOR_AIR_DUCT_MESSAGE = 'Vui lòng chọn kết quả kiểm tra ống dẫn khí của lò đốt';
const INICINERATOR_MESSAGE = 'Vui lòng chọn kết quả kiểm tra của lò đốt';
const DRYING_OVEN_AIR_MESSAGE = 'Vui lòng chọn kết quả kiểm tra ống dẫn khí của lò sấy';
const DRYING_OVEN_METER_MESSAGE = 'Vui lòng chọn kết quả kiểm tra đồng hồ của lò sấy';
const DRYING_OVEN_WALL_MESSAGE = 'Vui lòng chọn kết quả kiểm tra thành lò sấy';
const DRYING_OVEN_VALVE_MESSAGE = 'Vui lòng chọn kết quả kiểm tra van xả/ đóng của lò sấy';
const DRYING_OVEN_SEIEVE_SCREEN = 'Vui lòng chọn kết quả kiểm tra lưới máy sàng';
const DRYING_OVEN_CRUSHER = 'Vui lòng chọn kết quả kiểm tra máy nghiền';
const DRYING_OVEN_MIXER = 'Vui lòng chọn kết quả kiểm tra máy trộn';
const DRYING_OVEN_MAGENET = 'Vui lòng chọn kết quả kiểm tra nam châm';
const DRYIING_OVEN_PACKAGING_MACHINE = 'Vui lòng chọn kết quả kiểm tra máy đóng gói';
const CHECK_LIST_TYPE = 'Vui lòng chọn loại biểu mẫu';

export const productionProcessSchema = z
  .object({
    fromDate: z.custom<DateObject>(value => isValidDateObject(value), { message: DATE_MESSAGE }),
    moId: z.string({ message: COMMAND_MESSAGE }),
    checklistType: z.string({ message: CHECK_LIST_TYPE }).refine(value => value.trim() !== '', {
      message: CHECK_LIST_TYPE,
    }),
    workOrders: z.custom<IProductionProcess[]>().optional(),
  })
  .refine(
    data => {
      const productionProcessType = Object.keys(CHECKLIST_TYPE).map((item, index) => CHECKLIST_TYPE[item as CHECKLIST_TYPE]);

      const checklistOrder = !data?.workOrders ? 0 : Math.max(...(data?.workOrders?.map(item => item?.checklistOrder) ?? []));

      if (checklistOrder + 1 !== productionProcessType?.indexOf(data?.checklistType as CHECKLIST_TYPE) + 1) return false;
      return true;
    },
    {
      message: 'Vui lòng chọn biểu mẫu theo đúng trình tự',
      path: ['checklistType'],
    },
  );

export const receiveMaterialCheckListSchema = z
  .object({
    checkDate: z.custom<DateObject>(value => isValidDateObject(value), { message: CHECK_DATE_MESSAGE }),
    fromDate: z.custom<DateObject>(value => isValidDateObject(value), { message: PRODUCTION_PROCESS_DATE_MESSAGE }),
    checkTime: z.string({ message: CHECK_TIME_MESSAGE }).refine(value => value.trim() !== '', {
      message: CHECK_TIME_MESSAGE,
    }),
    weightNumber: z.string({ message: WEIGHT_NUMBER_MESSAGE }).refine(value => value.trim() !== '', {
      message: WEIGHT_NUMBER_MESSAGE,
    }),
    transportCondition: z.boolean({ message: TRANSPORT_CONDITION_MESSAGE }),
    transportNote: z.string().optional(),
    checkStatus: z.boolean({
      message: CHECK_STATUS_MESSAGE,
    }),
    statusNote: z.string().optional(),
    checkSmell: z.boolean({ message: CHECK_SMELL_MESSAGE }),
    smellNote: z.string().optional(),
    checkImpurity: z.boolean({ message: CHECK_IMPURITY_MESSAGE }),
    impurityNote: z.string().optional(),
    checkPoison: z.boolean({
      message: CHECK_POISON_MESSAGE,
    }),
    poisonNote: z.string().optional(),
    receiverId: z.string({ message: RECEIVER_MESSAGE }),
    note: z.string().max(MAX_NOTE_LENGTH, { message: NOTE_MAX_LENGTH_MESSAGE }).optional(),
    weight: z.string({ message: WEIGHT_MESSAGE }).refine(value => value.trim() !== '', {
      message: WEIGHT_MESSAGE,
    }),
    materialType: z.enum(materialType).optional(),
  })
  .refine(data => data.transportCondition === true || (data.transportNote && data.transportNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['transportNote'],
  })
  .refine(data => data.checkStatus === true || (data.statusNote && data.statusNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['statusNote'],
  })
  .refine(data => data.checkSmell === true || (data.smellNote && data.smellNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['smellNote'],
  })
  .refine(data => data.checkImpurity === true || (data.impurityNote && data.impurityNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['impurityNote'],
  })
  .refine(data => data.checkPoison === false || (data.poisonNote && data.poisonNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['poisonNote'],
  });

export const steamingProcessChecklistSchema = z.object({
  checkDate: z.custom<DateObject>(value => isValidDateObject(value), { message: CHECK_DATE_MESSAGE }),
  fromDate: z.custom<DateObject>(value => isValidDateObject(value), { message: PRODUCTION_PROCESS_DATE_MESSAGE }),
  checkTime: z.string({ message: CHECK_TIME_MESSAGE }).refine(value => value.trim() !== '', {
    message: CHECK_TIME_MESSAGE,
  }),
  weightNumber: z.string({ message: WEIGHT_NUMBER_MESSAGE }).refine(value => value.trim() !== '', {
    message: WEIGHT_NUMBER_MESSAGE,
  }),
  steamerAtm: z.string({ message: ATM_MESSAGE }).refine(value => value.trim() !== '', {
    message: ATM_MESSAGE,
  }),
  steamerTemp: z.string({ message: TEMP_MESSAGE }).refine(value => value.trim() !== '', { message: TEMP_MESSAGE }),
  steamerTime: z.string({ message: TIME_MESSAGE }).refine(value => value.trim() !== '', { message: TIME_MESSAGE }),
  tub1Atm: z.string({ message: ATM_MESSAGE }).refine(value => value.trim() !== '', { message: ATM_MESSAGE }),
  tub1Temp: z.string({ message: TEMP_MESSAGE }).refine(value => value.trim() !== '', { message: TEMP_MESSAGE }),
  tub1Time: z.string({ message: TIME_MESSAGE }).refine(value => value.trim() !== '', { message: TIME_MESSAGE }),
  tub2Atm: z.string({ message: ATM_MESSAGE }).refine(value => value.trim() !== '', { message: ATM_MESSAGE }),
  tub2Temp: z.string({ message: TEMP_MESSAGE }).optional(),
  tub2Time: z.string({ message: TIME_MESSAGE }).refine(value => value.trim() !== '', { message: TIME_MESSAGE }),
  finProductNo: z.string({ message: FIN_PRODUCT_NO_MESSAGE }).refine(value => value.trim() !== '', { message: FIN_PRODUCT_NO_MESSAGE }),
  receiverId: z.string({ message: EXAMINER_MESSAGE }).refine(value => value.trim() !== '', { message: EXAMINER_MESSAGE }),
});

export const additiveMaterialChecklistSchema = z
  .object({
    checkDate: z.custom<DateObject>(value => isValidDateObject(value), { message: CHECK_DATE_MESSAGE_ADDITIVE }),
    fromDate: z.custom<DateObject>(value => isValidDateObject(value), { message: PRODUCTION_PROCESS_DATE_MESSAGE }),
    checkTime: z.string({ message: CHECK_TIME_MESSAGE_ADDITIVE }).refine(value => value.trim() !== '', {
      message: CHECK_TIME_MESSAGE_ADDITIVE,
    }),
    weightNumber: z.string({ message: WEIGHT_NUMBER_MESSAGE }).refine(value => value.trim() !== '', {
      message: WEIGHT_NUMBER_MESSAGE,
    }),
    checkImpurity: z.boolean({ message: CHECK_IMPURITY_MESSAGE_ADDITIVE }),
    impurityNote: z.string().optional(),
    weightMaterial: z.string({ message: WEIGHT_MATERIAL_MESSAGE }).refine(value => value.trim() !== '', {
      message: WEIGHT_MATERIAL_MESSAGE,
    }),
    weightMaterialUnit: z.string(),
    bicabonatLotNumber: z.string({ message: BICABONAT_LOT_NUM_MESSAGE }).refine(value => value.trim() !== '', {
      message: BICABONAT_LOT_NUM_MESSAGE,
    }),
    bicacbonatWeight: z.string({ message: BICABONAT_WEIGHT_MESSAGE }).refine(value => value.trim() !== '', {
      message: BICABONAT_WEIGHT_MESSAGE,
    }),
    bicacbonatWeightUnit: z.string(),
    receiverId: z.string({ message: ASSIGNEE_MESSAGE }).refine(value => value.trim() !== '', { message: ASSIGNEE_MESSAGE }),
    note: z.string().max(MAX_NOTE_LENGTH, { message: NOTE_MAX_LENGTH_MESSAGE }).optional(),
  })
  .refine(data => data.checkImpurity === true || (data.impurityNote && data.impurityNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['impurityNote'],
  });

export const metalDetectionChecklistSchema = z
  .object({
    checkDate: z.custom<DateObject>(value => isValidDateObject(value), { message: CHECK_DATE_MESSAGE_MAG }),
    fromDate: z.custom<DateObject>(value => isValidDateObject(value), { message: PRODUCTION_PROCESS_DATE_MESSAGE }),
    finProductNo: z.string({ message: FIN_PRODUCT_NO_MESSAGE_MAG }).refine(value => value.trim() !== '', {
      message: FIN_PRODUCT_NO_MESSAGE_MAG,
    }),
    magnetBegin: z.boolean({ message: MAGNET_BEGIN_MESSAGE }),
    magnetBeginNote: z.string().optional(),
    magnetEnd: z.boolean({ message: MAGNET_END_MESSAGE }),
    magnetEndNote: z.string().optional(),
    screen4Begin: z.boolean({ message: SCREEN4_BEGIN_MESSAGE }),
    screen4BeginNote: z.string().optional(),
    screen4End: z.boolean({ message: SCREEN4_END_MESSAGE }),
    screen4EndNote: z.string().optional(),
    screen3Begin: z.boolean({ message: SCREEN3_BEGIN_MESSAGE }),
    screen3BeginNote: z.string().optional(),
    screen3End: z.boolean({ message: SCREEN3_END_MESSAGE }),
    screen3EndNote: z.string().optional(),
    checkedBy: z.string({ message: EXAMINER_MESSAGE_MAG }).refine(value => value.trim() !== '', { message: EXAMINER_MESSAGE_MAG }),
    auditedBy: z.string({ message: REEXAMINER_MESSAGE_MAG }).refine(value => value.trim() !== '', { message: REEXAMINER_MESSAGE_MAG }),
    note: z.string().max(MAX_NOTE_LENGTH, { message: NOTE_MAX_LENGTH_MESSAGE }).optional(),
  })
  .refine(data => data.magnetBegin === true || (data.magnetBeginNote && data.magnetBeginNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['magnetBeginNote'],
  })
  .refine(data => data.magnetEnd === true || (data.magnetEndNote && data.magnetEndNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['magnetEndNote'],
  })
  .refine(data => data.screen4Begin === true || (data.screen4BeginNote && data.screen4BeginNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['screen4BeginNote'],
  })
  .refine(data => data.screen4End === true || (data.screen4EndNote && data.screen4EndNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['screen4EndNote'],
  })
  .refine(data => data.screen3Begin === true || (data.screen3BeginNote && data.screen3BeginNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['screen3BeginNote'],
  })
  .refine(data => data.screen3End === true || (data.screen3EndNote && data.screen3EndNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['screen3EndNote'],
  });

export const mixingReportChecklistSchema = z
  .object({
    checkDate: z.custom<DateObject>(value => isValidDateObject(value), { message: CHECK_DATE_MESSAGE_MIX }),
    fromDate: z.custom<DateObject>(value => isValidDateObject(value), { message: PRODUCTION_PROCESS_DATE_MESSAGE }),
    finProduct1No: z.string({ message: FIN_PRODUCT_NO1_MESSAGE_MIX }).refine(value => value.trim() !== '', {
      message: FIN_PRODUCT_NO1_MESSAGE_MIX,
    }),
    finProduct1Weight: z.string({ message: FIN_PRODUCT_WEIGHT1_MESSAGE_MIX }).refine(
      value => {
        const floatValue = parseFloat(value);
        return !isNaN(floatValue) && floatValue >= 0;
      },
      {
        message: FIN_PRODUCT_NO1_MESSAGE_MIX,
      },
    ),
    finProduct1WeightUnit: z.string(),
    finProduct2No: z.string({ message: FIN_PRODUCT_NO2_MESSAGE_MIX }).refine(value => value.trim() !== '', {
      message: FIN_PRODUCT_NO2_MESSAGE_MIX,
    }),
    finProduct2Weight: z.string({ message: FIN_PRODUCT_WEIGHT2_MESSAGE_MIX }).refine(
      value => {
        const floatValue = parseFloat(value);
        return !isNaN(floatValue) && floatValue >= 0;
      },
      {
        message: FIN_PRODUCT_NO2_MESSAGE_MIX,
      },
    ),
    finProduct2WeightUnit: z.string(),
    bhtNo: z.string({ message: BHT_NO_MESSAGE_MIX }).refine(value => value.trim() !== '', {
      message: BHT_NO_MESSAGE_MIX,
    }),
    bhtWeight: z.string({ message: BHT_WEIGHT_MESSAGE_MIX }).refine(
      value => {
        const floatValue = parseFloat(value);
        return !isNaN(floatValue) && floatValue >= 0;
      },
      {
        message: BHT_NO_MESSAGE_MIX,
      },
    ),
    bhtWeightUnit: z.string(),
    bhtWeightPrd: z.string({ message: BHT_WEIGHT_PRD_MESSAGE_MIX }).refine(
      value => {
        const floatValue = parseFloat(value);
        return !isNaN(floatValue) && floatValue >= 0;
      },
      {
        message: BHT_WEIGHT_PRD_MESSAGE_MIX,
      },
    ),
    bhtWeightPrdUnit: z.string(),
    weightPrdNo: z.string({ message: WEIGHT_PRD_NO_MESSAGE_MIX }).refine(value => value.trim() !== '', {
      message: WEIGHT_PRD_NO_MESSAGE_MIX,
    }),
    checkImpurity: z.boolean().optional(),
    checkImpurityNote: z.string().optional(),
    checkSmell: z.boolean().optional(),
    checkSmellNote: z.string().optional(),
    checkColor: z.boolean().optional(),
    checkColorNote: z.string().optional(),
    checkEmployeeId: z.string().optional(),
    moisture: z.string().optional()
    //   .refine(
    //   value => {
    //     const floatValue = parseFloat(value);
    //     return !isNaN(floatValue) && floatValue >= 0;
    //   },
    //   {
    //     message: MOISTURE_MESSAGE_MIX,
    //   },
    // )
    ,
    tvn: z.string().optional()
    //   .refine(
    //   value => {
    //     const floatValue = parseFloat(value);
    //     return !isNaN(floatValue) && floatValue >= 0;
    //   },
    //   {
    //     message: TVN_MESSAGE_MIX,
    //   },
    // )
    ,
    ash: z.string().optional()
    // .refine(
    //   value => {
    //     const floatValue = parseFloat(value);
    //     return !isNaN(floatValue) && floatValue >= 0;
    //   },
    //   {
    //     message: ASH_MESSAGE_MIX,
    //   },
    // )
    ,
    protein: z.string().optional()
    //   .refine(
    //   value => {
    //     const floatValue = parseFloat(value);
    //     return !isNaN(floatValue) && floatValue >= 0;
    //   },
    //   {
    //     message: PROTEIN_MESSAGE_MIX,
    //   },
    // )
    ,
  })
  .refine(data => data.checkImpurity === true || (data.checkImpurityNote && data.checkImpurityNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['checkImpurityNote'],
  })
  .refine(data => data.checkSmell === true || (data.checkSmellNote && data.checkSmellNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['checkSmellNote'],
  })
  .refine(data => data.checkColor === true || (data.checkColorNote && data.checkColorNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['checkColorNote'],
  });

export const machineOperationChecklistSchema = z
  .object({
    checkDate: z.custom<DateObject>(value => isValidDateObject(value), { message: CHECK_DATE_MESSAGE_MIX }),
    fromDate: z.custom<DateObject>(value => isValidDateObject(value), { message: PRODUCTION_PROCESS_DATE_MESSAGE }),
    checkTime: z.string({ message: CHECK_TIME_MESSAGE_MACHINE }).refine(value => value.trim() !== '', {
      message: CHECK_TIME_MESSAGE_MACHINE,
    }),
    incineratorAirDuct: z.boolean({ message: INICINERATOR_AIR_DUCT_MESSAGE }),
    incineratorAirDuctNote: z.string().optional(),
    incinerator: z.boolean({ message: INICINERATOR_MESSAGE }),
    incineratorCheckNote: z.string().optional(),
    dryingOvenAirDuct: z.boolean({ message: DRYING_OVEN_AIR_MESSAGE }),
    dryingOvenAirDuctNote: z.string().optional(),
    dryingOvenMeter: z.boolean({ message: DRYING_OVEN_METER_MESSAGE }),
    dryingOvenMeterNote: z.string().optional(),
    dryingOvenWall: z.boolean({ message: DRYING_OVEN_WALL_MESSAGE }),
    dryingOvenWallNote: z.string().optional(),
    dryingOvenValve: z.boolean({ message: DRYING_OVEN_VALVE_MESSAGE }),
    dryingOvenValveNote: z.string().optional(),
    sieveScreen: z.boolean({ message: DRYING_OVEN_SEIEVE_SCREEN }),
    sieveScreenNote: z.string().optional(),
    crusher: z.boolean({ message: DRYING_OVEN_CRUSHER }),
    crusherNote: z.string().optional(),
    mixer: z.boolean({ message: DRYING_OVEN_MIXER }),
    mixerNote: z.string().optional(),
    magnet: z.boolean({ message: DRYING_OVEN_MAGENET }),
    magnetNote: z.string().optional(),
    packagingMachine: z.boolean({ message: DRYIING_OVEN_PACKAGING_MACHINE }),
    packagingMachineNote: z.string().optional(),
  })
  .refine(data => data.incineratorAirDuct === true || (data.incineratorAirDuctNote && data.incineratorAirDuctNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['incineratorAirDuctNote'],
  })
  .refine(data => data.incinerator === true || (data.incineratorCheckNote && data.incineratorCheckNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['incineratorCheckNote'],
  })
  .refine(data => data.dryingOvenAirDuct === true || (data.dryingOvenAirDuctNote && data.dryingOvenAirDuctNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['dryingOvenAirDuctNote'],
  })
  .refine(data => data.dryingOvenMeter === true || (data.dryingOvenMeterNote && data.dryingOvenMeterNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['dryingOvenMeterNote'],
  })
  .refine(data => data.dryingOvenWall === true || (data.dryingOvenWallNote && data.dryingOvenWallNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['dryingOvenWallNote'],
  })
  .refine(data => data.dryingOvenValve === true || (data.dryingOvenValveNote && data.dryingOvenValveNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['dryingOvenValveNote'],
  })
  .refine(data => data.sieveScreen === true || (data.sieveScreenNote && data.sieveScreenNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['sieveScreenNote'],
  })
  .refine(data => data.crusher === true || (data.crusherNote && data.crusherNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['crusherNote'],
  })
  .refine(data => data.mixer === true || (data.mixerNote && data.mixerNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['mixerNote'],
  })
  .refine(data => data.magnet === true || (data.magnetNote && data.magnetNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['magnetNote'],
  })
  .refine(data => data.packagingMachine === true || (data.packagingMachineNote && data.packagingMachineNote.trim() !== ''), {
    message: NOTE_MESSAGE,
    path: ['packagingMachineNote'],
  });

export type ProductionProcessFormSchema = z.infer<typeof productionProcessSchema>;
export type ReceiveMaterialCheckListFormSchema = z.infer<typeof receiveMaterialCheckListSchema>;
export type SteamingProcessChecklistFormSchema = z.infer<typeof steamingProcessChecklistSchema>;
export type AdditiveMaterialChecklistFormSchema = z.infer<typeof additiveMaterialChecklistSchema>;
export type MetalDetectionChecklistFormSchema = z.infer<typeof metalDetectionChecklistSchema>;
export type MixingReportChecklistFormSchema = z.infer<typeof mixingReportChecklistSchema>;
export type MachineOperationChecklistFormSchema = z.infer<typeof machineOperationChecklistSchema>;
