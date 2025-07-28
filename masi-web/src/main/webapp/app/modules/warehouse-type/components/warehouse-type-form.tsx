import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React, { useEffect } from 'react';
import { FormProvider, SubmitHandler, useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import useWarehouseType from 'app/hooks/use-warehouse-type';
import { warehouseTypeSchema, WarehouseTypeSchema } from 'app/validation/warehouse-type.validation';
import { IWarehouseType } from 'app/shared/model/warehouse-type.model';

const { useCreateWarehouseType, useUpdateWarehouseType, useGetWarehouseTypeById } = useWarehouseType;

interface IWarehouseTypeFormProps {
  type: 'create' | 'update';
  toggle?: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string | null;
  setSelectedRecord?: (value: string | null) => void;
}

const WarehouseTypeForm = (props: IWarehouseTypeFormProps) => {
  const { type, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const methods = useForm<WarehouseTypeSchema>({
    resolver: zodResolver(warehouseTypeSchema),
  });

  const { control, setValue, handleSubmit } = methods;

  const onOkSuccess = () => {
    toggle && toggle();
    toggleSuccess && toggleSuccess();
    setSelectedRecord && setSelectedRecord(null);
  };

  const { data: detail } = useGetWarehouseTypeById(selectedRecord);
  const { mutate: update } = useUpdateWarehouseType(selectedRecord, onOkSuccess);
  const { mutate: create } = useCreateWarehouseType(onOkSuccess);

  const onSubmit: SubmitHandler<WarehouseTypeSchema> = values => {
    const submitValues: IWarehouseType = {
      name: values.name,
      description: values.description,
    };

    if (type === 'update') {
      update(submitValues);
      return;
    }

    create(submitValues);
  };

  useEffect(() => {
    if (detail) {
      setValue('name', detail.name);
      setValue('description', detail.description);
    }
  }, [detail]);

  return (
    <FormProvider {...methods}>
      <Form id={FORM.WAREHOUSE_TYPE} onSubmit={handleSubmit(onSubmit)}>
        <Row>
          <Col md={12}>
            <FormInput control={control} id="name" name="name" label="Tên loại kho" />
          </Col>

          <Col md={12}>
            <FormInput control={control} id="description" name="description" label="Mô tả" type="textarea" />
          </Col>
        </Row>
      </Form>
    </FormProvider>
  );
};

export default WarehouseTypeForm;
