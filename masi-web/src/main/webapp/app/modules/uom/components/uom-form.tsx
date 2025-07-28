import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import useUom from 'app/hooks/use-uom';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { UomSchema, uomSchema } from 'app/validation/uom.validation';
import React, { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';

const { useCreateUom, useUpdateUom, useGetUomById } = useUom;

interface IUomFormProps {
  type: 'create' | 'update';
  toggle?: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string | null;
  setSelectedRecord?: (value: string | null) => void;
}

const UomForm = (props: IUomFormProps) => {
  const { type, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { control, setValue, handleSubmit } = useForm<UomSchema>({
    resolver: zodResolver(uomSchema),
  });

  const onOkSuccess = () => {
    toggle && toggle();
    toggleSuccess && toggleSuccess();
    setSelectedRecord && setSelectedRecord(null);
  };

  const { data: detail } = useGetUomById(selectedRecord);
  const { mutate: update } = useUpdateUom(selectedRecord, onOkSuccess);
  const { mutate: create } = useCreateUom(onOkSuccess);

  const onSubmit: SubmitHandler<UomSchema> = values => {
    if (type === 'update') {
      update({
        name: values.name,
      });
      return;
    }

    create({
      name: values.name,
    });
  };

  useEffect(() => {
    if (detail) {
      setValue('name', detail.name);
    }
  }, [detail]);

  return (
    <Form id={FORM.UOM} onSubmit={handleSubmit(onSubmit)}>
      <Row>
        <Col md={12}>
          <FormInput control={control} id="name" name="name" label="Tên đơn vị" />
        </Col>
      </Row>
    </Form>
  );
};

export default UomForm;
