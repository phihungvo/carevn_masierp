import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import React, { useState } from 'react';
import ReportMixingFishMealForm from '../form/report-mixing-fishmeal-form';
import Button from 'app/components/button/button';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { useNavigate } from 'react-router';
import { ModalsTemplateCreateSuccess, ModalsTemplateUpdateSuccess } from '../modals/template-modals';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { CREATE_MIXING_REPORT, UPDATE_MIXING_REPORT } = MUTATION_KEY;

export const ReportMixingFishmealTemplateDetail = () => {
  const navigate = useNavigate();

  return (
    <>
      <Flex direction="column" align="center" justify="center">
        <Typography level={3} style={{ marginTop: 24 }}>
          Báo cáo trộn sản phẩm bột cá
        </Typography>
        <ReportMixingFishMealForm type="detail" />
      </Flex>

      <Flex gap={12} justify="end" className="btn-group-template">
        <Button onClick={() => navigate(-1)} color="primary">
          Quay lại
        </Button>
      </Flex>
    </>
  );
};

export const ReportMixingFishmealTemplate = () => {
  const navigate = useNavigate();
  const isCreatingMixing = useIsMutating({
    mutationKey: [CREATE_MIXING_REPORT],
  });

  const [isOpen, setIsOpen] = useState(false);

  const toggle = () => {
    setIsOpen(prev => !prev);
  };

  return (
    <>
      <Flex direction="column" align="center" justify="center">
        <Typography level={3} style={{ marginTop: 24 }}>
          Báo cáo trộn sản phẩm bột cá
        </Typography>
        <ReportMixingFishMealForm toggle={toggle} type="create" />
      </Flex>

      <Flex gap={12} justify="end" className="btn-group-template">
        <Button onClick={() => navigate(-1)} outline className="btn-cancel-template">
          Huỷ
        </Button>
        <Button disabled={!!isCreatingMixing} color="primary" type="submit" form={FORM.REPORT_MIXING_FISHMEAL}>
          Tạo mới
        </Button>
      </Flex>

      <ModalsTemplateCreateSuccess isOpen={isOpen} toggle={toggle} />
    </>
  );
};

export const ReportMixingFishmealTemplateUpdate = () => {
  const navigate = useNavigate();
  const isUpdatingMixing = useIsMutating({
    mutationKey: [UPDATE_MIXING_REPORT],
  });

  const [isOpen, setIsOpen] = useState(false);

  const toggle = () => {
    setIsOpen(prev => !prev);
  };

  return (
    <>
      <Flex direction="column" align="center" justify="center">
        <Typography level={3} style={{ marginTop: 24 }}>
          Cập nhật báo cáo trộn sản phẩm bột cá
        </Typography>
        <ReportMixingFishMealForm toggle={toggle} type="update" />
      </Flex>

      <Flex gap={12} justify="end" className="btn-group-template">
        <Button onClick={() => navigate(-1)} outline className="btn-cancel-template">
          Huỷ
        </Button>
        <Button disabled={!!isUpdatingMixing} color="primary" type="submit" form={FORM.REPORT_MIXING_FISHMEAL}>
          Cập nhật
        </Button>
      </Flex>

      <ModalsTemplateUpdateSuccess isOpen={isOpen} toggle={toggle} />
    </>
  );
};
