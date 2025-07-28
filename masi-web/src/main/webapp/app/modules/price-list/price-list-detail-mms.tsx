import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router';

import Card from 'app/components/card/card';
import Flex from 'app/components/flex/flex';
import useQuotations from 'app/hooks/use-quotations';
import PriceListDetailHeader from './price-list-detail-header';
import PriceListDetailTableMMS from './price-list-detail-table-mms';
import PriceListDetailTableApproval from './price-list-detail-table-approval';
import { useAppDispatch } from 'app/config/store';
import { Typography } from 'app/components/typography/typography';
import { DownloadSuccessfulModals } from 'app/components/modals-download-successful/download-successful-modals';
import { FormProvider, SubmitHandler, useForm } from 'react-hook-form';
import Form from 'app/components/form/form';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { QuotationDetailsMMSSchema, quotationDetailsMMSSchema } from 'app/validation/quotation-details.validation';
import { setIsUpdate } from 'app/shared/reducers/quotation';
import { zodResolver } from '@hookform/resolvers/zod';
import PriceListUpdateSuccessModals from './modals/price-list-update-success-modals';
import PriceListDetailTableCommonMMS from './price-list-detail-table-common-mms';

const { useGetQuotationById, usePatchQuotation } = useQuotations;

const PriceListDetail = () => {
  const [openDownloadSuccessful, setOpenDownloadSuccessful] = useState<boolean>(false);
  const [openDeleteSuccess, setToggleDeleteSuccess] = useState<boolean>(false);

  const { id } = useParams();
  const dispatch = useAppDispatch();
  const methods = useForm({
    resolver: zodResolver(quotationDetailsMMSSchema),
  });

  const handleShowModal = (): void => setToggleDeleteSuccess(prev => !prev);

  const onSuccess = () => {
    dispatch(setIsUpdate(false));
    handleShowModal();
  };

  const { data } = useGetQuotationById(id);
  const { mutate } = usePatchQuotation(id, onSuccess);

  const toggleDownloadSuccessful = () => {
    setOpenDownloadSuccessful(prev => !prev);
  };

  useEffect(() => {
    dispatch(setIsUpdate(false));
  }, []);

  const onSubmit: SubmitHandler<QuotationDetailsMMSSchema> = values => {
    mutate({
      name: data?.name,
      description: data?.description,
      quotationDetails: values.quotationDetails?.map(item => ({
        ...data?.quotationDetails?.[0],
        materialId: item.materialId,
        weight: item.weight,
        note: item?.note,
        price: item?.price,
        materialCriteria: item?.materialCriteria,
        materialCriteriaEn: item?.materialCriteriaEn,
      })),
    });
  };

  return (
    <div className='page_container'>
      <Typography level={4}>Chi tiết bảng báo giá</Typography>

      <Flex direction="column" gap={16}>
        <Card header={<PriceListDetailHeader name={data?.name} toggleDownloadSuccessful={toggleDownloadSuccessful} />}>
          <PriceListDetailTableCommonMMS data={data} />
        </Card>

        <Flex direction="column" className="mt-8">
          <Typography level={5}>Chi tiết sản phẩm</Typography>
          <FormProvider {...methods}>
            <Form id={FORM.PRICE_LIST} onSubmit={methods.handleSubmit(onSubmit)}>
              <Card >
                <PriceListDetailTableMMS data={data} />
              </Card>
            </Form>
          </FormProvider>
        </Flex>

        <Flex direction="column" className="mt-8">
          <Typography level={5}>Chi tiết xét duyệt</Typography>
          <Card >
            <PriceListDetailTableApproval data={data} />
          </Card>
        </Flex>
      </Flex>
      <DownloadSuccessfulModals title="bảng báo giá" isOpen={openDownloadSuccessful} toggleSuccess={toggleDownloadSuccessful} />
      <PriceListUpdateSuccessModals isOpen={openDeleteSuccess} toggle={handleShowModal} />
    </div>
  );
};

export default PriceListDetail;
