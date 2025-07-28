import { zodResolver } from '@hookform/resolvers/zod';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import Form from 'app/components/form/form';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import AuthGuard from 'app/components/guards/auth-guard';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { MANUFACTURE_ORDER_STATUS, MANUFACTURE_ORDER_TYPE } from 'app/shared/model/enumerations/production-command.model';
import {
  ManufactureOrderSchema,
  productionBatchSchema,
  ProductionBatchSchema,
} from 'app/validation/manufacture-order.validation';
import { useEffect, useState } from 'react';
import { FormProvider, useForm, useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';

export const TabBatch = ({
  onSubmit,
  error,
  type,
}: {
  onSubmit: (values, complete) => void;
  error: { key: string; message: string };
  type?: string;
}) => {
  const [completeState, setCompleteState] = useState<boolean>(false);

  const methods = useForm<ProductionBatchSchema>({
    resolver: zodResolver(productionBatchSchema),
  });
  const { control, setValue, formState, handleSubmit, watch, setError } =
    methods;

  const methodPrimary = useFormContext<ManufactureOrderSchema>();
  const { watch: watchPrimary, control: controlPrimary } = methodPrimary;

  const statusWatch = watchPrimary('status');
  const productionBatchWatch = watchPrimary('productionBatch');

  const disabled =
    statusWatch === (MANUFACTURE_ORDER_STATUS.SHIPPED as string) ||
    statusWatch === (MANUFACTURE_ORDER_STATUS.COMPLETED as string) ||
    statusWatch === (MANUFACTURE_ORDER_STATUS.CANCELLED as string);

  useEffect(() => {
    if (productionBatchWatch) {
      setValue('id', productionBatchWatch?.id);
      setValue('productBatchCode', productionBatchWatch?.productBatchCode);
      setValue('productBatchName', productionBatchWatch?.productBatchName);
      setValue('manufactureDate', productionBatchWatch?.manufactureDate);
      setValue('expiredDate', productionBatchWatch?.expiredDate);
    }
  }, [productionBatchWatch]);

  useEffect(() => {
    if (error && error?.key === 'productBatchCode') {
      setError('productBatchCode', { message: error?.message });
    }
  }, [error]);

  useEffect(() => {
    if (completeState) setCompleteState(false);
  }, [watch()]);

  return (
    <FormProvider {...methods}>
      <Form
        id={FORM.MANUFACTURE_ORDER_PRODUCTION_BATCH}
        onSubmit={handleSubmit(values => onSubmit(values, completeState))}
      >
        <Row>
          <Col md={3}>
            <FormInputV2
              control={control}
              name="productBatchCode"
              label="Mã lô hàng"
              placeholder="Vui lòng nhập mã lô hàng"
              disabled={disabled}
            />
          </Col>
          <Col md={3}>
            <FormInputV2
              control={control}
              name="productBatchName"
              label="Lô hàng"
              placeholder="Vui lòng nhập lô hàng"
              disabled={disabled}
            />
          </Col>
          <Col md={3}>
            <FormDatePickerV2
              control={control}
              formState={formState}
              setValue={setValue}
              name="manufactureDate"
              label="Ngày sản xuất"
              placeholder="Vui lòng chọn ngày sản xuất"
              disabled={disabled}
            />
          </Col>
          <Col md={3}>
            <FormDatePickerV2
              control={control}
              formState={formState}
              setValue={setValue}
              name="expiredDate"
              label="Thời gian sử dụng đến"
              placeholder="Vui lòng chọn thời gian sử dụng đến"
              disabled={disabled}
            />
          </Col>
          <Col md={3}>
            <FormInputV2
              control={controlPrimary}
              name="productionPackaging.packageCode"
              label="Mã đóng gói"
              placeholder="Vui lòng chọn mã đống gói"
              disabled
            />
          </Col>
        </Row>
        <div className="production-card__footer">
          <AuthGuard permissionKey={ type == MANUFACTURE_ORDER_TYPE.MANUFACTURE_ORDER_BY_ORDER ? 'PRODUCTION_MANUFACTURE_ORDER.EDIT' : 'PRODUCTION_MANUFACTURE_ORDER_STANDARD.EDIT'}>
            <ButtonV2
              type="submit"
              onClick={() => setCompleteState(true)}
              disabled={disabled}
              style={{ borderColor: '#027A48', color: '#027A48' }}
            >
              Hoàn thành
            </ButtonV2>
            <ButtonV2
              variant="solid"
              color="blue"
              type="submit"
              disabled={disabled}
            >
              Lưu
            </ButtonV2>
          </AuthGuard>
        </div>
      </Form>
    </FormProvider>
  );
};
