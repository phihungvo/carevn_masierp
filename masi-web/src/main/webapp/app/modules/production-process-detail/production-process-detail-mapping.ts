import { CHECKLIST_TYPE } from 'app/shared/model/enumerations/production-process.model';

export const templateNameMap = (type: CHECKLIST_TYPE) => {
  switch (type) {
    case CHECKLIST_TYPE.METAL_DETECTION_CHECKLIST:
      return 'Kiểm tra nam châm và lưới';
    case CHECKLIST_TYPE.ADDITIVE_MATERIAL_CHECKLIST:
      return 'Lựa và bổ sung phụ gia';
    case CHECKLIST_TYPE.MIXING_REPORT_CHECKLIST:
      return 'Báo cáo trộn sản phẩm bột cá';
    case CHECKLIST_TYPE.MACHINE_OPERATION_CHECKLIST:
      return 'Giám sát hoạt động máy';
    case CHECKLIST_TYPE.STEAMING_PROCESS_CHECKLIST:
      return 'Giám sát công đoạn hấp - sấy';
    case CHECKLIST_TYPE.RECEIVE_MATERIAL_CHECKLIST:
      return 'Giám sát tiếp nhận nguyên liệu';
    default:
      return '';
  }
};
