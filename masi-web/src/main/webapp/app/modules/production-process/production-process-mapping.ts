import { Color } from 'app/shared/model/enumerations/color.model';
import { CHECKLIST_TYPE, PRODUCTION_PROCESS_STATUS } from 'app/shared/model/enumerations/production-process.model';

const mapProductionProcessStatusText = (status: PRODUCTION_PROCESS_STATUS): string => {
  switch (status) {
    case PRODUCTION_PROCESS_STATUS.NEW:
      return 'Mới';
    case PRODUCTION_PROCESS_STATUS.COMPLETED:
      return 'Hoàn thành';
    case PRODUCTION_PROCESS_STATUS.RUNNING:
      return 'Đang sản xuất';
    case PRODUCTION_PROCESS_STATUS.STOP:
      return 'Đã ngừng';
    default:
      return '';
  }
};

const mapProductionProcessStatusColor = (status: PRODUCTION_PROCESS_STATUS): Color => {
  switch (status) {
    case PRODUCTION_PROCESS_STATUS.NEW:
      return Color.PRIMARY;
    case PRODUCTION_PROCESS_STATUS.COMPLETED:
      return Color.SUCCESS;
    case PRODUCTION_PROCESS_STATUS.RUNNING:
      return Color.WARNING;
    case PRODUCTION_PROCESS_STATUS.STOP:
      return Color.ERROR;
    default:
      return undefined;
  }
};

const mapProductionProcessType = (text: string): string => {
  switch (text) {
    case CHECKLIST_TYPE.RECEIVE_MATERIAL_CHECKLIST:
      return 'Công đoạn tiếp nhận nguyên liệu';
    case CHECKLIST_TYPE.ADDITIVE_MATERIAL_CHECKLIST:
      return 'Công đoạn bổ sung phụ gia';
    case CHECKLIST_TYPE.MACHINE_OPERATION_CHECKLIST:
      return 'Công đoạn giám sát hoạt động máy';
    case CHECKLIST_TYPE.STEAMING_PROCESS_CHECKLIST:
      return 'Công đoạn giám sát hấp - sấy';
    case CHECKLIST_TYPE.METAL_DETECTION_CHECKLIST:
      return 'Công đoạn kiểm tra nam châm - lưới';
    case CHECKLIST_TYPE.MIXING_REPORT_CHECKLIST:
      return 'Công đoạn trộn bột';
    default:
      '';
      return undefined;
  }
};

const mapProductionProcessNamePath = (text: string): string => {
  switch (text) {
    case CHECKLIST_TYPE.RECEIVE_MATERIAL_CHECKLIST:
      return 'material-receipt-monitoring-template';
    case CHECKLIST_TYPE.ADDITIVE_MATERIAL_CHECKLIST:
      return 'selecting-adding-additives-template';
    case CHECKLIST_TYPE.MACHINE_OPERATION_CHECKLIST:
      return 'machine-operation-monitoring-template';
    case CHECKLIST_TYPE.STEAMING_PROCESS_CHECKLIST:
      return 'monitoring-steaming-drying-template';
    case CHECKLIST_TYPE.METAL_DETECTION_CHECKLIST:
      return 'magnet-mesh-test-template';
    case CHECKLIST_TYPE.MIXING_REPORT_CHECKLIST:
      return 'report-mixing-fishmeal-template';
    default:
      '';
      return undefined;
  }
};

export default {
  mapProductionProcessStatusText,
  mapProductionProcessStatusColor,
  mapProductionProcessType,
  mapProductionProcessNamePath,
};
