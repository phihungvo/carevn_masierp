import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import React, { useState } from 'react';
import SelectingAddingAdditivesForm from '../form/selecting-adding-additives-form';
import Button from 'app/components/button/button';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { useNavigate } from 'react-router';
import { ModalsTemplateCreateSuccess, ModalsTemplateUpdateSuccess } from '../modals/template-modals';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { CREATE_SELECTING_ADDING_ADDITIVES, UPDATE_SELECTING_ADDING_ADDITIVES } = MUTATION_KEY;

export const SelectingAddingAdditivesTemplateDetail = () => {
  const navigate = useNavigate();

  return (
    <>
      <Flex direction="column" align="center" justify="center">
        <Typography level={3} style={{ marginTop: 24 }}>
          Biểu mẫu lựa và bổ sung phụ gia
        </Typography>
        <SelectingAddingAdditivesForm type="detail" />
      </Flex>

      <Flex gap={12} justify="end" className="btn-group-template">
        <Button onClick={() => navigate(-1)} color="primary">
          Quay lại
        </Button>
      </Flex>
    </>
  );
};

export const SelectingAddingAdditivesTemplate = () => {
  const navigate = useNavigate();
  const isCreatingSelectingAddingAdditives = useIsMutating({ mutationKey: [CREATE_SELECTING_ADDING_ADDITIVES] });

  const [isOpen, setIsOpen] = useState(false);

  const toggle = () => {
    setIsOpen(prev => !prev);
  };

  return (
    <>
      <Flex direction="column" align="center" justify="center">
        <Typography level={3} style={{ marginTop: 24 }}>
          Biểu mẫu lựa và bổ sung phụ gia
        </Typography>
        <SelectingAddingAdditivesForm toggle={toggle} type="create" />
      </Flex>

      <Flex gap={12} justify="end" className="btn-group-template">
        <Button onClick={() => navigate(-1)} outline className="btn-cancel-template">
          Huỷ
        </Button>
        <Button disabled={!!isCreatingSelectingAddingAdditives} color="primary" type="submit" form={FORM.SELECTING_ADDING_ADDITIVES}>
          Cập nhật
        </Button>
      </Flex>

      <ModalsTemplateCreateSuccess isOpen={isOpen} toggle={toggle} />
    </>
  );
};

export const SelectingAddingAdditivesTemplateUpdate = () => {
  const navigate = useNavigate();
  const isUpdatingSelectingAddingAdditives = useIsMutating({ mutationKey: [UPDATE_SELECTING_ADDING_ADDITIVES] });

  const [isOpen, setIsOpen] = useState(false);

  const toggle = () => {
    setIsOpen(prev => !prev);
  };

  return (
    <>
      <Flex direction="column" align="center" justify="center">
        <Typography level={3} style={{ marginTop: 24 }}>
          Cập nhật biểu mẫu lựa và bổ sung phụ gia
        </Typography>
        <SelectingAddingAdditivesForm toggle={toggle} type="update" />
      </Flex>

      <Flex gap={12} justify="end" className="btn-group-template">
        <Button onClick={() => navigate(-1)} outline className="btn-cancel-template">
          Huỷ
        </Button>
        <Button disabled={!!isUpdatingSelectingAddingAdditives} color="primary" type="submit" form={FORM.SELECTING_ADDING_ADDITIVES}>
          Cập nhật
        </Button>
      </Flex>

      <ModalsTemplateUpdateSuccess isOpen={isOpen} toggle={toggle} />
    </>
  );
};
