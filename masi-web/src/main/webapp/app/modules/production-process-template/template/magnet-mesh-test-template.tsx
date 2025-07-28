import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import React, { useState } from 'react';
import MagnetMeshTestForm from '../form/magnet-mesh-test-form';
import Button from 'app/components/button/button';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { useNavigate } from 'react-router';
import { ModalsTemplateCreateSuccess, ModalsTemplateUpdateSuccess } from '../modals/template-modals';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { CREATE_MAGNET_MESH_TEST, UPDATE_MAGNET_MESH_TEST } = MUTATION_KEY;

export const MagnetMeshTestTemplateDetail = () => {
  const navigate = useNavigate();

  return (
    <>
      <Flex direction="column" align="center" justify="center">
        <Typography level={3} style={{ marginTop: 24 }}>
          Biểu mẫu kiểm tra nam châm và lưới
        </Typography>

        <MagnetMeshTestForm type="detail" />
      </Flex>

      <Flex gap={12} justify="end" className="btn-group-template">
        <Button onClick={() => navigate(-1)} color="primary">
          Quay lại
        </Button>
      </Flex>
    </>
  );
};

export const MagnetMeshTestTemplate = () => {
  const navigate = useNavigate();
  const isCreatingMagnet = useIsMutating({
    mutationKey: [CREATE_MAGNET_MESH_TEST],
  });

  const [isOpen, setIsOpen] = useState(false);

  const toggle = () => {
    setIsOpen(prev => !prev);
  };

  return (
    <>
      <Flex direction="column" align="center" justify="center">
        <Typography level={3} style={{ marginTop: 24 }}>
          Biểu mẫu kiểm tra nam châm và lưới
        </Typography>

        <MagnetMeshTestForm toggle={toggle} type="create" />
      </Flex>

      <Flex gap={12} justify="end" className="btn-group-template">
        <Button onClick={() => navigate(-1)} outline className="btn-cancel-template">
          Huỷ
        </Button>
        <Button disabled={!!isCreatingMagnet} color="primary" type="submit" form={FORM.MAGNET_MESH_TEST}>
          Tạo mới
        </Button>
      </Flex>

      <ModalsTemplateCreateSuccess isOpen={isOpen} toggle={toggle} />
    </>
  );
};

export const MagnetMeshTestTemplateUpdate = () => {
  const navigate = useNavigate();
  const isUpdatingMagnet = useIsMutating({
    mutationKey: [UPDATE_MAGNET_MESH_TEST],
  });

  const [isOpen, setIsOpen] = useState(false);

  const toggle = () => {
    setIsOpen(prev => !prev);
  };

  return (
    <>
      <Flex direction="column" align="center" justify="center">
        <Typography level={3} style={{ marginTop: 24 }}>
          Cập nhật biểu mẫu kiểm tra nam châm và lưới
        </Typography>

        <MagnetMeshTestForm toggle={toggle} type="update" />
      </Flex>

      <Flex gap={12} justify="end" className="btn-group-template">
        <Button onClick={() => navigate(-1)} outline className="btn-cancel-template">
          Huỷ
        </Button>
        <Button disabled={!!isUpdatingMagnet} color="primary" type="submit" form={FORM.MAGNET_MESH_TEST}>
          Cập nhật
        </Button>
      </Flex>

      <ModalsTemplateUpdateSuccess isOpen={isOpen} toggle={toggle} />
    </>
  );
};
