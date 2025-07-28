import { zodResolver } from '@hookform/resolvers/zod';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import Modal from 'app/components/modal/modal';
import { DEFAULT_PAGE } from 'app/constants/common';
import {
  companyFilterSchema,
  CompanyFilterSchema,
} from 'app/validation/company.validation';
import { useContext } from 'react';
import { useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { CompanyContext } from '../company-provider';

const CompanyFilterModals = () => {
  const { isOpenFilter, toggleFilter, setFilter } = useContext(CompanyContext);

  const { control, watch, setValue, formState } = useForm<CompanyFilterSchema>({
    resolver: zodResolver(companyFilterSchema),
  });

  const onOk = () => {
    setFilter(prev => ({ ...prev, page: DEFAULT_PAGE }));
    toggleFilter();
  };

  const onCancel = () => {
    setFilter(prev => ({ ...prev, page: DEFAULT_PAGE }));
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
      </Row>
    </Modal>
  );
};

export default CompanyFilterModals;
