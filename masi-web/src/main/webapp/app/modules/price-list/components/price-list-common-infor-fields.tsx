import FormInput from 'app/components/form/form-input';
import FormSelect from 'app/components/form/form-select';
import { DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import useCustomers from 'app/hooks/use-customers';
import { QuotationFormKimLongSchema } from 'app/validation/quotation.validation';
import React from 'react';
import { useFormContext } from 'react-hook-form';
import { Col, Label, Row } from 'reactstrap';

const { useGetEnabledCustomers } = useCustomers;

const PriceListCommonInforFields = () => {
  const { control } = useFormContext<QuotationFormKimLongSchema>();

  const { data: customers, isLoading: cusLoading } = useGetEnabledCustomers({ size: DEFAULT_PAGE_SIZE_NAX });

  return (
    <>
      <Row>
        <Col md={12}>
          <Label className="fw-bold">Thông tin chung:</Label>
        </Col>

        <Col md={6}>
          <FormInput control={control} id="name" name="name" label="Tên bảng báo giá" />
        </Col>
        <Col md={6}>
          <FormSelect
            control={control}
            id="customerId"
            name="customerId"
            placeholder="Chọn khách hàng"
            label="Khách hàng"
            options={customers?.data?.map(c => ({
              label: c?.companyName,
              value: c?.id,
            }))}
            isLoading={cusLoading}
          />
        </Col>
      </Row>

      <div className="divider" />

      <Row>
        <Col md={12}>
          <Label className="fw-bold">Thông tin giao hàng:</Label>
        </Col>

        <Col md={6}>
          <FormInput control={control} id="packaging" name="packaging" label="Đóng gói" />
        </Col>

        <Col md={6}>
          <FormInput control={control} id="packagingEn" name="packagingEn" label="Đóng gói (En)" />
        </Col>

        <Col md={6}>
          <FormInput control={control} id="paymentMethod" name="paymentMethod" label="Phương thức thanh toán" />
        </Col>

        <Col md={6}>
          <FormInput control={control} id="paymentMethodEn" name="paymentMethodEn" label="Phương thức thanh toán (En)" />
        </Col>
      </Row>
    </>
  );
};

export default PriceListCommonInforFields;
