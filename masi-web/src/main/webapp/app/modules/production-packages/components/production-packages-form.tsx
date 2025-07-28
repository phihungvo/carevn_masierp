import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import { DEFAULT_INTEGER_REGEX } from 'app/constants/common';
import useEmployee from 'app/hooks/use-employee';
import useProductionCommand from 'app/hooks/use-production-command';
import useProductionPackage from 'app/hooks/use-production-package';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { MANUFACTURE_ORDER_STATUS } from 'app/shared/model/enumerations/production-command.model';
import { PRODUCTION_PACKAGES_STATUS } from 'app/shared/model/enumerations/production-packages.model';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import { convertCurrency } from 'app/shared/util/format';
import { handleValidatePaste } from 'app/shared/util/handle-valid-decimal';
import {
  ProductionPackageFormSchema,
  productionPackageSchema,
} from 'app/validation/production-packages.validation';
import dayjs from 'dayjs';
import { useEffect, useState } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { Col, Row } from 'reactstrap';
import { mapProductionPackagesStatusOptions } from '../production-packages-mapping';

const { useGetProductionCommandsQuery } = useProductionCommand;
const {
  useProductionPackageById,
  usePostProductionPackage,
  useUpdateProductionPackage,
} = useProductionPackage;
const { useGetEmployeesQuery } = useEmployee;

interface IProductionPackagesFormProps {
  type: 'create' | 'update';
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord?: string;
  setSelectedRecord?: (record: string) => void;
}

const ProductionPackagesForm = (props: IProductionPackagesFormProps) => {
  const { type, toggle, toggleSuccess, selectedRecord } = props;

  const [defaultCommand, setDefaultCommand] = useState<{
    value: string;
    label: string;
  }>(null);

  const { control, setValue, handleSubmit, formState, watch, setError } =
    useForm<ProductionPackageFormSchema>({
      resolver: zodResolver(productionPackageSchema),
      defaultValues: { packageAt: new DateObject() },
    });

  const statusMWatch = watch('statusU');

  const { data: detail } = useProductionPackageById(selectedRecord);
  const { mutate: create } = usePostProductionPackage(toggle, toggleSuccess);
  const { mutate: update } = useUpdateProductionPackage(
    selectedRecord,
    toggle,
    toggleSuccess,
  );

  const { data: productionCommands, isLoading: loadingProdCommands } =
    useGetProductionCommandsQuery({
      statuses: [
        MANUFACTURE_ORDER_STATUS.PACKAGING,
        MANUFACTURE_ORDER_STATUS.ADDITIVES,
        MANUFACTURE_ORDER_STATUS.PRODUCTION,
      ],
      isProductPackageSpecified: false,
    });
  const { data: employees } = useGetEmployeesQuery();

  const onSubmit: SubmitHandler<ProductionPackageFormSchema> = values => {
    const submitValues = {
      packageCode: values.packageCode,
      status: values.status as PRODUCTION_PACKAGES_STATUS,
      manufactureOrderId: values.manufactureOrderId,
      quantity: Number(values.quantity?.replace(/,/g, '').replace(/\./g, '')),
      packageBy: values.packageBy,
      packageAt: dayjs(values.packageAt.toDate()).toISOString(),
      note: values.note,
    };

    const toggleError = error => {
      const errorCode = error.response.data?.message;
      if (errorCode === 'error.packageCodeExists') {
        setError('packageCode', { message: 'Mã đóng gói đã tồn tại' });
      }
    };

    if (type === 'create') {
      create({ ...submitValues }, { onError: toggleError });
      return;
    }

    update({ ...submitValues });
  };

  useEffect(() => {
    if (detail) {
      setValue('packageCode', detail?.data?.packageCode);
      setValue('status', detail?.data?.status);
      setValue('statusU', detail?.data?.manufactureOrder?.status);
      setValue('manufactureOrderId', detail?.data?.manufactureOrderId);

      const qty = detail?.data?.quantity;
      setValue('quantity', formatDecimalPrecision(qty?.toString()));
      setValue('weight', convertCurrency(Number(qty) * 50, false));

      setValue('packageBy', detail?.data?.packageBy);
      setValue('packageAt', new DateObject(detail?.data?.packageAt));
      setValue('note', detail?.data?.note);

      setDefaultCommand({
        label: detail?.data?.manufactureOrder?.name,
        value: detail?.data?.manufactureOrderId,
      });
    }
  }, [detail]);

  const getOptionsManufactureOrders = () => {
    const listOptions = productionCommands?.data?.map(item => ({
      label: `${item?.name}`,
      value: item?.id,
    }));
    if (type === 'update') return [...(listOptions ?? []), defaultCommand];
    return [...(listOptions ?? [])];
  };

  const disabled =
    statusMWatch === (MANUFACTURE_ORDER_STATUS.PACKED_COMPLETED as string) ||
    statusMWatch === (MANUFACTURE_ORDER_STATUS.SHIPPED as string) ||
    statusMWatch === (MANUFACTURE_ORDER_STATUS.COMPLETED as string) ||
    statusMWatch === (MANUFACTURE_ORDER_STATUS.CANCELLED as string);

  return (
    <Form id={FORM.PRODUCTION_PACKAGES} onSubmit={handleSubmit(onSubmit)}>
      <Row>
        <Col md={6}>
          <FormInputV2
            control={control}
            id="code"
            name="packageCode"
            label="Mã đóng gói"
            placeholder="Vui lòng nhập mã đóng gói"
            disabled={type === 'update'}
          />
        </Col>
        <Col md={6}>
          <FormSelect
            control={control}
            id="status"
            name="status"
            label="Trạng thái"
            placeholder="Vui lòng chọn trạng thái"
            options={mapProductionPackagesStatusOptions}
            disabled={disabled}
          />
        </Col>
        <Col md={12}>
          <FormSelect
            control={control}
            id="manufactureOrderId"
            name="manufactureOrderId"
            placeholder="Chọn lệnh sản xuất"
            label="Lệnh sản xuất"
            options={getOptionsManufactureOrders()}
            isLoading={loadingProdCommands}
            disabled={disabled}
          />
        </Col>
        <Col md={6}>
          <FormInputV2
            control={control}
            id="quantity"
            name="quantity"
            label="Số lượng (bao 50kg)"
            onChange={e => {
              const value = e.target?.value;
              setValue('quantity', value);
              setValue('weight', convertCurrency(Number(value) * 50, false));
            }}
            onPaste={e => handleValidatePaste(e, DEFAULT_INTEGER_REGEX)}
            placeholder="Vui lòng nhập số lượng bao"
            disabled={disabled}
          />
        </Col>

        <Col md={6}>
          <FormInputV2
            control={control}
            id="weight"
            name="weight"
            label="Khối lượng (Kg)"
            placeholder="Số bao * 50"
            disabled
          />
        </Col>

        <Col md={6}>
          <FormSelect
            control={control}
            id="packageBy"
            name="packageBy"
            label="Người đóng gói"
            placeholder="Chọn người đóng gói"
            options={employees?.data?.map(x => ({
              value: x?.id,
              label: `${x?.code} - ${x?.employeeProfile?.fullName}`,
            }))}
            disabled={disabled}
          />
        </Col>

        <Col md={6}>
          <FormDatePickerV2
            control={control}
            formState={formState}
            id="packageAt"
            name="packageAt"
            label="Ngày đóng gói"
            placeholder="Chọn ngày đóng gói"
            setValue={setValue}
            disabled={disabled}
          />
        </Col>

        <Col md={12}>
          <FormInputV2
            control={control}
            id="note"
            name="note"
            label="Ghi chú"
            placeholder="Vui lòng nhập ghi chú"
            disabled={disabled}
          />
        </Col>
      </Row>
    </Form>
  );
};

export default ProductionPackagesForm;
