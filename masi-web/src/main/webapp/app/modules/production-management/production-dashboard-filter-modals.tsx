import { zodResolver } from '@hookform/resolvers/zod';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import Modal from 'app/components/modal/modal';
import { DATE_FORMAT, DEFAULT_PAGE } from 'app/constants/common';
import { IProductionPackageParams } from 'app/shared/model/production-package.model';
import {
  manufactureOrderFilterSchema,
  ManufactureOrderFilterSchema,
} from 'app/validation/manufacture-order.validation';
import dayjs from 'dayjs';
import React from 'react';
import { useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { mapProductionDashboardStatusOptions } from './production-mapping';

interface IProductionDashboardFilterModalsProps {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<IProductionPackageParams>>;
}

const ProductionDashboardFilterModals = (
  props: IProductionDashboardFilterModalsProps,
) => {
  const { isOpen, toggle, setFilter } = props;

  const { control, formState, watch, setValue } =
    useForm<ManufactureOrderFilterSchema>({
      resolver: zodResolver(manufactureOrderFilterSchema),
    });

  const statusWatch = watch('status');

  const onOk = () => {
    const values = watch();
    setFilter(prev => ({
      ...prev,
      statuses: [statusWatch],
      page: DEFAULT_PAGE,
      fromDate:
        values?.createdAt?.length === 2
          ? dayjs(values?.createdAt[0].toDate()).format(DATE_FORMAT.YEAR_DATE)
          : undefined,
      toDate:
        values?.createdAt?.length === 2
          ? dayjs(values?.createdAt[1].toDate()).format(DATE_FORMAT.YEAR_DATE)
          : undefined,
    }));
    toggle();
  };

  const onCancel = () => {
    setValue('createdAt', undefined);
    setValue('status', undefined);
    setFilter(prev => ({
      ...prev,
      statuses: [],
      page: DEFAULT_PAGE,
      fromDate: undefined,
      toDate: undefined,
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
          <FormDatePickerV2
            control={control}
            formState={formState}
            setValue={setValue}
            id="createdAt"
            name="createdAt"
            placeholder="Chọn lệnh sản xuất"
            range
          />
        </Col>
        <Col md={6}>
          <FormSelect
            control={control}
            id="status"
            name="status"
            placeholder="Chọn trạng thái"
            options={mapProductionDashboardStatusOptions}
            isClearable={false}
          />
        </Col>
      </Row>
    </Modal>
  );
};

export default ProductionDashboardFilterModals;
