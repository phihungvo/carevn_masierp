import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import React, { useState } from 'react';
import MaterialReceiptMonitoringForm from '../form/material-receipt-monitoring-form';
import { ModalsTemplateCreateSuccess, ModalsTemplateUpdateSuccess } from '../modals/template-modals';
import Button from 'app/components/button/button';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { useNavigate } from 'react-router';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { CREATE_MATERIAL_RECEIPT_MONITORING, UPDATE_MATERIAL_RECEIPT_MONITORING } = MUTATION_KEY;

export const MaterialReceiptMonitoringTemplateDetail = () => {
  const navigate = useNavigate();

  return (
    <>
      <Flex direction="column" align="center" justify="center">
        <Typography level={3} style={{ marginTop: 24 }}>
          Biểu mẫu giám sát tiếp nhận nguyên liệu
        </Typography>
        <MaterialReceiptMonitoringForm type="detail" />
      </Flex>

      <Flex gap={12} justify="end" className="btn-group-template">
        <Button onClick={() => navigate(-1)} color="primary">
          Quay lại
        </Button>
      </Flex>
    </>
  );
};

export const MaterialReceiptMonitoringTemplateCreate = () => {
  const navigate = useNavigate();
  const isCreatingMaterialReceiptMonitoring = useIsMutating({ mutationKey: [CREATE_MATERIAL_RECEIPT_MONITORING] });

  const [isOpen, setIsOpen] = useState(false);

  const toggle = () => {
    setIsOpen(prev => !prev);
  };

  return (
    <>
      <Flex direction="column" align="center" justify="center">
        <Typography level={3} style={{ marginTop: 24 }}>
          Biểu mẫu giám sát tiếp nhận nguyên liệu
        </Typography>
        <MaterialReceiptMonitoringForm type="create" toggle={toggle} />
      </Flex>

      <Flex gap={12} justify="end" className="btn-group-template">
        <Button onClick={() => navigate(-1)} outline className="btn-cancel-template">
          Huỷ
        </Button>
        <Button disabled={!!isCreatingMaterialReceiptMonitoring} color="primary" type="submit" form={FORM.MATERIAL_RECEIPT_MONITORING}>
          Tạo mới
        </Button>
      </Flex>

      <ModalsTemplateCreateSuccess isOpen={isOpen} toggle={toggle} />
    </>
  );
};

export const MaterialReceiptMonitoringTemplateUpdate = () => {
  const navigate = useNavigate();
  const isUpdatingMaterialReceipt = useIsMutating({ mutationKey: [UPDATE_MATERIAL_RECEIPT_MONITORING] });

  const [isOpen, setIsOpen] = useState(false);

  const toggle = () => {
    setIsOpen(prev => !prev);
  };

  return (
    <>
      <Flex direction="column" align="center" justify="center">
        <Typography level={3} style={{ marginTop: 24 }}>
          Cập nhật biểu mẫu giám sát tiếp nhận nguyên liệu
        </Typography>
        <MaterialReceiptMonitoringForm type="update" toggle={toggle} />
      </Flex>

      <Flex gap={12} justify="end" className="btn-group-template">
        <Button outline className="btn-cancel-template" onClick={() => navigate(-1)}>
          Huỷ
        </Button>
        <Button disabled={!!isUpdatingMaterialReceipt} color="primary" type="submit" form={FORM.MATERIAL_RECEIPT_MONITORING}>
          Cập nhật
        </Button>
      </Flex>

      <ModalsTemplateUpdateSuccess isOpen={isOpen} toggle={toggle} />
    </>
  );
};
