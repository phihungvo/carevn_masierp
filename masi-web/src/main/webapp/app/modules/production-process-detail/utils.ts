import { CHECKLIST_TYPE } from 'app/shared/model/enumerations/production-process.model';
import { Template } from './production-process-detail-components';
import { NavigateFunction } from 'react-router';
import { PATH } from 'app/constants/path';

export const handleUpdate = (templateId: string, template: Template, id: string, workItemId: string, navigate: NavigateFunction) => {
  switch (template) {
    case CHECKLIST_TYPE.RECEIVE_MATERIAL_CHECKLIST:
      navigate(
        PATH.PRODUCTION_PROCESS_TEMPLATE_MATERIAL_RECEIPT_UPDATE.replace(':id', id)
          .replace(':workItemId', workItemId)
          .replace(':templateId', templateId),
      );
      break;
    case CHECKLIST_TYPE.STEAMING_PROCESS_CHECKLIST:
      navigate(
        PATH.PRODUCTION_PROCESS_TEMPLATE_MONITORING_STEAMING_UPDATE.replace(':id', id)
          .replace(':workItemId', workItemId)
          .replace(':templateId', templateId),
      );
      break;
    case CHECKLIST_TYPE.ADDITIVE_MATERIAL_CHECKLIST:
      navigate(
        PATH.PRODUCTION_PROCESS_TEMPLATE_SELECTING_ADDING_UPDATE.replace(':id', id)
          .replace(':workItemId', workItemId)
          .replace(':templateId', templateId),
      );
      break;
    case CHECKLIST_TYPE.METAL_DETECTION_CHECKLIST:
      navigate(
        PATH.PRODUCTION_PROCESS_TEMPLATE_MAGNET_MESH_UPDATE.replace(':id', id)
          .replace(':workItemId', workItemId)
          .replace(':templateId', templateId),
      );
      break;
    case CHECKLIST_TYPE.MIXING_REPORT_CHECKLIST:
      navigate(
        PATH.PRODUCTION_PROCESS_TEMPLATE_REPORT_MIXING_UPDATE.replace(':id', id)
          .replace(':workItemId', workItemId)
          .replace(':templateId', templateId),
      );
      break;
    case CHECKLIST_TYPE.MACHINE_OPERATION_CHECKLIST:
      navigate(
        PATH.PRODUCTION_PROCESS_TEMPLATE_MACHINE_OPERATION_UPDATE.replace(':id', id)
          .replace(':workItemId', workItemId)
          .replace(':templateId', templateId),
      );
      break;
    default:
      break;
  }
};

export const handleViewDetail = (templateId: string, template: Template, id: string, workItemId: string, navigate: NavigateFunction) => {
  switch (template) {
    case CHECKLIST_TYPE.RECEIVE_MATERIAL_CHECKLIST:
      navigate(
        PATH.PRODUCTION_PROCESS_TEMPLATE_MATERIAL_RECEIPT_DETAIL.replace(':id', id)
          .replace(':workItemId', workItemId)
          .replace(':templateId', templateId),
      );
      break;
    case CHECKLIST_TYPE.STEAMING_PROCESS_CHECKLIST:
      navigate(
        PATH.PRODUCTION_PROCESS_TEMPLATE_MONITORING_STEAMING_DETAIL.replace(':id', id)
          .replace(':workItemId', workItemId)
          .replace(':templateId', templateId),
      );
      break;
    case CHECKLIST_TYPE.ADDITIVE_MATERIAL_CHECKLIST:
      navigate(
        PATH.PRODUCTION_PROCESS_TEMPLATE_SELECTING_ADDING_DETAIL.replace(':id', id)
          .replace(':workItemId', workItemId)
          .replace(':templateId', templateId),
      );
      break;
    case CHECKLIST_TYPE.METAL_DETECTION_CHECKLIST:
      navigate(
        PATH.PRODUCTION_PROCESS_TEMPLATE_MAGNET_MESH_DETAIL.replace(':id', id)
          .replace(':workItemId', workItemId)
          .replace(':templateId', templateId),
      );
      break;
    case CHECKLIST_TYPE.MIXING_REPORT_CHECKLIST:
      navigate(
        PATH.PRODUCTION_PROCESS_TEMPLATE_REPORT_MIXING_DETAIL.replace(':id', id)
          .replace(':workItemId', workItemId)
          .replace(':templateId', templateId),
      );
      break;
    case CHECKLIST_TYPE.MACHINE_OPERATION_CHECKLIST:
      navigate(
        PATH.PRODUCTION_PROCESS_TEMPLATE_MACHINE_OPERATION_DETAIL.replace(':id', id)
          .replace(':workItemId', workItemId)
          .replace(':templateId', templateId),
      );
      break;
    default:
      break;
  }
};
