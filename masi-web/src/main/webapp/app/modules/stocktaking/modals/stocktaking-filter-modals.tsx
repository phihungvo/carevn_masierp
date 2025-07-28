import { zodResolver } from '@hookform/resolvers/zod';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import Modal from 'app/components/modal/modal';
import { DATE_FORMAT, DEFAULT_PAGE } from 'app/constants/common';
import {
  stocktakingFilterSchema,
  StocktakingFilterSchema,
} from 'app/validation/stocktaking.validation';
import dayjs from 'dayjs';
import { useContext } from 'react';
import { useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { StocktakingStatusOptions } from '../stocktaking-mapping';
import { StocktakingContext } from '../stocktaking-provider';

const StocktakingFilterModals = () => {
  const { isOpenFilter, toggleFilter, setFilter } =
    useContext(StocktakingContext);

  const { control, watch, setValue, formState } =
    useForm<StocktakingFilterSchema>({
      resolver: zodResolver(stocktakingFilterSchema),
    });

  const onOk = () => {
    const values = watch();
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      'status.equals': values.status,
      'checkDate.greaterThanOrEqual':
        values?.createdAt?.length === 2
          ? dayjs(values?.createdAt[0].toDate()).toISOString()
          : undefined,
      'checkDate.lessThanOrEqual':
        values?.createdAt?.length === 2
          ? dayjs(values?.createdAt[1].toDate()).toISOString()
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
      'checkDate.greaterThanOrEqual': undefined,
      'checkDate.lessThanOrEqual': undefined,
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
            options={StocktakingStatusOptions}
            placeholder="Vui lòng chọn trạng thái"
            isClearable={false}
          />
        </Col>
      </Row>
    </Modal>
  );
};

export default StocktakingFilterModals;
