import { zodResolver } from '@hookform/resolvers/zod';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import Modal from 'app/components/modal/modal';
import {
  DATE_FORMAT,
  DEFAULT_PAGE,
  DEFAULT_PAGE_SIZE_NAX,
} from 'app/constants/common';
import { useInventoriesTypes } from 'app/hooks/use-inventories';
import {
  inventoriesFilterSchema,
  InventoriesFilterSchema,
} from 'app/validation/inventories.validation';
import { useContext } from 'react';
import { useForm } from 'react-hook-form';
import { Col, Label, Row } from 'reactstrap';
import { InventoriesStatusOptions } from '../inventories-mapping';
import { InventoriesStorageContext } from '../inventories-storage-provider';
import dayjs from 'dayjs';
import useSupplier from 'app/hooks/use-supplier';
import useWarehouse from 'app/hooks/use-warehouse';
import Flex from 'app/components/flex/flex';
import Input from 'app/components/input/input';

const { useGetSuppliers } = useSupplier;
const { useGetWarehouses } = useWarehouse;

const InventoriesFilterModals = () => {
  const { isOpenFilter, toggleFilter, setFilter } = useContext(
    InventoriesStorageContext,
  );

  const { control, watch, setValue, formState } =
    useForm<InventoriesFilterSchema>({
      resolver: zodResolver(inventoriesFilterSchema),
    });

  const { data: inventoriesTypes } = useInventoriesTypes({
    size: DEFAULT_PAGE_SIZE_NAX,
  });
  const { data: suppliers } = useGetSuppliers({ size: DEFAULT_PAGE_SIZE_NAX });
  const { data: warehouses } = useGetWarehouses({
    size: DEFAULT_PAGE_SIZE_NAX,
  });

  const isInvoice = watch('isInvoice');

  const onOk = () => {
    const values = watch();
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      'status.equals': values.status,
      'createdAt.greaterThanOrEqual':
        values?.createdAt?.length === 2
          ? dayjs(values?.createdAt[0].toDate()).format(DATE_FORMAT.YEAR_DATE)
          : undefined,
      'createdAt.lessThanOrEqual':
        values?.createdAt?.length === 2
          ? dayjs(values?.createdAt[1].toDate()).format(DATE_FORMAT.YEAR_DATE)
          : undefined,
      'customerId.equals': values.customerId,
      'inventoriesTypeId.equals': values.inventoriesTypeId,
      'incomingWarehouseId.equals': values.incomingWarehouseId,
      'isInvoice.equals': values.isInvoice,
    }));
    toggleFilter();
  };

  const onCancel = () => {
    setValue('createdAt', []);
    setValue('status', undefined);
    setValue('customerId', undefined);
    setValue('incomingWarehouseId', undefined);
    setValue('inventoriesTypeId', undefined);
    setValue('isInvoice', undefined);
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      'status.equals': undefined,
      'createdAt.greaterThanOrEqual': undefined,
      'createdAt.lessThanOrEqual': undefined,
      'customerId.equals': undefined,
      'inventoriesTypeId.equals': undefined,
      'incomingWarehouseId.equals': undefined,
      'isInvoice.equals': undefined,
    }));
    toggleFilter();
  };

  return (
    <Modal
      isOpen={isOpenFilter}
      toggle={toggleFilter}
      title="Bộ lọc"
      okText="Áp dụng"
      cancelText="Đặt lại"
      onOk={onOk}
      onCancel={onCancel}
      style={{ width: '600px' }}
    >
      <Row>
        <Col md={12}>
          <FormDatePickerV2
            control={control}
            formState={formState}
            name="createdAt"
            label="Khung thời gian"
            placeholder="Chọn khung thời gian"
            setValue={setValue}
            range
          />
        </Col>
        <Col md={6}>
          <FormSelect
            control={control}
            name="status"
            label="Trạng thái"
            options={InventoriesStatusOptions}
            placeholder="Vui lòng chọn trạng thái"
            isClearable={false}
          />
        </Col>
        <Col md={6}>
          <FormSelect
            control={control}
            name="inventoriesTypeId"
            label="Loại kho"
            options={
              inventoriesTypes?.data?.map(type => ({
                label: type.name,
                value: type.id,
              })) || []
            }
            placeholder="Vui lòng chọn loại kho"
            isClearable={false}
          />
        </Col>
        <FormSelect
          control={control}
          name="customerId"
          label="NCC"
          options={
            suppliers?.data?.map(supplier => ({
              label: `${supplier.code} - ${supplier.name}`,
              value: supplier.id,
            })) || []
          }
          placeholder="Chọn"
        />
        <FormSelect
          control={control}
          name="incomingWarehouseId"
          label="Kho nhập"
          options={
            warehouses?.data?.map(warehouse => ({
              label: `${warehouse.code}  ${warehouse.name}`,
              value: warehouse.id,
            })) || []
          }
          placeholder="Chọn"
        />
        <Flex gap={8}>
          <Input
            id="isInvoice"
            placeholder="Chọn"
            type="checkbox"
            checked={isInvoice}
            onChange={e => {
              e.target.checked
                ? setValue('isInvoice', true)
                : setValue('isInvoice', false);
            }}
          />
          <Label for="isInvoice">Đã có hoá đơn</Label>
        </Flex>
      </Row>
    </Modal>
  );
};

export default InventoriesFilterModals;
