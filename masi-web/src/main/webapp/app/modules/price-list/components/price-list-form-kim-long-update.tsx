import Form from 'app/components/form/form';
import React, { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { Col, Label, Row } from 'reactstrap';
import FormInput from 'app/components/form/form-input';
import { QuotationFormKimLongSchema, QuotationUpdateSchema, quotationUpdateSchema } from 'app/validation/quotation.validation';
import { zodResolver } from '@hookform/resolvers/zod';
import useQuotations from 'app/hooks/use-quotations';
import FormDatePicker from 'app/components/form/form-date-picker';
import Flex from 'app/components/flex/flex';
import { IQuotationCreate } from 'app/shared/model/quotation.model';
import { useParams } from 'react-router';
import { DateObject } from 'react-multi-date-picker';

const { usePatchQuotation, useGetQuotationById } = useQuotations;

interface IPriceListFormProps {
  toggleSuccess?: () => void;
}

const PriceListFormUpdate = (props: IPriceListFormProps) => {
  const { toggleSuccess } = props;

  const { id } = useParams();

  const { control, handleSubmit, setValue, formState } = useForm<QuotationUpdateSchema>({
    resolver: zodResolver(quotationUpdateSchema),
  });

  const { data: detail } = useGetQuotationById(id);
  const { mutate: update } = usePatchQuotation(id, toggleSuccess);

  const onSubmit: SubmitHandler<QuotationFormKimLongSchema> = data => {
    const submitValues: IQuotationCreate = {
      name: data?.name,
      description: detail?.description,
      quotationDetails: detail?.quotationDetails?.map(item => ({
        ...item,
        paymentMethod: data.paymentMethod,
        paymentMethodEn: data.paymentMethodEn,
        deliveryLocation: data.deliveryLocation,
        deliveryLocationEn: data.deliveryLocationEn,
        packaging: data.packaging,
        packagingEn: data.packagingEn,
        // deliveryDate: data.deliveryDate?.toDate()?.toISOString(),
        // minimumWeight: data.minimumWeight,
      })),
    };

    update(submitValues);
  };

  useEffect(() => {
    setValue('name', detail?.name);
    setValue('deliveryLocation', detail?.quotationDetails?.[0]?.deliveryLocation);
    setValue('deliveryLocationEn', detail?.quotationDetails?.[0]?.deliveryLocationEn);
    setValue('packaging', detail?.quotationDetails?.[0]?.packaging);
    setValue('packagingEn', detail?.quotationDetails?.[0]?.packagingEn);
    setValue('deliveryDate', new DateObject(detail?.quotationDetails?.[0]?.deliveryDate).add(7, 'hours'));
    setValue('minimumWeight', detail?.quotationDetails?.[0]?.minimumWeight);
    setValue('paymentMethod', detail?.quotationDetails?.[0]?.paymentMethod);
    setValue('paymentMethodEn', detail?.quotationDetails?.[0]?.paymentMethodEn);
  }, [detail]);

  return (
    <Form id={FORM.PRICE_LIST} onSubmit={handleSubmit(onSubmit)}>
      <Flex direction="column" gap={24}>
        <Row>
          <Col md={6}>
            <FormInput control={control} id="name" name="name" label="Tên bảng báo giá" />
          </Col>
        </Row>

        <Row>
          <Col md={12}>
            <Label className="fw-bold">Thông tin giao hàng:</Label>
          </Col>
          <Col md={6}>
            <FormInput control={control} id="deliveryLocation" name="deliveryLocation" label="Địa điểm giao hàng" />
          </Col>

          <Col md={6}>
            <FormInput control={control} id="deliveryLocationEn" name="deliveryLocationEn" label="Địa điểm giao hàng (En)" />
          </Col>

          <Col md={6}>
            <FormInput control={control} id="packaging" name="packaging" label="Đóng gói" />
          </Col>

          <Col md={6}>
            <FormInput control={control} id="packagingEn" name="packagingEn" label="Đóng gói (En)" />
          </Col>

          <Col md={6}>
            <FormDatePicker setValue={setValue} control={control} id="deliveryDate" name="deliveryDate" label="Thời gian giao hàng" formState={formState} />
          </Col>

          <Col md={6}>
            <FormInput control={control} id="minimumWeight" name="minimumWeight" label="Khối lượng giao hàng tối thiểu" />
          </Col>

          <Col md={6}>
            <FormInput control={control} id="paymentMethod" name="paymentMethod" label="Phương thức thanh toán" />
          </Col>

          <Col md={6}>
            <FormInput control={control} id="paymentMethodEn" name="paymentMethodEn" label="Phương thức thanh toán (En)" />
          </Col>
        </Row>
      </Flex>
    </Form>
  );
};

export default PriceListFormUpdate;
