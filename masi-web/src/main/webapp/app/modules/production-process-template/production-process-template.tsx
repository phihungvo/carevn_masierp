import './production-process-template.scss';
import Card from 'app/components/card/card';
import React from 'react';
import {
  MaterialReceiptMonitoringTemplateCreate,
  MaterialReceiptMonitoringTemplateDetail,
  MaterialReceiptMonitoringTemplateUpdate,
} from './template/material-receipt-monitoring-template';
import {
  MachineOperationMonitoringTemplateCreate,
  MachineOperationMonitoringTemplateDetail,
  MachineOperationMonitoringTemplateUpdate,
} from './template/machine-operation-monitoring-template';
import { MagnetMeshTestTemplate, MagnetMeshTestTemplateDetail, MagnetMeshTestTemplateUpdate } from './template/magnet-mesh-test-template';
import { useLocation, useParams } from 'react-router';
import { PATH } from 'app/constants/path';
import {
  MonitoringStreamingDryingTemplate,
  MonitoringStreamingDryingTemplateDetail,
  MonitoringStreamingDryingTemplateUpdate,
} from './template/monitoring-steaming-drying-template';
import {
  SelectingAddingAdditivesTemplate,
  SelectingAddingAdditivesTemplateDetail,
  SelectingAddingAdditivesTemplateUpdate,
} from './template/selecting-adding-additives-template';
import {
  ReportMixingFishmealTemplate,
  ReportMixingFishmealTemplateDetail,
  ReportMixingFishmealTemplateUpdate,
} from './template/report-mixing-fishmeal-template';

const renderTemplate = () => {
  const { pathname } = useLocation();
  const { id, workItemId, templateId } = useParams();

  switch (pathname) {
    case PATH.PRODUCTION_PROCESS_TEMPLATE_MACHINE_OPERATION.replace(':id', id).replace(':workItemId', workItemId):
      {
        /* Biểu mẫu giám sát hoạt động máy */
      }
      return <MachineOperationMonitoringTemplateCreate />;
    case PATH.PRODUCTION_PROCESS_TEMPLATE_MACHINE_OPERATION_UPDATE.replace(':id', id)
      .replace(':workItemId', workItemId)
      .replace(':templateId', templateId):
      return <MachineOperationMonitoringTemplateUpdate />;
    case PATH.PRODUCTION_PROCESS_TEMPLATE_MACHINE_OPERATION_DETAIL.replace(':id', id)
      .replace(':workItemId', workItemId)
      .replace(':templateId', templateId):
      return <MachineOperationMonitoringTemplateDetail />;

      {
        /* Biểu mẫu kiểm tra nam châm và lưới */
      }
    case PATH.PRODUCTION_PROCESS_TEMPLATE_MAGNET_MESH.replace(':id', id).replace(':workItemId', workItemId):
      return <MagnetMeshTestTemplate />;
    case PATH.PRODUCTION_PROCESS_TEMPLATE_MAGNET_MESH_UPDATE.replace(':id', id)
      .replace(':workItemId', workItemId)
      .replace(':templateId', templateId):
      return <MagnetMeshTestTemplateUpdate />;
    case PATH.PRODUCTION_PROCESS_TEMPLATE_MAGNET_MESH_DETAIL.replace(':id', id)
      .replace(':workItemId', workItemId)
      .replace(':templateId', templateId):
      return <MagnetMeshTestTemplateDetail />;

      {
        /* Biểu mẫu giám sát tiếp nhận nguyên liệu  */
      }
    case PATH.PRODUCTION_PROCESS_TEMPLATE_MATERIAL_RECEIPT.replace(':id', id).replace(':workItemId', workItemId):
      return <MaterialReceiptMonitoringTemplateCreate />;
    case PATH.PRODUCTION_PROCESS_TEMPLATE_MATERIAL_RECEIPT_UPDATE.replace(':id', id)
      .replace(':workItemId', workItemId)
      .replace(':templateId', templateId):
      return <MaterialReceiptMonitoringTemplateUpdate />;
    case PATH.PRODUCTION_PROCESS_TEMPLATE_MATERIAL_RECEIPT_DETAIL.replace(':id', id)
      .replace(':workItemId', workItemId)
      .replace(':templateId', templateId):
      return <MaterialReceiptMonitoringTemplateDetail />;

      {
        /* Biểu mẫu giám sát công đoạn hấp - sấy */
      }
    case PATH.PRODUCTION_PROCESS_TEMPLATE_MONITORING_STEAMING.replace(':id', id).replace(':workItemId', workItemId):
      return <MonitoringStreamingDryingTemplate />;
    case PATH.PRODUCTION_PROCESS_TEMPLATE_MONITORING_STEAMING_UPDATE.replace(':id', id)
      .replace(':workItemId', workItemId)
      .replace(':templateId', templateId):
      return <MonitoringStreamingDryingTemplateUpdate />;
    case PATH.PRODUCTION_PROCESS_TEMPLATE_MONITORING_STEAMING_DETAIL.replace(':id', id)
      .replace(':workItemId', workItemId)
      .replace(':templateId', templateId):
      return <MonitoringStreamingDryingTemplateDetail />;

      {
        /*  Báo cáo trộn sản phẩm bột cá */
      }
    case PATH.PRODUCTION_PROCESS_TEMPLATE_REPORT_MIXING.replace(':id', id).replace(':workItemId', workItemId):
      return <ReportMixingFishmealTemplate />;
    case PATH.PRODUCTION_PROCESS_TEMPLATE_REPORT_MIXING_UPDATE.replace(':id', id)
      .replace(':workItemId', workItemId)
      .replace(':templateId', templateId):
      return <ReportMixingFishmealTemplateUpdate />;
    case PATH.PRODUCTION_PROCESS_TEMPLATE_REPORT_MIXING_DETAIL.replace(':id', id)
      .replace(':workItemId', workItemId)
      .replace(':templateId', templateId):
      return <ReportMixingFishmealTemplateDetail />;

      {
        /* Biểu mẫu lựa và bổ sung phụ gia */
      }
    case PATH.PRODUCTION_PROCESS_TEMPLATE_SELECTING_ADDING.replace(':id', id).replace(':workItemId', workItemId):
      return <SelectingAddingAdditivesTemplate />;
    case PATH.PRODUCTION_PROCESS_TEMPLATE_SELECTING_ADDING_UPDATE.replace(':id', id)
      .replace(':workItemId', workItemId)
      .replace(':templateId', templateId):
      return <SelectingAddingAdditivesTemplateUpdate />;
    case PATH.PRODUCTION_PROCESS_TEMPLATE_SELECTING_ADDING_DETAIL.replace(':id', id)
      .replace(':workItemId', workItemId)
      .replace(':templateId', templateId):
      return <SelectingAddingAdditivesTemplateDetail />;
    default:
      return <></>;
  }
};

const ProductionProcessTemplate = () => {
  return <Card className="card-template">{renderTemplate()}</Card>;
};

export default ProductionProcessTemplate;
