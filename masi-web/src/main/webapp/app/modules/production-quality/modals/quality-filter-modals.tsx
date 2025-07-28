import { zodResolver } from '@hookform/resolvers/zod';
import FormSelect from 'app/components/form/form-select';
import Modal from 'app/components/modal/modal';
import { DEFAULT_PAGE } from 'app/constants/common';
import { IProductionPackageParams } from 'app/shared/model/production-package.model';
import {
  productionPackageFilterSchema,
  ProductionPackageFilterSchema,
} from 'app/validation/production-packages.validation';
import React from 'react';
import { useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';

interface IQualityFilterModalsProps {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<IProductionPackageParams>>;
}

const QualityFilterModals = (props: IQualityFilterModalsProps) => {
  const { isOpen, toggle, setFilter } = props;

  const { control, watch, setValue } = useForm<ProductionPackageFilterSchema>({
    resolver: zodResolver(productionPackageFilterSchema),
  });
  const statusWatch = watch('status');
  const manufactureOrderIdWatch = watch('manufactureOrderId');

  const onOk = () => {
    setFilter(prev => ({
      ...prev,
      manufactureOrderIds: [manufactureOrderIdWatch],
      isExistItem:
        statusWatch === 'true'
          ? true
          : statusWatch === 'false'
            ? false
            : undefined,
      page: DEFAULT_PAGE,
    }));
    toggle();
  };

  const onCancel = () => {
    setValue('manufactureOrderId', undefined);
    setValue('status', undefined);
    setFilter(prev => ({
      ...prev,
      manufactureOrderIds: [],
      isExistItem: undefined,
      page: DEFAULT_PAGE,
    }));
    toggle();
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="production-packages-filter-modals"
      okText="Áp dụng"
      cancelText="Đặt lại"
      onOk={onOk}
      onCancel={onCancel}
    >
      <Row>
        <Col md={6}>
          <FormSelect
            control={control}
            id="status"
            name="status"
            placeholder="Chọn trạng thái"
            options={[
              { value: 'false', label: 'Chờ kết quả' },
              { value: 'true', label: 'Đã có kết quả' },
            ]}
            isClearable={false}
          />
        </Col>
      </Row>
    </Modal>
  );
};

export default QualityFilterModals;
