import Form from 'app/components/form/form';
import React, { useEffect } from 'react';
import { FormProvider, SubmitHandler, useForm } from 'react-hook-form';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { quotationMMSSchema, QuotationMMSSchema } from 'app/validation/quotation.validation';
import { zodResolver } from '@hookform/resolvers/zod';
import useQuotations from 'app/hooks/use-quotations';
import Flex from 'app/components/flex/flex';
import { IQuotationCreate } from 'app/shared/model/quotation.model';
import PriceListCommonInforFields from './price-list-common-infor-fields';
import PriceListMMSInfoTable from './price-list-mms-infor-table';
import { v4 } from 'uuid';
import { useParams } from 'react-router';
import { DateObject } from 'react-multi-date-picker';
import PriceListExtraCommonMMS from './price-list-extra-common-mms';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';

const { usePostQuotation, usePatchQuotation, useGetQuotationById } = useQuotations;

interface IPriceListMMSFormProps {
  type?: 'create' | 'update';
  toggleSuccess?: () => void;
}

const PriceListMMSForm = (props: IPriceListMMSFormProps) => {
  const { type, toggleSuccess } = props;

  const { id } = useParams();

  const methods = useForm<QuotationMMSSchema>({
    resolver: zodResolver(quotationMMSSchema),
    defaultValues: {
      quotationDetails: [
        {
          id: v4(),
        },
      ],
    },
  });
  const { handleSubmit, setValue } = methods;

  const { data: detail } = useGetQuotationById(id);
  const { mutate: create } = usePostQuotation(toggleSuccess);
  const { mutate: update } = usePatchQuotation(id, toggleSuccess);

  const onSubmit: SubmitHandler<QuotationMMSSchema> = data => {
    const submitValues: IQuotationCreate = {
      name: data.name,
      description: '',
      packaging: data?.packaging,
      packagingEn: data?.packagingEn,
      paymentMethod: data?.paymentMethod,
      paymentMethodEn: data?.paymentMethodEn,
      materialCriteria: data?.materialCriteria,
      materialCriteriaEn: data?.materialCriteriaEn,
      quotationDetails: data?.quotationDetails?.map((item, index) => ({
        materialId: item?.materialId,
        weight: item?.weight,
        price: item?.price,
        note: item?.note,
        index,
        deliveryDate: item?.deliveryDate?.toDate()?.toISOString(),
        deliveryLocation: item?.deliveryLocation,
        deliveryLocationEn: item?.deliveryLocationEn,
      })),
      customerId: data?.customerId,
    };

    if (type === 'update') {
      update(submitValues);
      return;
    }

    create(submitValues);
  };

  useEffect(() => {
    if (detail) {
      setValue('name', detail?.name);
      setValue('packaging', detail?.packaging);
      setValue('packagingEn', detail?.packagingEn || '');
      setValue('paymentMethod', detail?.paymentMethod);
      setValue('paymentMethodEn', detail?.paymentMethodEn || '');
      setValue('materialCriteria', detail?.materialCriteria);
      setValue('materialCriteriaEn', detail?.materialCriteriaEn || '');
      detail?.quotationDetails?.forEach((item, index) => {
        setValue(`quotationDetails.${index}.materialId`, item?.materialId);
        setValue(`quotationDetails.${index}.weight`, formatDecimalPrecision(item?.weight));
        setValue(`quotationDetails.${index}.price`, formatDecimalPrecision(item?.price));
        setValue(`quotationDetails.${index}.note`, item?.note);
        setValue(`quotationDetails.${index}.deliveryDate`, new DateObject(item?.deliveryDate));
        setValue(`quotationDetails.${index}.deliveryLocation`, item?.deliveryLocation);
        setValue(`quotationDetails.${index}.deliveryLocationEn`, item?.deliveryLocationEn);
      });

      setValue('customerId', detail?.customerId);
    }
  }, [detail]);

  return (
    <FormProvider {...methods}>
      <Form id={FORM.PRICE_LIST} onSubmit={handleSubmit(onSubmit)}>
        <Flex direction="column">
          <PriceListCommonInforFields />
          <PriceListExtraCommonMMS />
          <PriceListMMSInfoTable />
        </Flex>
      </Form>
    </FormProvider>
  );
};

export default PriceListMMSForm;
