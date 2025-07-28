import { zodResolver } from '@hookform/resolvers/zod';
import FormSelect from 'app/components/form/form-select';
import Modal from 'app/components/modal/modal';
import { DATE_FORMAT, DEFAULT_PAGE } from 'app/constants/common';
import { IProductionStandardParams } from 'app/shared/model/production-command.model';
import { generateMonthYearOptions } from 'app/shared/util/format';
import {
  productionStandardFilterSchema,
  ProductionStandardFilterSchema,
} from 'app/validation/production-standard.validation';
import dayjs from 'dayjs';
import React from 'react';
import { useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';

interface IProductionStandardFilterModals {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<IProductionStandardParams>>;
}

const ProductionStandardFilterModals = (
  props: IProductionStandardFilterModals,
) => {
  const { isOpen, toggle, setFilter } = props;

  const methods = useForm<ProductionStandardFilterSchema>({
    resolver: zodResolver(productionStandardFilterSchema),
  });
  const { control, setValue, setError, clearErrors, watch } = methods;

  const onOk = () => {
    const startDate = watch('startDate');
    const endDate = watch('endDate');

    if (dayjs(startDate, 'MM/YYYY') <= dayjs(endDate, 'MM/YYYY')) {
      setFilter(prev => ({
        ...prev,
        page: DEFAULT_PAGE,
        startDate: startDate
          ? dayjs(startDate, 'MM/YYYY').format(DATE_FORMAT.YEAR_DATE)
          : undefined,
        endDate: endDate
          ? dayjs(endDate, 'MM/YYYY')
              .endOf('month')
              .format(DATE_FORMAT.YEAR_DATE)
          : undefined,
      }));
      clearErrors('endDate');
      toggle();
    } else setError('endDate', { message: 'Tháng không hợp lệ' });
  };

  const onCancel = () => {
    setValue('startDate', undefined);
    setValue('endDate', undefined);

    setFilter(prev => ({
      ...prev,
      status: [],
      page: DEFAULT_PAGE,
      startDate: undefined,
      endDate: undefined,
    }));
    toggle();
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Áp dụng"
      cancelText="Đặt lại"
      onOk={onOk}
      onCancel={onCancel}
    >
      <Row>
        <Col md={6}>
          <FormSelect
            control={control}
            id="startDate"
            name="startDate"
            placeholder="Vui lòng chọn tháng"
            label="Từ tháng"
            options={generateMonthYearOptions(2024, 1, 2050, 1)}
          />
        </Col>

        <Col md={6}>
          <FormSelect
            control={control}
            id="endDate"
            name="endDate"
            placeholder="Vui lòng chọn tháng"
            label="Đến tháng"
            options={generateMonthYearOptions(2024, 1, 2050, 1)}
          />
        </Col>
      </Row>
    </Modal>
  );
};

export default ProductionStandardFilterModals;
