import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import { DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import useProductionMaintain from 'app/hooks/use-production-maintain';
import useProductionPackage from 'app/hooks/use-production-package';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { MANUFACTURE_ORDER_STATUS } from 'app/shared/model/enumerations/production-command.model';
import { handleMergeTime } from 'app/shared/util/date-utils';
import { convertCurrency } from 'app/shared/util/format';
import {
  ProductionMaintainFormSchema,
  productionMaintainSchema,
} from 'app/validation/production-maintain.validation';
import dayjs from 'dayjs';
import { useEffect, useState } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { Col, Row } from 'reactstrap';

const {
  useGetProductionMaintainById,
  usePatchProductionMaintain,
  usePostProductionMaintain,
} = useProductionMaintain;
const { useProductionPackages } = useProductionPackage;

interface IProductionMaintenanceFormProps {
  type: 'create' | 'update';
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord?: string;
  setSelectedRecord?: (record: string) => void;
}

const ProductionMaintenanceForm = (props: IProductionMaintenanceFormProps) => {
  const { type, toggle, toggleSuccess, selectedRecord, setSelectedRecord } =
    props;

  const [defaultObject, setDefaultObject] = useState<{
    value: string;
    label: string;
  }>(null);

  const { control, setValue, handleSubmit, setError, formState, watch } =
    useForm<ProductionMaintainFormSchema>({
      resolver: zodResolver(productionMaintainSchema),
    });

  const statusMWatch = watch('statusM');

  const { data } = useGetProductionMaintainById(selectedRecord);
  const { mutate: create } = usePostProductionMaintain(toggle, toggleSuccess);
  const { mutate: update } = usePatchProductionMaintain(
    selectedRecord,
    toggle,
    toggleSuccess,
  );
  const { data: productionPackages, isLoading: loadingProductionPackages } =
    useProductionPackages({
      size: DEFAULT_PAGE_SIZE_NAX,
      isProductMaintainSpecified: false,
    });

  const toggleError = error => {
    const errorCode = error.response.data?.message;
    if (errorCode === 'error.CODE_EXISTS') {
      setError('productBatchCode', { message: 'Mã lô hàng đã tồn tại' });
    }
  };

  const onSubmit: SubmitHandler<ProductionMaintainFormSchema> = values => {
    const submitValues = {
      productBatchCode: values.productBatchCode,
      productBatchName: values.productBatchName,
      manufactureDate: handleMergeTime(
        dayjs(values.manufactureDate.toDate()),
      ).toISOString(),
      expiredDate: handleMergeTime(
        dayjs(values.expiredDate.toDate()),
      ).toISOString(),
      productPackageId: values.productPackageId,
      note: values?.note,
    };

    if (type === 'update') {
      update({ ...submitValues });
      setSelectedRecord(null);
      return;
    }

    create({ ...submitValues }, { onError: toggleError });
  };

  useEffect(() => {
    if (data) {
      setValue('productBatchCode', data?.data?.productBatchCode);
      setValue('productBatchName', data?.data?.productBatchName);

      setValue(
        'statusM',
        data?.data?.productPackageDTO?.manufactureOrder?.status,
      );

      setValue('manufactureDate', new DateObject(data?.data?.manufactureDate));
      setValue('expiredDate', new DateObject(data?.data?.expiredDate));
      setValue('productPackageId', data?.data?.productPackageId);
      setValue('note', data?.data?.note);

      if (data?.data?.productPackageDTO) {
        const qty = data?.data?.productPackageDTO?.quantity ?? 0;
        setValue('productPackageQty', convertCurrency(qty, false));
        setValue('productPackageWeight', convertCurrency(qty * 50, false));
      }
    }

    setDefaultObject({
      label: data?.data?.productPackageDTO?.packageCode,
      value: data?.data?.productPackageId,
    });
  }, [data]);

  const getOptionsPackages = () => {
    const listOptions = productionPackages?.data?.map(item => ({
      label: `${item?.packageCode}`,
      value: item?.id,
    }));
    if (type === 'update') return [...(listOptions ?? []), defaultObject];
    return [...(listOptions ?? [])];
  };

  const disabled =
    statusMWatch === (MANUFACTURE_ORDER_STATUS.SHIPPED as string) ||
    statusMWatch === (MANUFACTURE_ORDER_STATUS.COMPLETED as string) ||
    statusMWatch === (MANUFACTURE_ORDER_STATUS.CANCELLED as string);

  return (
    <Form id={FORM.PRODUCTION_MAINTENANCE} onSubmit={handleSubmit(onSubmit)}>
      <Row>
        <Col md={6}>
          <FormInputV2
            control={control}
            id="productBatchCode"
            name="productBatchCode"
            label="Mã lô hàng"
            placeholder="Vui lòng nhập mã lô hàng"
            disabled={type === 'update'}
          />
        </Col>
        <Col md={6}>
          <FormInputV2
            control={control}
            id="productBatchName"
            name="productBatchName"
            label="Tên lô hàng"
            placeholder="Vui lòng nhập tên lô hàng"
            disabled={disabled}
          />
        </Col>
        <Col md={6}>
          <FormSelect
            control={control}
            id="productPackageId"
            name="productPackageId"
            placeholder="Chọn mã đóng gói"
            label="Mã đóng gói"
            options={getOptionsPackages()}
            isLoading={loadingProductionPackages}
            onChanges={e => {
              const selected = productionPackages?.data?.find(x => x.id === e);
              if (selected) {
                const qty = selected?.quantity ?? 0;
                setValue('productPackageQty', convertCurrency(qty, false));
                setValue(
                  'productPackageWeight',
                  convertCurrency(qty * 50, false),
                );
              }
            }}
            disabled={disabled}
          />
        </Col>
        <Col md={3}>
          <FormInputV2
            control={control}
            id="productPackageQty"
            name="productPackageQty"
            label="Số bao"
            placeholder="Số bao"
            disabled
          />
        </Col>
        <Col md={3}>
          <FormInputV2
            control={control}
            id="productPackageWeight"
            name="productPackageWeight"
            label="Số Kg"
            placeholder="Số kg"
            disabled
          />
        </Col>
        <Col md={6}>
          <FormDatePickerV2
            setValue={setValue}
            formState={formState}
            control={control}
            id="manufactureDate"
            name="manufactureDate"
            label="Ngày sản xuất"
            placeholder="Chọn ngày sản xuất"
            disabled={disabled}
          />
        </Col>
        <Col md={6}>
          <FormDatePickerV2
            control={control}
            formState={formState}
            setValue={setValue}
            id="expiredDate"
            name="expiredDate"
            label="Sử dụng đến"
            placeholder="Chọn thời hạn sử dụng đến"
            disabled={disabled}
          />
        </Col>
        <Col md={12}>
          <FormInputV2
            type="textarea"
            rows={3}
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

export default ProductionMaintenanceForm;
