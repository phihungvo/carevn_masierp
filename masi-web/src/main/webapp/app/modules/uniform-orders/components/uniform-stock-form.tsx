import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import useUniform from 'app/hooks/use-uniform';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { uniformStockSchema, UniformStockSchema } from 'app/validation/uniform.validation';
import React, { useEffect } from 'react';
import { FormProvider, SubmitHandler, useForm } from 'react-hook-form';
import { Alert, Col, Row } from 'reactstrap';
import UniformStockFields from './uniform-stock-list-fields';
import { UNIFORM_INVALID_RETURN } from 'app/constants/error';
import FormSelect from 'app/components/form/form-select';
import useWarehouse from 'app/hooks/use-warehouse';

const { useUniformOrderById, usePostUniformOrderStock } = useUniform;
const { useGetWarehouses } = useWarehouse;

interface IUniformStockProps {
  onOkSuccess?: () => void;
  selectedRecord?: string;
}

const UniformStockForm = (props: IUniformStockProps) => {
  const { onOkSuccess, selectedRecord } = props;

  const methods = useForm<UniformStockSchema>({
    resolver: zodResolver(uniformStockSchema),
  });

  const { setValue, handleSubmit, control } = methods;

  const { data: warehouses, isLoading: loadingWarehouses } = useGetWarehouses({
    isNotGetWareHouseUniform: false,
  });
  const { data } = useUniformOrderById(selectedRecord);
  const { mutate, error } = usePostUniformOrderStock(onOkSuccess);

  useEffect(() => {
    if (data) {
      data?.uniformFormDetails?.forEach((u, index) => {
        setValue(`uniformFormDetailDTO.${index}.uniformId`, u.uniformId);
        setValue(`uniformFormDetailDTO.${index}.initQuantity`, u.quantity?.toString());
        setValue(`uniformFormDetailDTO.${index}.returnedQuantity`, u.quantityChange ? u.quantityChange?.toString() : '0');
      });
    }
  }, [data]);

  const onSubmit: SubmitHandler<UniformStockSchema> = values => {
    mutate({
      uniformOrderId: selectedRecord,
      warehouseId: values.warehouseId,
      warehouseName: warehouses?.data?.find(w => w.id === values.warehouseId)?.name,
      uniformFormDetailDTO: values.uniformFormDetailDTO
        ?.filter(item => !isNaN(Number(item?.quantity)))
        ?.map(e => ({
          uniformId: e.uniformId,
          quantity: e.quantity ? Number(e.quantity) : 0,
        })),
    });
  };

  const isNotEmpFound = error?.response?.data?.message === UNIFORM_INVALID_RETURN;

  return (
    <>
      {isNotEmpFound && (
        <Alert color="danger" className="mb-4">
          Số lượng nhập kho không hợp lệ
        </Alert>
      )}

      <FormProvider {...methods}>
        <Form id={FORM.UNIFORM} onSubmit={handleSubmit(onSubmit)}>
          <Row>
            <Col md={12}>
              <FormSelect
                control={control}
                id="warehouseId"
                name="warehouseId"
                label="Kho"
                placeholder="Chọn kho"
                options={warehouses?.data
                  ?.filter(i => i.warehouseTypePage == 'UNIFORM_WAREHOUSE')
                  ?.map(w => ({
                  label: w?.name,
                  value: w?.id,
                }))}
                isLoading={loadingWarehouses}
              />
            </Col>
          </Row>
          <Row>
            <UniformStockFields />
          </Row>
        </Form>
      </FormProvider>
    </>
  );
};

export default UniformStockForm;
