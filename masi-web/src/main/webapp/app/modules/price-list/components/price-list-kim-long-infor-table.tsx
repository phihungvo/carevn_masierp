import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';
import React from 'react';
import { Label, Row } from 'reactstrap';
import { useFieldArray, useFormContext } from 'react-hook-form';
import { QuotationFormKimLongSchema } from 'app/validation/quotation.validation';
import { v4 } from 'uuid';
import PriceListFormTableKimLong from '../price-list-form-table-kim-long';

const PriceListKimLongInfoTable = () => {
  const { control } = useFormContext<QuotationFormKimLongSchema>();
  const { append } = useFieldArray<QuotationFormKimLongSchema>({
    control,
    name: 'quotationDetails',
  });
  const handleAddNew = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    append({
      id: v4(),
    });
  };

  return (
    <Row>
      <Flex justify="space-between" className="mb-4">
        <Label className="fw-bold">Thông tin sản phẩm:</Label>

        <Button color="primary" type="button" onClick={e => handleAddNew(e)}>
          Thêm
        </Button>
      </Flex>
      <PriceListFormTableKimLong />
    </Row>
  );
};

export default PriceListKimLongInfoTable;
