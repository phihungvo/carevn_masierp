import Form from 'app/components/form/form';
import React, { ReactElement } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { zodResolver } from '@hookform/resolvers/zod';
import { useAppSelector } from 'app/config/store';
import { useParams } from 'react-router';
import useQuotationDetails from 'app/hooks/use-quotationDetails';
import { QuotationDetailsKimLongSchema, quotationDetailsKimLongSchema } from 'app/validation/quotation-details.validation';
import useContracts from 'app/hooks/use-contracts';

const { usePostQuotationDetails, useGetQuotationDetailsById, usePatchQuotationDetails } = useQuotationDetails;
const { useGetContractMaterials } = useContracts;

interface IPriceListFormProps {
  type: 'create' | 'update';
  toggleSuccess?: () => void;
}

const PriceListFormKimLongDetail = (props: IPriceListFormProps): ReactElement => {
  const { type, toggleSuccess } = props;

  const { id, detailId } = useParams();
  const account = useAppSelector(state => state.authentication.account);

  const { control, handleSubmit, setValue } = useForm<QuotationDetailsKimLongSchema>({
    resolver: zodResolver(quotationDetailsKimLongSchema),
  });

  const { data: contractMaterials, isLoading: loadingMaterial } = useGetContractMaterials();
  const { data } = useGetQuotationDetailsById(detailId);
  const { mutate: create } = usePostQuotationDetails(toggleSuccess);
  const { mutate: update } = usePatchQuotationDetails(detailId, toggleSuccess);

  // const onSubmit: SubmitHandler<QuotationDetailsKimLongSchema> = data => {
  //   const submitValues: IQuotationDetail = {
  //     id: detailId,
  //     deliveryLocation: data.deliveryLocation,
  //     deliveryLocationEn: data.deliveryLocationEn,
  //     deliveryDate: data.deliveryDate?.toDate()?.toISOString(),
  //     packaging: data.packaging,
  //     packagingEn: data.packagingEn,
  //     minimumWeight: data.minimumWeight,
  //     weight: data.weight,
  //     nitrogen180Price: data.nitrogen180Price,
  //     nitrogen150Price: data.nitrogen150Price,
  //     priceType: data.priceType,
  //     priceTypeEn: data.priceTypeEn,
  //     paymentMethod: data.paymentMethod,
  //     paymentMethodEn: data.paymentMethodEn,
  //     materialId: data.materialId,
  //     note: data.note,
  //     quotationId: id,
  //   };
  //   if (type === 'update') {
  //     update(submitValues);
  //     return;
  //   }
  //
  //   create(submitValues);
  // };
  //
  // useEffect((): void => {
  //   if (data) {
  //     setValue('materialId', data.materialId);
  //     setValue('note', data.note);
  //     setValue('nitrogen180Price', data.nitrogen180Price);
  //     setValue('nitrogen150Price', data.nitrogen150Price);
  //     setValue('weight', data.weight);
  //     setValue('priceType', data.priceType);
  //     setValue('priceTypeEn', data.priceTypeEn);
  //     setValue('deliveryLocation', data.deliveryLocation);
  //     setValue('deliveryLocationEn', data.deliveryLocationEn);
  //     setValue('packaging', data.packaging);
  //     setValue('packagingEn', data.packagingEn);
  //     setValue('deliveryDate', new DateObject(data.deliveryDate).add(7, 'hours'));
  //     setValue('minimumWeight', data.minimumWeight);
  //     setValue('paymentMethod', data.paymentMethod);
  //     setValue('paymentMethodEn', data.paymentMethodEn);
  //   }
  // }, [data]);

  const onSubmit: SubmitHandler<QuotationDetailsKimLongSchema> = data => {};

  return (
    <Form id={FORM.PRICE_LIST} onSubmit={handleSubmit(onSubmit)}>
      <></>
      {/* <Flex direction="column" gap={24}> */}
      {/*   <Row> */}
      {/*     <Col md={12}> */}
      {/*       <FormSelect */}
      {/*         control={control} */}
      {/*         id="materialId" */}
      {/*         name="materialId" */}
      {/*         label="Loại hàng" */}
      {/*         placeholder="Chọn loại hàng" */}
      {/*         options={contractMaterials?.data?.map(c => ({ */}
      {/*           label: c?.name, */}
      {/*           value: c?.id, */}
      {/*         }))} */}
      {/*         isLoading={loadingMaterial} */}
      {/*       /> */}
      {/*     </Col> */}
      {/*   </Row> */}
      {/**/}
      {/*   <Row> */}
      {/*     <Col md={12}> */}
      {/*       <FormInput control={control} id="note" name="note" label="Thông tin sản phẩm" type="textarea" rows={5} /> */}
      {/*     </Col> */}
      {/*   </Row> */}
      {/**/}
      {/*   <Row> */}
      {/*     <Col md={12}> */}
      {/*       <Label className="fw-bold">Đơn giá (Vnđ/kg) - TVN:</Label> */}
      {/*     </Col> */}
      {/**/}
      {/*     <Col md={6}> */}
      {/*       <FormInput control={control} id="nitrogen180Price" name="nitrogen180Price" label="180 mgN/100g" /> */}
      {/*     </Col> */}
      {/**/}
      {/*     <Col md={6}> */}
      {/*       <FormInput control={control} id="nitrogen150Price" name="nitrogen150Price" label="150 mgN/100g" /> */}
      {/*     </Col> */}
      {/**/}
      {/*     <Col md={6}> */}
      {/*       <FormInput control={control} id="weight" name="weight" label="Khối lượng (Kg)" /> */}
      {/*     </Col> */}
      {/**/}
      {/*     <Col md={6} /> */}
      {/**/}
      {/*     <Col md={6}> */}
      {/*       <FormInput control={control} id="priceType" name="priceType" label="Hình thức giá" /> */}
      {/*     </Col> */}
      {/**/}
      {/*     <Col md={6}> */}
      {/*       <FormInput control={control} id="priceTypeEn" name="priceTypeEn" label="Hình thức giá (En)" /> */}
      {/*     </Col> */}
      {/*   </Row> */}
      {/**/}
      {/*   <Row> */}
      {/*     <Col md={6}> */}
      {/*       <FormInput control={control} id="deliveryLocation" name="deliveryLocation" label="Địa điểm giao hàng" /> */}
      {/*     </Col> */}
      {/**/}
      {/*     <Col md={6}> */}
      {/*       <FormInput control={control} id="deliveryLocationEn" name="deliveryLocationEn" label="Địa điểm giao hàng (En)" /> */}
      {/*     </Col> */}
      {/**/}
      {/*     <Col md={6}> */}
      {/*       <FormInput control={control} id="packaging" name="packaging" label="Đóng gói" /> */}
      {/*     </Col> */}
      {/**/}
      {/*     <Col md={6}> */}
      {/*       <FormInput control={control} id="packagingEn" name="packagingEn" label="Đóng gói (En)" /> */}
      {/*     </Col> */}
      {/**/}
      {/*     <Col md={6}> */}
      {/*       <FormDatePicker control={control} id="deliveryDate" name="deliveryDate" label="Thời gian giao hàng" /> */}
      {/*     </Col> */}
      {/**/}
      {/*     <Col md={6}> */}
      {/*       <FormInput control={control} id="minimumWeight" name="minimumWeight" label="Khối lượng giao hàng tối thiểu" /> */}
      {/*     </Col> */}
      {/**/}
      {/*     <Col md={6}> */}
      {/*       <FormInput control={control} id="paymentMethod" name="paymentMethod" label="Phương thức thanh toán" /> */}
      {/*     </Col> */}
      {/**/}
      {/*     <Col md={6}> */}
      {/*       <FormInput control={control} id="paymentMethodEn" name="paymentMethodEn" label="Phương thức thanh toán (En)" /> */}
      {/*     </Col> */}
      {/*   </Row> */}
      {/* </Flex> */}
    </Form>
  );
};

export default PriceListFormKimLongDetail;
