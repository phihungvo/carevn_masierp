import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import FormSelect from 'app/components/form/form-select';
import useUom from 'app/hooks/use-uom';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { UomGroupSchema, uomGroupSchema } from 'app/validation/uom.validation';
import React, { useEffect } from 'react';
import { FormProvider, SubmitHandler, useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import UomGroupDetailsFields from './uom-group-details-fields';
import { IUomGroup } from 'app/shared/model/uom.model';

const { useCreateUomGroup, useUpdateUomGroup, useGetUoms, useGetUomGroupById } = useUom;

interface IUomGroupFormProps {
  type: 'create' | 'update';
  toggle?: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string | null;
  setSelectedRecord?: (value: string | null) => void;
}

const UomGroupForm = (props: IUomGroupFormProps) => {
  const { type, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const methods = useForm<UomGroupSchema>({
    resolver: zodResolver(uomGroupSchema),
    defaultValues: {
      uomGroupDetailsDTOs: [
        {
          name: '',
          baseUomId: '',
          baseQty: '',
          altUomId: '',
          altQty: '',
        },
      ],
    },
  });

  const { control, setValue, handleSubmit } = methods;

  const onOkSuccess = () => {
    toggle && toggle();
    toggleSuccess && toggleSuccess();
    setSelectedRecord && setSelectedRecord(null);
  };

  const { data: uoms, isLoading: loadingUoms } = useGetUoms();
  const { data: detail } = useGetUomGroupById(selectedRecord);
  const { mutate: update } = useUpdateUomGroup(selectedRecord, onOkSuccess);
  const { mutate: create } = useCreateUomGroup(onOkSuccess);

  const onSubmit: SubmitHandler<UomGroupSchema> = values => {
    const submitValues: IUomGroup = {
      name: values.name,
      baseUomId: values.baseUomId,
      uomGroupDetailsDTOs: values.uomGroupDetailsDTOs?.map(item => ({
        name: item.name,
        baseUomId: item.baseUomId,
        baseQty: Number(item.baseQty),
        altUomId: item.altUomId,
        altQty: Number(item.altQty),
      })),
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
      setValue('baseUomId', detail.baseUomId);
      detail.uomGroupDetailsDTOs?.forEach((item, index) => {
        setValue(`uomGroupDetailsDTOs.${index}.name`, item.name);
        setValue(`uomGroupDetailsDTOs.${index}.baseUomId`, item.baseUomId);
        setValue(`uomGroupDetailsDTOs.${index}.baseQty`, item.baseQty?.toString());
        setValue(`uomGroupDetailsDTOs.${index}.altUomId`, item.altUomId);
        setValue(`uomGroupDetailsDTOs.${index}.altQty`, item.altQty?.toString());
      });
    }
  }, [detail]);

  return (
    <FormProvider {...methods}>
      <Form id={FORM.UOM} onSubmit={handleSubmit(onSubmit)}>
        <Row>
          <Col md={6}>
            <FormInput control={control} id="name" name="name" label="Tên nhóm đơn vị" />
          </Col>

          <Col md={6}>
            <FormSelect
              control={control}
              id="baseUomId"
              name="baseUomId"
              placeholder="Chọn đơn vị"
              label="Đơn vị cơ bản"
              options={uoms?.data?.map(u => ({
                label: u?.name,
                value: u?.id,
              }))}
              isLoading={loadingUoms}
            />
          </Col>
        </Row>

        <Row>
          <UomGroupDetailsFields data={uoms?.data} isLoading={loadingUoms} />
        </Row>
      </Form>
    </FormProvider>
  );
};

export default UomGroupForm;
