import FormInputV2 from 'app/components/formV2/form-input/form-input';
import SupplierCodes from 'app/components/suppliersCode/SuppliesCode';
import { Typography } from 'app/components/typography/typography';
import { useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { IncomingInvoiceV2SchemaType } from '../validation/incoming.validate';
import { useEffect } from 'react';

type Props = {};

const Provider = (props: Props) => {
  const { control, watch, setValue, getValues } = useFormContext<IncomingInvoiceV2SchemaType>();
  const debtDays = watch('provider.debtDays');

  useEffect(() => {
    debtDays && setValue('relativedFees.0.debtDays', debtDays);
  }, [debtDays]);

  return (
    <section>
      <Row>
        <Typography level={6} style={{ marginBottom: '12px' }}>
          NCC
        </Typography>
      </Row>
      <Row>
        <Col md={6}>
          <SupplierCodes<any>
            autoSetValue={(supplierItem) => {
              const { taxCode, address, phone, name, id } = supplierItem
              setValue('provider.supplierTaxCode', taxCode);
              setValue('provider.supplierAddress', address);
              setValue('provider.supplierName', name)
              setValue('provider.supplierPhone', phone);
              setValue('provider.supplierId', id);
              let relativedDefault = getValues('relativedFees.0');
              relativedDefault.supplierId = id;
              relativedDefault.taxCode = taxCode;
              setValue('relativedFees.0', relativedDefault);
            }}
            name='provider.supplierId'
          />
        </Col>
        <Col md={3}>
          <FormInputV2 control={control} label="MST" name="provider.supplierTaxCode" disabled />
        </Col>
        <Col md={3}>
          <FormInputV2 control={control} label="Số ngày công nợ" name="provider.debtDays" />
        </Col>
      </Row>
      <Row>
        <Col md={12}>
          <FormInputV2 control={control} label="Địa chỉ" name="provider.supplierAddress" />
        </Col>
      </Row>
      <Row>
        <Col md={6}>
          <FormInputV2 control={control} label="Người đại diện" name="provider.supplierName" />
        </Col>
        <Col md={6}>
          <FormInputV2 control={control} label="SĐT" name="provider.supplierPhone" />
        </Col>
      </Row>
    </section>
  );
};

export default Provider;
