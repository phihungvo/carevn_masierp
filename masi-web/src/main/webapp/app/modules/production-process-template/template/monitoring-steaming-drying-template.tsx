import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import React, { useState } from 'react';
import MonitoringSteamingDryingForm from '../form/monitoring-steaming-drying-form';
import Button from 'app/components/button/button';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { useNavigate } from 'react-router';
import { ModalsTemplateCreateSuccess, ModalsTemplateUpdateSuccess } from '../modals/template-modals';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { CREATE_MONITORING_STEAMING_DRYING, UPDATE_MONITORING_STEAMING_DRYING } = MUTATION_KEY;

export const MonitoringStreamingDryingTemplateDetail = () => {
  const navigate = useNavigate();

  return (
    <>
      <Flex direction="column" align="center" justify="center">
        <Typography level={3} style={{ marginTop: 24 }}>
          Biểu mẫu giám sát công đoạn hấp - sấy
        </Typography>
        <MonitoringSteamingDryingForm type="detail" />
      </Flex>

      <Flex gap={12} justify="end" className="btn-group-template">
        <Button onClick={() => navigate(-1)} color="primary">
          Quay lại
        </Button>
      </Flex>
    </>
  );
};

export const MonitoringStreamingDryingTemplate = () => {
  const navigate = useNavigate();

  const [isOpen, setIsOpen] = useState(false);
  const isCreatingMonitoringSteamingDrying = useIsMutating({
    mutationKey: [CREATE_MONITORING_STEAMING_DRYING],
  });

  const toggle = () => {
    setIsOpen(prev => !prev);
  };

  return (
    <>
      <Flex direction="column" align="center" justify="center">
        <Typography level={3} style={{ marginTop: 24 }}>
          Biểu mẫu giám sát công đoạn hấp - sấy
        </Typography>
        <MonitoringSteamingDryingForm type="create" toggle={toggle} />
      </Flex>

      <Flex gap={12} justify="end" className="btn-group-template">
        <Button onClick={() => navigate(-1)} outline className="btn-cancel-template">
          Huỷ
        </Button>
        <Button disabled={!!isCreatingMonitoringSteamingDrying} color="primary" type="submit" form={FORM.MONITORING_STEAMING_DRYING}>
          Tạo mới
        </Button>
      </Flex>

      <ModalsTemplateCreateSuccess isOpen={isOpen} toggle={toggle} />
    </>
  );
};

export const MonitoringStreamingDryingTemplateUpdate = () => {
  const navigate = useNavigate();
  const isUpdatingMonitoringSteamingDrying = useIsMutating({
    mutationKey: [UPDATE_MONITORING_STEAMING_DRYING],
  });

  const [isOpen, setIsOpen] = useState(false);

  const toggle = () => {
    setIsOpen(prev => !prev);
  };

  return (
    <>
      <Flex direction="column" align="center" justify="center">
        <Typography level={3} style={{ marginTop: 24 }}>
          Cập nhật biểu mẫu giám sát công đoạn hấp - sấy
        </Typography>
        <MonitoringSteamingDryingForm type="update" toggle={toggle} />
      </Flex>

      <Flex gap={12} justify="end" className="btn-group-template">
        <Button onClick={() => navigate(-1)} outline className="btn-cancel-template">
          Huỷ
        </Button>
        <Button disabled={!!isUpdatingMonitoringSteamingDrying} color="primary" type="submit" form={FORM.MONITORING_STEAMING_DRYING}>
          Cập nhật
        </Button>
      </Flex>

      <ModalsTemplateUpdateSuccess isOpen={isOpen} toggle={toggle} />
    </>
  );
};
