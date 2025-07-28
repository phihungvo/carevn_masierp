import { zodResolver } from '@hookform/resolvers/zod';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import Modal from 'app/components/modal/modal';
import {
  DATE_FORMAT,
  DEFAULT_PAGE,
  DEFAULT_PAGE_SIZE_NAX,
} from 'app/constants/common';
import useWarehouse from 'app/hooks/use-warehouse';
import {
  inventoriesFilterSchema,
  InventoriesFilterSchema,
} from 'app/validation/inventories.validation';
import dayjs from 'dayjs';
import { useContext } from 'react';
import { useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { InventoriesExportStatusOptions } from '../inventories-export-mapping';
import { InventoriesExportStorageContext } from '../inventories-storage-export-provider';

const { useGetWarehouses } = useWarehouse;

const InventoriesFilterModals = () => {
  const { isOpenFilter, toggleFilter, setFilter } = useContext(
    InventoriesExportStorageContext,
  );

  const { control, watch, setValue, formState } =
    useForm<InventoriesFilterSchema>({
      resolver: zodResolver(inventoriesFilterSchema),
    });

  const { data: warehouses } = useGetWarehouses({
    size: DEFAULT_PAGE_SIZE_NAX,
  });

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
      'incomingWarehouseId.equals': values.incomingWarehouseId,
    }));
    toggleFilter();
  };

  const onCancel = () => {
    setValue('createdAt', []);
    setValue('status', undefined);
    setValue('incomingWarehouseId', undefined);
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      'status.equals': undefined,
      'createdAt.greaterThanOrEqual': undefined,
      'createdAt.lessThanOrEqual': undefined,
      'incomingWarehouseId.equals': undefined,
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
            options={InventoriesExportStatusOptions}
            placeholder="Vui lòng chọn trạng thái"
            isClearable={false}
          />
        </Col>
        <Col md={6}>
          <FormSelect
            control={control}
            name="incomingWarehouseId"
            label="Kho"
            options={
              warehouses?.data?.map(x => ({
                label: `${x.code} - ${x.name}`,
                value: x.id,
              })) || []
            }
            placeholder="Chọn"
          />
        </Col>
      </Row>
    </Modal>
  );
};

export default InventoriesFilterModals;
