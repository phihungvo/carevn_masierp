import { zodResolver } from '@hookform/resolvers/zod';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import Modal from 'app/components/modal/modal';
import { DATE_FORMAT, DEFAULT_PAGE } from 'app/constants/common';
import { IProductionMaintainParams } from 'app/shared/model/production-maintain.model';
import {
  productionMaintainFilterSchema,
  ProductionMaintainFilterSchema,
} from 'app/validation/production-maintain.validation';
import React from 'react';
import { useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';

interface IProductionMaintenanceFilterModalsProps {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<IProductionMaintainParams>>;
}

const ProductionMaintenanceFilterModals = (
  props: IProductionMaintenanceFilterModalsProps,
) => {
  const { isOpen, toggle, setFilter } = props;

  const { control, setValue, watch, getValues, formState } =
    useForm<ProductionMaintainFilterSchema>({
      resolver: zodResolver(productionMaintainFilterSchema),
    });

  const onOk = () => {
    const manufactureDateW = watch('manufactureDate');
    const expiredDateW = watch('expiredDate');
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      manufactureStartDate: manufactureDateW[0]?.format(DATE_FORMAT.YEAR_DATE),
      manufactureEndDate: manufactureDateW[1]
        ? manufactureDateW[1]?.format(DATE_FORMAT.YEAR_DATE)
        : manufactureDateW[0]?.format(DATE_FORMAT.YEAR_DATE),
      expiredStartDate: expiredDateW?.[0]?.format(DATE_FORMAT.YEAR_DATE),
      expiredEndDate: expiredDateW?.[1]
        ? expiredDateW?.[1]?.format(DATE_FORMAT.YEAR_DATE)
        : expiredDateW?.[0]?.format(DATE_FORMAT.YEAR_DATE),
    }));
    toggle();
  };

  const onCancel = () => {
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      manufactureStartDate: null,
      manufactureEndDate: null,
      expiredStartDate: null,
      expiredEndDate: null,
    }));
    setValue('manufactureDate', []);
    setValue('expiredDate', []);
    toggle();
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="production-maintenance-filter-modals"
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
            label="Ngày sản xuất"
            name="manufactureDate"
            placeholder="Chọn ngày sản xuất"
            range
          />
        </Col>
      </Row>
    </Modal>
  );
};

export default ProductionMaintenanceFilterModals;
