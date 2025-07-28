import Form from 'app/components/form/form';
import React, { useEffect } from 'react';
import { FormProvider, SubmitHandler, useForm } from 'react-hook-form';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { QuotationFormKimLongSchema, quotationKimLongSchema } from 'app/validation/quotation.validation';
import useQuotations from 'app/hooks/use-quotations';
import Flex from 'app/components/flex/flex';
import { IQuotationCreate } from 'app/shared/model/quotation.model';
import PriceListKimLongInfoTable from './price-list-kim-long-infor-table';
import PriceListCommonInforFields from './price-list-common-infor-fields';
import { zodResolver } from '@hookform/resolvers/zod';
import { v4 } from 'uuid';
import { useParams } from 'react-router';
import PriceListExtraCommonKimLong from './price-list-extra-common-kim-long';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';

const { usePostQuotation, usePatchQuotation, useGetQuotationById } = useQuotations;

interface IPriceListFormProps {
  type?: 'create' | 'update';
  toggleSuccess?: () => void;
}

const PriceListFormKimLong = (props: IPriceListFormProps) => {
  const { type, toggleSuccess } = props;

  const { id } = useParams();

  const methods = useForm<QuotationFormKimLongSchema>({
    resolver: zodResolver(quotationKimLongSchema),
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

  const onSubmit: SubmitHandler<QuotationFormKimLongSchema> = data => {
    const submitValues: IQuotationCreate = {
      name: data?.name,
      description: '',
      // deliveryDate: data?.deliveryDate?.toDate()?.toISOString(),
      deliveryLocation: data?.deliveryLocation,
      deliveryLocationEn: data?.deliveryLocationEn,
      // minimumWeight: data?.minimumWeight,
      packaging: data?.packaging,
      packagingEn: data?.packagingEn,
      paymentMethod: data?.paymentMethod,
      paymentMethodEn: data?.paymentMethodEn,
      // priceType: data?.priceType,
      // priceTypeEn: data?.priceTypeEn,
      quotationDetails: data?.quotationDetails?.map((item, index) => ({
        materialId: item?.materialId,
        weight: item?.weight?.replace(/,/g, '').replace(/\./g, ''),
        price: item?.price?.replace(/,/g, '').replace(/\./g, ''),
        // nitrogen180Price: item?.nitrogen180Price,
        // nitrogen150Price: item?.nitrogen150Price,
        note: item?.note,
        index,
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
      // setValue('deliveryDate', new DateObject(detail?.deliveryDate).add(7, 'hours'));
      setValue('deliveryLocation', detail?.deliveryLocation);
      setValue('deliveryLocationEn', detail?.deliveryLocationEn || '');
      // setValue('minimumWeight', detail?.minimumWeight);
      setValue('packaging', detail?.packaging);
      setValue('packagingEn', detail?.packagingEn || '');
      setValue('paymentMethod', detail?.paymentMethod);
      setValue('paymentMethodEn', detail?.paymentMethodEn || '');

      setValue('quotationDetails', detail?.quotationDetails?.map(item => ({
        ...item,
        price: formatDecimalPrecision(item.price),
        weight: formatDecimalPrecision(item.weight)
      })));
      // setValue('priceType', detail?.priceType);
      // setValue('priceTypeEn', detail?.priceTypeEn || '');
      setValue('customerId', detail?.customerId);
    }
  }, [detail]);

  return (
    <FormProvider {...methods}>
      <Form id={FORM.PRICE_LIST} onSubmit={handleSubmit(onSubmit)}>
        <Flex direction="column">
          <PriceListCommonInforFields />
          <PriceListExtraCommonKimLong />
          <PriceListKimLongInfoTable />
        </Flex>
      </Form>
    </FormProvider>
  );
};

export default PriceListFormKimLong;
