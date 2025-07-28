import { zodResolver } from '@hookform/resolvers/zod';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import Modal from 'app/components/modal/modal';
import { DATE_FORMAT, DEFAULT_PAGE } from 'app/constants/common';
import {
  SupplierContractsFilterSchema,
  supplierContractsSchema,
} from 'app/validation/supplier-contracts.validation';
import dayjs from 'dayjs';
import { useContext } from 'react';
import { useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { SupplierContractsStatusOptions } from '../supplier-contracts-mapping';
import { SupplierContractsContext } from '../supplier-contracts-storage-provider';

const SupplierContractsFilterModals = () => {
  const { isOpenFilter, toggleFilter, setFilter } = useContext(
    SupplierContractsContext,
  );

  const { control, watch, setValue, formState } =
    useForm<SupplierContractsFilterSchema>({
      resolver: zodResolver(supplierContractsSchema),
    });

  const onOk = () => {
    const values = watch();
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      'status.equals': values.status,
      'startDate.greaterThanOrEqual':
        values?.createdAt?.length === 2
          ? dayjs(values?.createdAt[0].toDate()).format(DATE_FORMAT.YEAR_DATE)
          : undefined,
      'startDate.lessThanOrEqual':
        values?.createdAt?.length === 2
          ? dayjs(values?.createdAt[1].toDate()).format(DATE_FORMAT.YEAR_DATE)
          : undefined,
    }));
    toggleFilter();
  };

  const onCancel = () => {
    setValue('createdAt', []);
    setValue('status', undefined);
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      'status.equals': undefined,
      'startDate.greaterThanOrEqual': undefined,
      'startDate.lessThanOrEqual': undefined,
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
        <Col md={6}>
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
            options={SupplierContractsStatusOptions}
            placeholder="Vui lòng chọn trạng thái"
            isClearable={false}
          />
        </Col>
      </Row>
    </Modal>
  );
};

export default SupplierContractsFilterModals;
