import FormInput from 'app/components/form/form-input';
import { QuotationMMSSchema } from 'app/validation/quotation.validation';
import React from 'react';
import { useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';

const PriceListExtraCommonMMS = () => {
  const { control } = useFormContext<QuotationMMSSchema>();

  return (
    <>
      <Row>
        <Col md={6}>
          <FormInput control={control} id="materialCriteria" name="materialCriteria" label="Chỉ tiêu nguyên liệu" />
        </Col>

        <Col md={6}>
          <FormInput control={control} id="materialCriteriaEn" name="materialCriteriaEn" label="Chỉ tiêu nguyên liệu (En)" />
        </Col>
      </Row>

      <div className="divider" />
    </>
  );
};

export default PriceListExtraCommonMMS;
