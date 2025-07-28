import { zodResolver } from '@hookform/resolvers/zod';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import Modal from 'app/components/modal/modal';
import { DEFAULT_PAGE } from 'app/constants/common';
import { calLCenterMappingGroupOptions } from 'app/modules/call-center/call-center-mapping';
import {
  complainFilterSchema,
  ComplainFilterSchema,
} from 'app/validation/complain.validation';
import dayjs from 'dayjs';
import { useContext } from 'react';
import { useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import {
  complainMappingStatusOptions,
  complainMappingTypeOptions,
} from '../complain-mapping';
import { ComplainContext } from '../complain-provider';
import { CALL_CENTER_STATUS } from 'app/shared/model/enumerations/call-center';

const ComplainFilterModals = () => {
  const { isOpenFilter, toggleFilter, setFilter } = useContext(ComplainContext);

  const { control, watch, setValue, formState } = useForm<ComplainFilterSchema>(
    {
      resolver: zodResolver(complainFilterSchema),
    },
  );

  const onOk = () => {
    const values = watch();
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      // 'status.equals': values.status,
      'typeCS.equals': values.type,
      'groupCS.equals': values.groupCS,
      'receptionDate.greaterThanOrEqual':
        values?.receptionDate?.length === 2
          ? dayjs(values?.receptionDate[0].toDate())
              .startOf('date')
              .toISOString()
          : undefined,
      'receptionDate.lessThanOrEqual':
        values?.receptionDate?.length === 2
          ? dayjs(values?.receptionDate[1].toDate())
              .endOf('date')
              .toISOString()
          : undefined,
      'updatedAt.greaterThanOrEqual':
        values?.processDate?.length === 2
          ? dayjs(values?.processDate[0].toDate())
              .startOf('date')
              .toISOString()
          : undefined,
      'updatedAt.lessThanOrEqual':
        values?.processDate?.length === 2
          ? dayjs(values?.processDate[1].toDate())
              .endOf('date')
              .toISOString()
          : undefined,
      'status.equals':
        values?.processDate?.length === 2
          ? CALL_CENTER_STATUS.COMPLETED
          : values.status,
    }));
    toggleFilter();
  };

  const onCancel = () => {
    setValue('receptionDate', []);
    setValue('processDate', []);
    setValue('status', undefined);
    setValue('type', undefined);
    setValue('groupCS', undefined);
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      'typeCS.equals': undefined,
      'groupCS.equals': undefined,
      'status.equals': undefined,
      'receptionDate.greaterThanOrEqual': undefined,
      'receptionDate.lessThanOrEqual': undefined,
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
            name="receptionDate"
            label="Ngày ghi nhận"
            placeholder="Chọn ngày ghi nhận"
            setValue={setValue}
            range
          />
        </Col>
        <Col md={6}>
          <FormDatePickerV2
            control={control}
            formState={formState}
            name="processDate"
            label="Ngày xử lý (từ ... đến ...)"
            placeholder="Chọn ngày xử lý"
            setValue={setValue}
            range
          />
        </Col>
        <Col md={6}>
          <FormSelect
            control={control}
            name="status"
            label="Trạng thái"
            options={complainMappingStatusOptions}
            placeholder="Vui lòng chọn trạng thái"
            isClearable={false}
          />
        </Col>
        <Col md={6}>
          <FormSelect
            control={control}
            name="type"
            label="Loại"
            options={complainMappingTypeOptions}
            placeholder="Vui lòng chọn loại"
            isClearable={false}
          />
        </Col>
        <Col md={6}>
          <FormSelect
            control={control}
            name="groupCS"
            label="Nhóm"
            options={calLCenterMappingGroupOptions}
            placeholder="Vui lòng chọn nhóm"
            isClearable={false}
          />
        </Col>
      </Row>
    </Modal>
  );
};

export default ComplainFilterModals;
