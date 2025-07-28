import { zodResolver } from '@hookform/resolvers/zod';
import Card from 'app/components/card/card';
import Form from 'app/components/form/form';
import FormDatePicker from 'app/components/form/form-date-picker';
import FormInput from 'app/components/form/form-input';
import FormSelect from 'app/components/form/form-select';
import useSupplier from 'app/hooks/use-supplier';
import useUniform from 'app/hooks/use-uniform';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { UNIFORM_STATUS } from 'app/shared/model/enumerations/uniform.model';
import { IUniformOrderCreate } from 'app/shared/model/uniform.model';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import { uniformOrderSchema, UniformOrderSchema } from 'app/validation/uniform.validation';
import React, { useEffect } from 'react';
import { FormProvider, SubmitHandler, useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { Col, Row } from 'reactstrap';
import UniformListFields from './uniform-list-fields';

const { useUniformOrderById, usePostUniformOrder, usePatchUniformOrder, useUniforms } = useUniform;
const { useGetSuppliers } = useSupplier;

interface IUniformFormProps {
  type: 'create' | 'update';
  toggle?: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string | null;
  setSelectedRecord?: (value: string | null) => void;
  setSelectedRowKeys?: React.Dispatch<React.SetStateAction<string[]>>;
}

const UniformOrdersForm = (props: IUniformFormProps) => {
  const { type, toggle, toggleSuccess, selectedRecord, setSelectedRecord, setSelectedRowKeys } = props;

  const methods = useForm<UniformOrderSchema>({
    resolver: zodResolver(uniformOrderSchema),
    defaultValues: {
      uniformDetails: [{ uniformId: '', quantity: '' }],
    },
  });

  const { control, setValue, handleSubmit, formState } = methods;

  const onOkSuccess = () => {
    toggle && toggle();
    toggleSuccess && toggleSuccess();
    setSelectedRecord(null);
  };

  const { data: suppliers, isLoading: loadingSuppliers } = useGetSuppliers();
  const { data, isLoading } = useUniforms({
    status: UNIFORM_STATUS.ENABLE,
  });
  const { data: detail } = useUniformOrderById(selectedRecord);
  const { mutate: create } = usePostUniformOrder(onOkSuccess);
  const { mutate: update } = usePatchUniformOrder(selectedRecord, onOkSuccess);

  const onSubmit: SubmitHandler<UniformOrderSchema> = values => {
    const submitValues: IUniformOrderCreate = {
      name: values.name,
      date: values.date?.toDate().toISOString(),
      supplierId: values.supplierId,
      supplierName: suppliers?.data?.find(e => e.id === values.supplierId)?.name ?? '',
      details: values.uniformDetails.map(item => ({
        quantity: Number(item.quantity?.replace(/,/g, '').replace(/\./g, '')),
        uniformId: item.uniformId,
        uomId: data?.data?.find(e => e.id === item.uniformId)?.uomId ?? '',
        uomName: data?.data?.find(e => e.id === item.uniformId)?.uomDTO?.name ?? '',
        basePrice: data?.data?.find(e => e.id === item.uniformId)?.basePrice ?? 0,
        actualPrice: Number(item.actualPrice?.replace(/,/g, '').replace(/\./g, '')),
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
      setValue('name', detail?.name);
      setValue('date', new DateObject(detail?.date));
      detail?.uniformFormDetails?.forEach((item, index) => {
        setValue(`uniformDetails.${index}.uniformId`, item?.uniform?.id);
        setValue(`uniformDetails.${index}.quantity`, formatDecimalPrecision(item?.quantity));
        setValue(`uniformDetails.${index}.actualPrice`, item?.actualPrice ? formatDecimalPrecision(item.actualPrice) : '0');
      });
    }
  }, [detail]);

  return (
    <FormProvider {...methods}>
      <Form id={FORM.UNIFORM} onSubmit={handleSubmit(onSubmit)}>
        <Card header="Thông tin chung" className="card-body-padding" classNameHeader="card-header-bold">
          <Row>
            <Col md={6}>
              <FormInput control={control} id="name" name="name" label="Tên đơn hàng" />
            </Col>

            <Col md={6}>
              <FormDatePicker setValue={setValue} control={control} id="date" name="date" label="Ngày" formState={formState} />
            </Col>

            <Col md={6}>
              <FormSelect
                control={control}
                id="supplierId"
                name="supplierId"
                label="Nhà cung cấp"
                placeholder="Chọn nhà cung cấp"
                options={suppliers?.data?.map(e => ({
                  label: e?.name,
                  value: e?.id,
                }))}
                isLoading={loadingSuppliers}
              />
            </Col>
          </Row>
        </Card>

        <div className="divider" />

        <Card header="Danh sách đồng phục" className="card-body-padding" classNameHeader="card-header-bold">
          <UniformListFields data={data?.data} isLoading={isLoading} />
        </Card>
      </Form>
    </FormProvider>
  );
};

export default UniformOrdersForm;
