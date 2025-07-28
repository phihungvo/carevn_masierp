import { zodResolver } from '@hookform/resolvers/zod';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import Modal from 'app/components/modal/modal';
import { DEFAULT_PAGE } from 'app/constants/common';
import useSupplier from 'app/hooks/use-supplier';
import {
  PAYMENT_REQUEST_STATUS,
  PAYMENT_REQUEST_TYPE,
} from 'app/shared/model/enumerations/payment-request';
import { IPaymentRequestParams } from 'app/shared/model/payment-request.model';
import {
  RequestPaymentFilterSchema,
  requestPaymentFilterSchema,
} from 'app/validation/request-payment.validation';
import React from 'react';
import { useForm } from 'react-hook-form';
import { Col, FormGroup, Row } from 'reactstrap';
import {
  RequestPaymentStatusOptions,
  RequestPaymentTypeOptions,
} from './request-payment-mapping';
import dayjs from 'dayjs';

const { useGetSuppliers } = useSupplier;

interface IRequestPaymentFilterModalsProps {
  isOpen: boolean;
  toggle: () => void;
  setFilter: React.Dispatch<React.SetStateAction<IPaymentRequestParams>>;
}

const RequestPaymentFilterModals = (
  props: IRequestPaymentFilterModalsProps,
) => {
  const { isOpen, toggle, setFilter } = props;
  const methods = useForm<RequestPaymentFilterSchema>({
    resolver: zodResolver(requestPaymentFilterSchema),
    defaultValues: {
      paymentDate: [],
    },
  });

  const { control, getValues, setValue, watch, formState } = methods;

  const { data: suppliers } = useGetSuppliers();

  const onOk = () => {
    const paymentDate = watch('paymentDate');
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      'status.equals': getValues('status') as PAYMENT_REQUEST_STATUS,
      'type.equals': getValues('type') as PAYMENT_REQUEST_TYPE,
      'paymentDate.greaterThanOrEqual':
        paymentDate?.length === 2
          ? dayjs(paymentDate[0].toDate()).startOf('date').toISOString()
          : undefined,
      'paymentDate.lessThanOrEqual':
        paymentDate?.length === 2
          ? dayjs(paymentDate[1].toDate()).endOf('date').toISOString()
          : undefined,
      'supplierId.equals': getValues('supplierId'),
    }));
    toggle();
  };

  const onCancel = () => {
    setValue('type', undefined);
    setValue('status', undefined);
    setValue('supplierId', undefined);
    setValue('paymentDate', []);
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      'status.equals': undefined,
      'type.equals': undefined,
      'supplierId.equals': undefined,
      'paymentDate.greaterThanOrEqual': undefined,
      'paymentDate.lessThanOrEqual': undefined,
    }));
    toggle();
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="invoice-filter-modals"
      style={{ width: '500px' }}
      okText="Áp dụng"
      cancelText="Đặt lại"
      onOk={onOk}
      onCancel={onCancel}
    >
      <FormGroup>
        <Row>
          <Col md={12}>
            <FormDatePickerV2
              control={control}
              formState={formState}
              name="paymentDate"
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
              options={RequestPaymentStatusOptions}
              placeholder="Chọn trạng thái"
              isClearable={false}
            />
          </Col>
          <Col md={6}>
            <FormSelect
              control={control}
              name="type"
              label="Loại chứng từ"
              options={RequestPaymentTypeOptions}
              placeholder="Chọn loại chứng từ"
              isClearable={false}
            />
          </Col>
          <Col md={12}>
            <FormSelect
              control={control}
              name="supplierId"
              label="Nhà cung cấp"
              options={suppliers?.data?.map(x => ({
                value: x.id,
                label: `${x.code} - ${x.name}`,
              }))}
              placeholder="Chọn nhà cung cấp"
              isClearable={false}
            />
          </Col>
        </Row>
      </FormGroup>
    </Modal>
  );
};

export default RequestPaymentFilterModals;
