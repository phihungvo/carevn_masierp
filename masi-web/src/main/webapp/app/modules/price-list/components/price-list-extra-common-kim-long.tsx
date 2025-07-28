import FormInput from 'app/components/form/form-input';
import { QuotationFormKimLongSchema } from 'app/validation/quotation.validation';
import React from 'react';
import { useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';

const PriceListExtraCommonKimLong = () => {
  const { control } = useFormContext<QuotationFormKimLongSchema>();

  return (
    <>
      <Row>
        <Col md={6}>
          <FormInput control={control} id="deliveryLocation" name="deliveryLocation" label="Địa điểm giao hàng" />
        </Col>

        <Col md={6}>
          <FormInput control={control} id="deliveryLocationEn" name="deliveryLocationEn" label="Địa điểm giao hàng (En)" />
        </Col>

        {/* <Col md={6}>
        <FormInput control={control} id="minimumWeight" name="minimumWeight" label="Khối lượng giao hàng tối thiểu" />
      </Col>

      <Col md={6}>
        <FormDatePicker control={control} id="deliveryDate" name="deliveryDate" label="Thời gian giao hàng" />
      </Col>

      <Col md={6}>
        <FormInput control={control} id="priceType" name="priceType" label="Hình thức giá" />
      </Col>

      <Col md={6}>
        <FormInput control={control} id="priceTypeEn" name="priceTypeEn" label="Hình thức giá (En)" />
      </Col> */}
      </Row>
      <div className="divider" />
    </>
  );
};

export default PriceListExtraCommonKimLong;
