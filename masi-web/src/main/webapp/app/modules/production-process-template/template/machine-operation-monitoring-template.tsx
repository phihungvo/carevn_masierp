import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import React, { useState } from 'react';
import MachineOperationMonitoringForm from '../form/machine-operation-monitoring-form';
import { ModalsTemplateCreateSuccess, ModalsTemplateUpdateSuccess } from '../modals/template-modals';
import Button from 'app/components/button/button';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { useNavigate } from 'react-router';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { CREATE_MACHINE_OPERATION_MONITORING, UPDATE_MACHINE_OPERATION_MONITORING } = MUTATION_KEY;

export const MachineOperationMonitoringTemplateDetail = () => {
  const navigate = useNavigate();

  return (
    <>
      <Flex direction="column" align="center" justify="center">
        <Typography level={3}>Biểu mẫu giám sát hoạt động máy</Typography>
        <MachineOperationMonitoringForm type="detail" />
      </Flex>

      <Flex gap={12} justify="end" className="btn-group-template">
        <Button onClick={() => navigate(-1)} color="primary">
          Quay lại
        </Button>
      </Flex>
    </>
  );
};

export const MachineOperationMonitoringTemplateCreate = () => {
  const navigate = useNavigate();
  const isCreatingMachineOperationMonitoring = useIsMutating({
    mutationKey: [CREATE_MACHINE_OPERATION_MONITORING],
  });

  const [isOpen, setIsOpen] = useState(false);

  const toggle = () => {
    setIsOpen(prev => !prev);
  };

  return (
    <>
      <Flex direction="column" align="center" justify="center">
        <Typography level={3}>Biểu mẫu giám sát hoạt động máy</Typography>
        <MachineOperationMonitoringForm type="create" toggle={toggle} />
      </Flex>

      <Flex gap={12} justify="end" className="btn-group-template">
        <Button onClick={() => navigate(-1)} outline className="btn-cancel-template">
          Huỷ
        </Button>
        <Button disabled={!!isCreatingMachineOperationMonitoring} color="primary" type="submit" form={FORM.MACHINE_OPERATION_MONITORING}>
          Tạo mới
        </Button>
      </Flex>

      <ModalsTemplateCreateSuccess isOpen={isOpen} toggle={toggle} />
    </>
  );
};

export const MachineOperationMonitoringTemplateUpdate = () => {
  const navigate = useNavigate();
  const isUpdatingMachineOperationMonitoring = useIsMutating({
    mutationKey: [UPDATE_MACHINE_OPERATION_MONITORING],
  });

  const [isOpen, setIsOpen] = useState(false);

  const toggle = () => {
    setIsOpen(prev => !prev);
  };

  return (
    <>
      <Flex direction="column" align="center" justify="center">
        <Typography level={3}>Cập nhật biểu mẫu giám sát hoạt động máy</Typography>
        <MachineOperationMonitoringForm type="update" toggle={toggle} />
      </Flex>

      <Flex gap={12} justify="end" className="btn-group-template">
        <Button onClick={() => navigate(-1)} outline className="btn-cancel-template">
          Huỷ
        </Button>
        <Button disabled={!!isUpdatingMachineOperationMonitoring} color="primary" type="submit" form={FORM.MACHINE_OPERATION_MONITORING}>
          Cập nhật
        </Button>
      </Flex>

      <ModalsTemplateUpdateSuccess isOpen={isOpen} toggle={toggle} />
    </>
  );
};
