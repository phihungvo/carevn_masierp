import { zodResolver } from '@hookform/resolvers/zod';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import Form from 'app/components/form/form';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import AuthGuard from 'app/components/guards/auth-guard';
import { DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import useEmployee from 'app/hooks/use-employee';
import { mapProductionPackagesStatusOptions } from 'app/modules/production-packages/production-packages-mapping';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { MANUFACTURE_ORDER_TYPE } from 'app/shared/model/enumerations/production-command.model';
import { convertCurrency } from 'app/shared/util/format';
import {
  ManufactureOrderSchema,
  productionPackagingSchema,
  ProductionPackagingSchema,
} from 'app/validation/manufacture-order.validation';
import { useEffect, useState } from 'react';
import { FormProvider, useForm, useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { enable, enableDirectShipment } from '../../production-ultis';

const { useGetEmployeesQuery } = useEmployee;
export const TabPackaging = ({
  onSubmit,
  error,
  type,
}: {
  onSubmit: (values, complete) => void;
  error: { key: String; message: string };
  type?: string;
}) => {
  const [completeState, setCompleteState] = useState<boolean>(false);

  const methods = useForm<ProductionPackagingSchema>({
    resolver: zodResolver(productionPackagingSchema),
  });
  const { control, setValue, formState, handleSubmit, watch, setError } =
    methods;
  const quantityWatch = watch('quantity');

  const methodPrimary = useFormContext<ManufactureOrderSchema>();
  const { watch: watchPrimary } = methodPrimary;

  const statusWatch = watchPrimary('status');
  const productionPackagingWatch = watchPrimary('productionPackaging');

  const { data: employees } = useGetEmployeesQuery({
    size: DEFAULT_PAGE_SIZE_NAX,
  });

  const disabled = enableDirectShipment(statusWatch);

  useEffect(() => {
    if (productionPackagingWatch) {
      setValue('id', productionPackagingWatch?.id);
      setValue('packageCode', productionPackagingWatch?.packageCode);
      setValue('packageAt', productionPackagingWatch?.packageAt);
      setValue(
        'weight',
        convertCurrency(Number(productionPackagingWatch?.weight ?? 0), false),
      );
      setValue('packageBy', productionPackagingWatch?.packageBy);
      setValue('quantity', productionPackagingWatch?.quantity);
      setValue('note', productionPackagingWatch?.note);
      setValue('status', productionPackagingWatch?.status);
    }
  }, [productionPackagingWatch]);

  useEffect(() => {
    if (error && error?.key === 'packageCode') {
      setError('packageCode', { message: error?.message });
    }
  }, [error]);

  useEffect(() => {
    if (completeState) setCompleteState(false);
  }, [watch()]);

  return (
    <FormProvider {...methods}>
      <Form
        id={FORM.MANUFACTURE_ORDER_PRODUCTION_PACKAGING}
        onSubmit={handleSubmit(values => onSubmit(values, completeState))}
      >
        <Row>
          <Col md={4}>
            <FormInputV2
              control={control}
              name="packageCode"
              label="Mã đóng gói"
              placeholder="Vui lòng nhập mã đóng gói"
              disabled={disabled}
            />
          </Col>
          <Col md={4}>
            <FormSelect
              control={control}
              name="packageBy"
              label="Người đóng gói"
              placeholder="Vui lòng chọn người đóng gói"
              options={employees?.data?.map(x => ({
                value: x?.id,
                label: `${x.code} - ${x.employeeProfile?.fullName}`,
              }))}
              disabled={disabled}
            />
          </Col>
          <Col md={4}>
            <FormDatePickerV2
              control={control}
              formState={formState}
              setValue={setValue}
              name="packageAt"
              label="Ngày đóng gói"
              placeholder="Vui lòng nhập ngày đóng gói"
              disabled={disabled}
            />
          </Col>
          <Col md={4}>
            <FormInputV2
              control={control}
              name="quantity"
              label="Số bao (50 Kg)"
              placeholder="Vui lòng nhập số bao"
              disabled={disabled}
              onChange={e => {
                const weight = Number(e.target.value) ?? 0;
                setValue('weight', convertCurrency(weight * 50, false));
              }}
            />
          </Col>
          <Col md={4}>
            <FormInputV2
              control={control}
              name="weight"
              label="Khối lượng thành phẩm"
              placeholder="Số bao * 50"
              value={Number(quantityWatch ?? 0) * 50}
              disabled
            />
          </Col>
          <Col md={4}>
            <FormSelect
              control={control}
              name="status"
              label="Trạng thái"
              placeholder="Vui lòng chọn trạng thái"
              options={mapProductionPackagesStatusOptions}
              disabled={enable(statusWatch)}
            />
          </Col>
          <Col md={12}>
            <FormInputV2
              control={control}
              name="note"
              label="Ghi chú"
              placeholder="Vui lòng nhập ghi chú"
              disabled={disabled}
            />
          </Col>
        </Row>
        <div className="production-card__footer">
          <AuthGuard
            permissionKey={
              type === MANUFACTURE_ORDER_TYPE.MANUFACTURE_ORDER_BY_ORDER
                ? 'PRODUCTION_MANUFACTURE_ORDER.EDIT'
                : 'PRODUCTION_MANUFACTURE_ORDER_STANDARD.EDIT'
            }
          >
            <ButtonV2
              type="submit"
              onClick={() => setCompleteState(true)}
              disabled={disabled}
              style={{ borderColor: '#027A48', color: '#027A48' }}
            >
              Hoàn thành
            </ButtonV2>
            <ButtonV2 variant="solid" color="blue" type="submit">
              Lưu
            </ButtonV2>
          </AuthGuard>
        </div>
      </Form>
    </FormProvider>
  );
};
