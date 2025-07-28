import Form from 'app/components/form/form';
import React, { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { zodResolver } from '@hookform/resolvers/zod';
import { useAppSelector } from 'app/config/store';
import useContracts from 'app/hooks/use-contracts';
import { useParams } from 'react-router';
import { quotationDetailsMMSSchema, QuotationDetailsMMSSchema } from 'app/validation/quotation-details.validation';
import useQuotationDetails from 'app/hooks/use-quotationDetails';

const { usePostQuotationDetails, useGetQuotationDetailsById, usePatchQuotationDetails } = useQuotationDetails;
const { useGetContractMaterials } = useContracts;

interface IPriceListMMSFormProps {
  type: 'create' | 'update';
  toggleSuccess?: () => void;
}

const PriceListMMSDetailForm = (props: IPriceListMMSFormProps) => {
  const { type, toggleSuccess } = props;

  const { id, detailId } = useParams();
  const account = useAppSelector(state => state.authentication.account);

  const { control, handleSubmit, setValue } = useForm<QuotationDetailsMMSSchema>({
    resolver: zodResolver(quotationDetailsMMSSchema),
  });

  const { data } = useGetQuotationDetailsById(detailId);
  const { data: contractMaterials, isLoading: loadingMaterial } = useGetContractMaterials();
  const { mutate: create } = usePostQuotationDetails(toggleSuccess);
  const { mutate: update } = usePatchQuotationDetails(detailId, toggleSuccess);

  const onSubmit: SubmitHandler<QuotationDetailsMMSSchema> = data => {
    // const submitValues: IQuotationDetail = {
    //   id: detailId,
    //   deliveryLocation: data.deliveryLocation,
    //   deliveryLocationEn: data.deliveryLocationEn,
    //   deliveryDate: data.deliveryDate?.toDate()?.toISOString(),
    //   packaging: data.packaging,
    //   packagingEn: data.packagingEn,
    //   weight: data.weight,
    //   paymentMethod: data.paymentMethod,
    //   paymentMethodEn: data.paymentMethodEn,
    //   materialId: data.materialId,
    //   price: data.price,
    //   materialCriteria: data.materialCriteria,
    //   materialCriteriaEn: data.materialCriteriaEn,
    //   note: data.note,
    //   quotationId: id,
    // };
    // if (type === 'update') {
    //   update(submitValues);
    //   return;
    // }
    // create(submitValues);
  };

  useEffect((): void => {
    // if (data) {
    //   setValue('materialId', data.materialId);
    //   setValue('weight', data.weight);
    //   setValue('deliveryLocation', data.deliveryLocation);
    //   setValue('deliveryLocationEn', data.deliveryLocationEn);
    //   setValue('packaging', data.packaging);
    //   setValue('packagingEn', data.packagingEn);
    //   setValue('deliveryDate', new DateObject(data.deliveryDate).add(7, 'hours'));
    //   setValue('paymentMethod', data.paymentMethod);
    //   setValue('paymentMethodEn', data.paymentMethodEn);
    //   setValue('materialCriteria', data.materialCriteria);
    //   setValue('materialCriteriaEn', data.materialCriteriaEn);
    //   setValue('price', data.price);
    //   setValue('note', data.note);
    // }
  }, [data]);

  return (
    <Form id={FORM.PRICE_LIST} onSubmit={handleSubmit(onSubmit)}>
      <></>
      {/* <Flex direction="column" gap={24}>
        <Row>
          <Col md={12}>
            <FormSelect
              control={control}
              id="materialId"
              name="materialId"
              label="Loại hàng"
              placeholder="Chọn loại hàng"
              options={contractMaterials?.data?.map(c => ({
                label: c?.name,
                value: c?.id,
              }))}
              isLoading={loadingMaterial}
            />
          </Col>

          <Row>
            <Col md={6}>
              <FormInput control={control} id="paymentMethod" name="paymentMethod" label="Phương thức thanh toán" />
            </Col>

            <Col md={6}>
              <FormInput control={control} id="paymentMethodEn" name="paymentMethodEn" label="Phương thức thanh toán (En)" />
            </Col>

            <Col md={6}>
              <FormDatePicker control={control} id="deliveryDate" name="deliveryDate" label="Thời gian giao hàng" />
            </Col>
          </Row>

          <Row>
            <Col md={6}>
              <FormInput control={control} id="price" name="price" label="Đơn giá (Vnđ/kg)" />
            </Col>

            <Col md={6}>
              <FormInput control={control} id="weight" name="weight" label="Khối lượng (Tấn)" />
            </Col>
          </Row>

          <Row>
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
          </Row>

          <Row>
            <Col md={6}>
              <FormInput control={control} id="materialCriteria" name="materialCriteria" label="Chỉ tiêu nguyên liệu" />
            </Col>

            <Col md={6}>
              <FormInput control={control} id="materialCriteriaEn" name="materialCriteriaEn" label="Chỉ tiêu nguyên liệu (En)" />
            </Col>
          </Row>

          <Row>
            <Col md={12}>
              <FormInput control={control} id="note" name="note" label="Thông tin sản phẩm" type="textarea" rows={5} />
            </Col>
          </Row>
        </Row>
      </Flex> */}
    </Form>
  );
};

export default PriceListMMSDetailForm;
