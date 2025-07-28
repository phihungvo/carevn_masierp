import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';
import { Template } from './production-process-detail-components';
import useReceiveMaterialChecklist from 'app/hooks/use-receive-material-checklist';
import useSteamingProcessChecklist from 'app/hooks/use-steaming-process-checklist';
import useAdditiveMaterialChecklist from 'app/hooks/use-additive-material-checklist';
import useMixingReportChecklist from 'app/hooks/use-mixing-report-checklist';
import useMachineOperationChecklist from 'app/hooks/use-machine-operation-checklist';
import useMetalDetectionChecklist from 'app/hooks/use-material-detection-checklist';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';

const {
  DELETE_MATERIAL_RECEIPT_MONITORING,
  DELETE_SELECTING_ADDING_ADDITIVES,
  DELETE_MACHINE_OPERATION_MONITORING,
  DELETE_MONITORING_STEAMING_DRYING,
  DELETE_MAGNET_MESH_TEST,
  DELETE_MIXING_REPORT,
} = MUTATION_KEY;

const { useDeleteReceiveMaterialChecklist } = useReceiveMaterialChecklist;
const { useDeleteSteamingProcessChecklist } = useSteamingProcessChecklist;
const { useDeleteAdditiveMaterialChecklist } = useAdditiveMaterialChecklist;
const { useDeleteMetalDetectionChecklist } = useMetalDetectionChecklist;
const { useDeleteMixingReportChecklist } = useMixingReportChecklist;
const { useDeleteMachineOperationChecklist } = useMachineOperationChecklist;

interface IModalConfirmDeleteTemplateProps {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord: { template: Template; id: string };
}

const ModalConfirmDeleteTemplate = (props: IModalConfirmDeleteTemplateProps) => {
  const { isOpen, toggle, selectedRecord } = props;

  const { mutate: deleteReceiveMaterialChecklist } = useDeleteReceiveMaterialChecklist();
  const { mutate: deleteSteamingProcessChecklist } = useDeleteSteamingProcessChecklist();
  const { mutate: deleteAdditiveMaterialChecklist } = useDeleteAdditiveMaterialChecklist();
  const { mutate: deleteMetalDetectionChecklist } = useDeleteMetalDetectionChecklist();
  const { mutate: deleteMixingReportChecklist } = useDeleteMixingReportChecklist();
  const { mutate: deleteMachineOperationChecklist } = useDeleteMachineOperationChecklist();

  const handleCancelTemplate = () => {
    selectedRecord?.template === 'RECEIVE_MATERIAL_CHECKLIST' && deleteReceiveMaterialChecklist(selectedRecord?.id);
    selectedRecord?.template === 'STEAMING_PROCESS_CHECKLIST' && deleteSteamingProcessChecklist(selectedRecord?.id);
    selectedRecord?.template === 'ADDITIVE_MATERIAL_CHECKLIST' && deleteAdditiveMaterialChecklist(selectedRecord?.id);
    selectedRecord?.template === 'METAL_DETECTION_CHECKLIST' && deleteMetalDetectionChecklist(selectedRecord?.id);
    selectedRecord?.template === 'MIXING_REPORT_CHECKLIST' && deleteMixingReportChecklist(selectedRecord?.id);
    selectedRecord?.template === 'MACHINE_OPERATION_CHECKLIST' && deleteMachineOperationChecklist(selectedRecord?.id);
    toggle();
  };

  const isDeletingMaterial = useIsMutating({
    mutationKey: [DELETE_MATERIAL_RECEIPT_MONITORING],
  });
  const idDeletingAdditive = useIsMutating({
    mutationKey: [DELETE_SELECTING_ADDING_ADDITIVES],
  });
  const isDeletingMachineOperation = useIsMutating({
    mutationKey: [DELETE_MACHINE_OPERATION_MONITORING],
  });
  const isDeletingSteamingProcess = useIsMutating({
    mutationKey: [DELETE_MONITORING_STEAMING_DRYING],
  });
  const isDeletingMagnetMeshTest = useIsMutating({
    mutationKey: [DELETE_MAGNET_MESH_TEST],
  });
  const isDeletingMixingReport = useIsMutating({
    mutationKey: [DELETE_MIXING_REPORT],
  });

  const disabled =
    !!isDeletingMaterial ||
    !!idDeletingAdditive ||
    !!isDeletingMachineOperation ||
    !!isDeletingSteamingProcess ||
    !!isDeletingMagnetMeshTest ||
    !!isDeletingMixingReport;

  return (
    <Modal
      disabledOk={disabled}
      isOpen={isOpen}
      toggle={toggle}
      className="modal-delete-process"
      okText="Xác nhận"
      onOk={handleCancelTemplate}
    >
      <Typography level={3}>Xoá biểu mẫu</Typography>
      <Typography level={4}>Biểu mẫu sẽ bị xóa vĩnh viễn và không thể phục hồi sau khi xóa?</Typography>
    </Modal>
  );
};

export default ModalConfirmDeleteTemplate;
