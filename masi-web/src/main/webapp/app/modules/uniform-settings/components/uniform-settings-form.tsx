import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import FormSelect from 'app/components/form/form-select';
import { DEFAULT_DECIMAL_REGEX } from 'app/constants/common';
import useUniform from 'app/hooks/use-uniform';
import useUom from 'app/hooks/use-uom';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { handleValidatePaste, handleValidDecimal } from 'app/shared/util/handle-valid-decimal';
import { uniformSchema, UniformSchema } from 'app/validation/uniform.validation';
import React, { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';

const { useGetUoms } = useUom;
const { useUniformById, useCreateUniform, useUpdateUniform } = useUniform;

interface IUniformSettingsFormProps {
  type: 'create' | 'update';
  toggle?: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string | null;
  setSelectedRecord?: (value: string | null) => void;
}

const UniformSettingsForm = (props: IUniformSettingsFormProps) => {
  const { type, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { control, setValue, handleSubmit } = useForm<UniformSchema>({
    resolver: zodResolver(uniformSchema),
  });

  const onOkSuccess = () => {
    toggle && toggle();
    toggleSuccess && toggleSuccess();
    setSelectedRecord && setSelectedRecord(null);
  };

  const { data: uoms, isLoading: loadingUoms } = useGetUoms();
  const { data: detail } = useUniformById(selectedRecord);
  const { mutate: update } = useUpdateUniform(selectedRecord, onOkSuccess);
  const { mutate: create } = useCreateUniform(onOkSuccess);

  const onSubmit: SubmitHandler<UniformSchema> = values => {
    if (type === 'update') {
      update({
        // code: values.code,
        name: values.name,
        basePrice: Number(values.basePrice),
        uomId: values.uomId,
      });
      return;
    }

    create({
      // code: values.code,
      name: values.name,
      basePrice: Number(values.basePrice),
      uomId: values.uomId,
    });
  };

  useEffect(() => {
    if (detail) {
      // setValue('code', detail.code);
      setValue('name', detail.name);
      setValue('basePrice', detail.basePrice?.toString());
      setValue('uomId', detail.uomId);
    }
  }, [detail]);

  return (
    <Form id={FORM.UNIFORM} onSubmit={handleSubmit(onSubmit)}>
      <Row>
        {/* <Col md={6}>
          <FormInput control={control} id="code" name="code" label="Mã đồng phục" />
        </Col> */}
        <Col md={6}>
          <FormInput control={control} id="name" name="name" label="Tên đồng phục" />
        </Col>
        <Col md={6}>
          <FormSelect
            control={control}
            id="uomId"
            name="uomId"
            placeholder="Chọn đơn vị"
            label="Đơn vị"
            options={uoms?.data?.map(u => ({
              label: u?.name,
              value: u?.id,
            }))}
            isLoading={loadingUoms}
          />
        </Col>
        <Col md={6}>
          <FormInput
            control={control}
            id="basePrice"
            name="basePrice"
            label="Đơn giá"
            onChange={e => handleValidDecimal<UniformSchema>(e.target.value, 'basePrice', DEFAULT_DECIMAL_REGEX, setValue)}
            onPaste={e => handleValidatePaste(e, DEFAULT_DECIMAL_REGEX)}
          />
        </Col>
      </Row>
    </Form>
  );
};

export default UniformSettingsForm;
