import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import { DATE_FORMAT, DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import useProductionMaintain from 'app/hooks/use-production-maintain';
import useProductionRoutings from 'app/hooks/use-production-routings';
import useWarehouse from 'app/hooks/use-warehouse';
import { FORM } from 'app/shared/model/enumerations/form.model';
import {
  MANUFACTURE_ORDER_STATUS,
  MANUFACTURE_ORDER_TYPE,
} from 'app/shared/model/enumerations/production-command.model';
import { convertCurrency } from 'app/shared/util/format';
import {
  ProductionRoutingFormSchema,
  productionRoutingSchema,
} from 'app/validation/production-routing.validation';
import dayjs from 'dayjs';
import { useEffect, useState } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { Col, Row } from 'reactstrap';

const { useGetWarehouses } = useWarehouse;
const { useGetProductionMaintains } = useProductionMaintain;
const {
  useGetProductionRoutingByIdQuery,
  usePostProductionRoutingMutation,
  useUpdateProductionRoutingMutation,
} = useProductionRoutings;

interface IProductionRoutingsFormProps {
  type: 'create' | 'update';
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord?: string;
  setSelectedRecord?: (record: string) => void;
}

const ProductionRoutingsForm = (props: IProductionRoutingsFormProps) => {
  const { type, toggle, toggleSuccess, selectedRecord, setSelectedRecord } =
    props;

  const [defaultObject, setDefaultObject] = useState<{
    value: string;
    label: string;
  }>(null);

  const { control, setValue, handleSubmit, formState, setError, watch } =
    useForm<ProductionRoutingFormSchema>({
      resolver: zodResolver(productionRoutingSchema),
    });

  const statusMWatch = watch('statusM');
  const typeManufactureOrderWatch = watch('typeManufactureOrder');

  const { data: maintains, isLoading: loadingMaintains } =
    useGetProductionMaintains({
      size: DEFAULT_PAGE_SIZE_NAX,
      isProductRoutingSpecified: false,
    });
  const { data: warehouseRouting, isLoading: loadingWarehouseRouting } =
    useGetWarehouses({
      size: DEFAULT_PAGE_SIZE_NAX,
      'warehouseTypePage.contains': typeManufactureOrderWatch
        ? typeManufactureOrderWatch ===
          (MANUFACTURE_ORDER_TYPE.MANUFACTURE_ORDER_BY_STANDARD as string)
          ? 'SEMI_FINISHED_PRODUCTS_STORAGE'
          : 'FINISHED_PRODUCTS_STORAGE'
        : undefined,
    });

  const { data } = useGetProductionRoutingByIdQuery(selectedRecord);
  const { mutate: create } = usePostProductionRoutingMutation(
    toggle,
    toggleSuccess,
  );
  const { mutate: update } = useUpdateProductionRoutingMutation(
    selectedRecord,
    toggle,
    toggleSuccess,
  );

  const toggleError = error => {
    const errorCode = error.response.data?.message;
    if (errorCode === 'error.CODE_EXISTS') {
      setError('name', { message: 'Mã lưu kho đã tồn tại' });
    }
  };

  const onSubmit: SubmitHandler<ProductionRoutingFormSchema> = values => {
    const submitValues = {
      name: values?.name,
      warehouseDate: values?.warehouseDate.toDate(),
      storageId: values?.storageId,
      productMaintainId: values?.productMaintainId,
      quantity: Number(values?.quantity ?? 0),
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
      setValue('name', data.data?.name);
      setValue('warehouseDate', new DateObject(data.data?.warehouseDate));
      setValue('storageId', data.data?.storageId);
      setValue('productMaintainId', data.data?.productMaintainId);

      const packageCode =
        data?.data?.productMaintainDTO?.productPackageDTO?.packageCode;
      setValue('productPackageName', packageCode);
      const qty = data?.data?.productMaintainDTO?.productPackageDTO?.quantity;
      setValue('quantity', qty.toString());
      setValue('productPackageWeight', convertCurrency(qty * 50, false));

      setValue('statusM', data.data?.manufactureOrder?.status);

      const manufacture = dayjs(
        data?.data?.productMaintainDTO.manufactureDate,
      ).format(DATE_FORMAT.DATE);
      const expired = dayjs(data?.data?.productMaintainDTO.expiredDate).format(
        DATE_FORMAT.DATE,
      );
      setDefaultObject({
        label: `${data?.data?.productMaintainDTO?.productBatchCode} - ${data?.data?.productMaintainDTO?.productBatchName} - NXS: ${manufacture} - HSD: ${expired}`,
        value: data?.data?.productMaintainId,
      });
    }
  }, [data]);

  const getOptionsMaintains = () => {
    const listOptions = maintains?.data?.map(s => {
      const manufacture = dayjs(s.manufactureDate).format(DATE_FORMAT.DATE);
      const expired = dayjs(s.expiredDate).format(DATE_FORMAT.DATE);
      return {
        label: `${s?.productBatchCode} - ${s?.productBatchName} - NXS: ${manufacture} - HSD: ${expired}`,
        value: s?.id,
      };
    });
    if (type === 'update') return [...(listOptions ?? []), defaultObject];
    return [...(listOptions ?? [])];
  };

  const disabled =
    statusMWatch === (MANUFACTURE_ORDER_STATUS.COMPLETED as string);

  return (
    <Form id={FORM.PRODUCTION_ROUTINGS} onSubmit={handleSubmit(onSubmit)}>
      <Row>
        <Col md={6}>
          <FormInputV2
            control={control}
            id="name"
            name="name"
            label="Mã lưu kho"
            placeholder="Vui lòng nhập mã lưu kho"
            disabled={disabled}
          />
        </Col>
        <Col md={6}>
          <FormDatePickerV2
            label="Ngày lưu kho"
            placeholder="Vui lòng chọn ngày lưu kho"
            control={control}
            name="warehouseDate"
            formState={formState}
            setValue={setValue}
            disabled={disabled}
          />
        </Col>
        <Col md={12}>
          <FormSelect
            control={control}
            id="productMaintainId"
            name="productMaintainId"
            placeholder="Chọn lô hàng"
            label="Lô hàng"
            options={getOptionsMaintains()}
            isLoading={loadingMaintains}
            onChanges={e => {
              const selected = maintains?.data?.find(x => x.id === e);
              if (selected) {
                const packageDTO = selected?.productPackageDTO;
                const name = `${packageDTO?.packageCode}`;
                setValue('productPackageName', name);
                const qty = packageDTO?.quantity;
                setValue('quantity', `${qty}`);
                const weight = (qty ?? 0) * 50;
                setValue(
                  'productPackageWeight',
                  convertCurrency(weight, false),
                );
                setValue(
                  'typeManufactureOrder',
                  packageDTO?.manufactureOrder?.manufactureOrderType,
                );
                setValue('storageId', '');
              } else setValue('typeManufactureOrder', '');
            }}
            disabled={disabled}
          />
        </Col>
        <Col md={12}>
          <FormSelect
            control={control}
            id="storageId"
            name="storageId"
            placeholder="Chọn nơi lưu trữ"
            label="Nơi lưu trữ"
            options={warehouseRouting?.data?.map(s => ({
              label: s?.name,
              value: s?.id,
            }))}
            isLoading={loadingWarehouseRouting}
            disabled={disabled}
          />
        </Col>
        <Col md={12}>
          <FormInputV2
            control={control}
            id="productPackageName"
            name="productPackageName"
            label="Mã đóng gói"
            disabled
            placeholder="Vui lòng chọn lô hàng"
          />
        </Col>
        <Col md={12}>
          <FormInputV2
            control={control}
            id="productPackageWeight"
            name="productPackageWeight"
            label="Khối lượng (Kg)"
            disabled
            placeholder="Vui lòng chọn lô hàng"
          />
        </Col>
      </Row>
    </Form>
  );
};

export default ProductionRoutingsForm;
