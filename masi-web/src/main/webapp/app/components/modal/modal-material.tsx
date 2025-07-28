import React from 'react';
import Modal from './modal';
import Form from '../form/form';
import { FORM } from 'app/shared/model/enumerations/form.model';
import FormInput from '../form/form-input';
import { SubmitHandler, useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { MaterialSchema, materialSchema } from 'app/validation/material.validation';
import { zodResolver } from '@hookform/resolvers/zod';

const createOption = (label: string) => ({
  label,
  value: label,
});

interface ModalMaterialProps {
  isOpen: boolean;
  toggle: () => void;
  setListOptions: React.Dispatch<React.SetStateAction<any>>;
}

const ModalMaterial = (props: ModalMaterialProps) => {
  const { isOpen, toggle, setListOptions } = props;

  const { control, handleSubmit } = useForm<MaterialSchema>({
    resolver: zodResolver(materialSchema),
  });

  const onSubmit: SubmitHandler<MaterialSchema> = data => {
    const newOption = createOption(data.materialName);
    setListOptions(prev => [...prev, newOption]);
    toggle();
  };

  return (
    <Modal isOpen={isOpen} toggle={toggle}>
      <Form id={FORM.MATERIAL} onSubmit={handleSubmit(onSubmit)}>
        <Row>
          <Col md={6}>
            <FormInput control={control} id="materialName" name="materialName" label="Tên nguyên liệu" />
          </Col>
          <Col md={6}>
            <FormInput control={control} id="materialNameEn" name="materialNameEn" label="Tên nguyên liệu (En)" />
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default ModalMaterial;
