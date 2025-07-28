import { zodResolver } from '@hookform/resolvers/zod';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import { useModalsProductionQuality } from 'app/hooks/use-modals-production-quality';
import useProductionQualityControl from 'app/hooks/use-production-quality-control';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { PRODUCTION_QUALITY_STATUS } from 'app/shared/model/enumerations/production-quality-control.model';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import {
  ProductionQualityFormSchema,
  productionQualitySchema,
} from 'app/validation/production-quality.validation';
import dayjs from 'dayjs';
import { useEffect, useState } from 'react';
import { FormProvider, useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { useNavigate, useParams } from 'react-router';
import QualityApproveModals from '../modals/quality-approve-modals';
import QualityApproveSuccessModals from '../modals/quality-approve-success-modals';
import QualityCancelModals from '../modals/quality-cancel-modals';
import QualityCancelSuccessModals from '../modals/quality-cancel-success-modals';
import QualityCreateSuccessModals from '../modals/quality-create-success-modals';
import QualityRejectModals from '../modals/quality-reject-modals';
import QualityRejectSuccessModals from '../modals/quality-reject-success-modals';
import QualityUpdateSuccessModals from '../quality-update-success-modal';
import { ButtonGroupHeader } from './button-group-header';
import CancelTestingTemplate from './cancel-testing-template';
import ProductionQualityForm from './production-quality-create-form';
import { MANUFACTURE_ORDER_STATUS } from 'app/shared/model/enumerations/production-command.model';
import { handleMergeTime } from 'app/shared/util/date-utils';

const {
  useGetQualityCheckSampleById,
  usePostQualityCheckSample,
  usePatchQualityCheckSample,
} = useProductionQualityControl;

const ProductionQualityCreate = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [
    { isOpenFilter, toggleFilter },
    { isOpenCreateSuccess, toggleCreateSuccess },
    { isOpenUpdateSuccess, toggleUpdateSuccess },
    { isOpenApprove, toggleApprove },
    { isOpenApproveSuccess, toggleApproveSuccess },
    { isOpenRejectCancel, toggleRejectCancel },
    { isOpenRejectCancelSuccess, toggleRejectCancelSuccess },
    { isOpenCancel, toggleCancel },
    { isOpenCancelSuccess, toggleCancelSuccess },
    { isOpenDelete, toggleDelete },
    { isOpenDeleteSuccess, toggleDeleteSuccess },
    { isOpenPass, toggleModalPass },
    { isOpenPassSuccess, toggleModalPassSuccess },
    { selectedRecord, setSelectedRecord },
  ] = useModalsProductionQuality();

  const [openModal, setOpenModal] = useState<boolean>(false);

  const methods = useForm<ProductionQualityFormSchema>({
    resolver: zodResolver(productionQualitySchema),
  });
  const { handleSubmit, setValue, watch, setError } = methods;

  const statusWatch = watch('status');
  const statusMWatch = watch('statusM');

  const toggle = () => {
    if (id) toggleUpdateSuccess();
    else toggleCreateSuccess();
  };

  const { data: detail, refetch } = useGetQualityCheckSampleById(id);

  const { mutate: create } = usePostQualityCheckSample(toggle);
  const { mutate: update } = usePatchQualityCheckSample(id, toggle);

  const toggleError = error => {
    const errorCode = error.response.data?.message;
    if (errorCode === 'error.CODE_EXISTS') {
      setError('sampleNo', { message: 'Mã mẫu đã tồn tại' });
    }
  };

  const onSubmit = (data: ProductionQualityFormSchema) => {
    const submitValues = {
      samplingDate: handleMergeTime(dayjs(data.samplingDate.toDate()))
        .toDate()
        .toISOString(),
      sampleNo: data.sampleNo,
      productType: data.productType,
      sampleWeight: Number(
        data.sampleWeight?.replace(/,/g, '').replace(/\./g, ''),
      ),
      customer: data.customer,
      reason: data.reason,
      sampleReleaseDate: data.sampleReleaseDate?.toDate().toISOString(),
      internalHum: data.internalHum,
      internalTvn: data.internalTvn,
      internalAsh: data.internalAsh,
      internalProtein: data.internalProtein,
      externalHum: data.externalHum,
      externalTvn: data.externalTvn,
      externalAsh: data.externalAsh,
      externalProtein: data.externalProtein,
      samplingEmployeeId: data.samplingEmployeeId,
      packageId: data.packageId,
      manufactureOrderId: data.manufactureOrderId,
      itemId: '00000000-0000-0000-0000-000000000000',
      proteinPercentageApply: Number(data.proteinPercentageApply ?? 0),
      attributes: data.attributes,
    };

    if (id) update(submitValues);
    else create(submitValues, { onError: toggleError });
  };

  useEffect(() => {
    if (detail) {
      const { data } = detail;
      setValue(
        'samplingDate',
        new DateObject(data?.zonedSamplingDate).add(7, 'hours'),
      );
      setValue('sampleNo', data?.sampleNo);
      setValue('productType', data?.productType);
      setValue(
        'sampleWeight',
        formatDecimalPrecision(data?.sampleWeight?.toString()),
      );
      setValue('customer', data?.customer);
      setValue('samplingEmployeeId', data?.samplingEmployeeId);
      setValue('internalHum', data?.internalHum);
      setValue('internalTvn', data?.internalTvn);
      setValue('internalAsh', data?.internalAsh);
      setValue('internalProtein', data?.internalProtein);
      setValue('externalHum', data?.externalHum);
      setValue('externalTvn', data?.externalTvn);
      setValue('externalAsh', data?.externalAsh);
      setValue('externalProtein', data?.externalProtein);
      setValue('reason', data?.reason);
      setValue(
        'sampleReleaseDate',
        new DateObject(data?.zonedSampleReleaseDate).add(7, 'hours'),
      );
      setValue('packageId', data?.packageId);
      setValue('manufactureOrderId', data?.manufactureOrderId);
      setValue('itemId', data?.itemId);
      setValue(
        'proteinPercentageApply',
        data?.proteinPercentageApply?.toString(),
      );

      setValue('status', data?.status);
      setValue('statusM', data?.manufactureOrder?.status);

      setValue('attributes', data?.attributes);
    }
  }, [detail]);

  return (
    <>
      <FormProvider {...methods}>
        <Form<ProductionQualityFormSchema>
          id={FORM.TESTING_TEMPLATE}
          onSubmit={handleSubmit(onSubmit)}
        >
          <CardV2
            header={
              <Flex justify="space-between" align="center">
                <Typography level={4}>
                  Quản lý kiểm tra chất lượng (QC)
                </Typography>

                <ButtonGroupHeader
                  status={statusWatch}
                  toggleReject={toggleRejectCancel}
                  toggleCancel={toggleCancel}
                  toggleSendApprove={toggleApprove}
                  setSelectedRecord={setSelectedRecord}
                  toggleCancelTesting={() => setOpenModal(true)}
                  detail={detail?.data}
                  statusM={statusMWatch}
                />
              </Flex>
            }
          >
            <ProductionQualityForm />
          </CardV2>
        </Form>
      </FormProvider>

      <QualityCreateSuccessModals
        isOpen={isOpenCreateSuccess}
        toggle={() => {
          toggleCreateSuccess();
          navigate(PATH.PRODUCTION_QUALITY);
        }}
      />

      <QualityUpdateSuccessModals
        isOpen={isOpenUpdateSuccess}
        toggle={() => {
          toggleUpdateSuccess();
          navigate(PATH.PRODUCTION_QUALITY);
        }}
      />

      <QualityRejectModals
        isOpen={isOpenRejectCancel}
        toggle={toggleRejectCancel}
        toggleSuccess={toggleRejectCancelSuccess}
        selectedRecord={selectedRecord}
      />

      <QualityRejectSuccessModals
        isOpen={isOpenRejectCancelSuccess}
        toggle={toggleRejectCancelSuccess}
      />

      <QualityCancelModals
        isOpen={isOpenCancel}
        toggle={toggleCancel}
        toggleSuccess={() => {
          toggleCancelSuccess();
          navigate(PATH.PRODUCTION_QUALITY);
        }}
        selectedRecord={selectedRecord}
      />

      <QualityCancelSuccessModals
        isOpen={isOpenCancelSuccess}
        toggle={toggleCancelSuccess}
      />

      <QualityApproveModals
        isOpen={isOpenApprove}
        toggle={toggleApprove}
        toggleSuccess={toggleApproveSuccess}
        selectedRecord={selectedRecord}
      />

      <QualityApproveSuccessModals
        isOpen={isOpenApproveSuccess}
        toggle={toggleApproveSuccess}
      />

      <CancelTestingTemplate
        isOpen={openModal}
        toggle={() => setOpenModal(false)}
        toggleSuccess={() => {
          setOpenModal(false);
          refetch();
        }}
        detail={{
          quantitySampleNo: watch('sampleNo'),
          quantityReleaseDate: watch('samplingDate')
            ? dayjs(watch('samplingDate')?.toDate()).toISOString()
            : null,
          ...detail?.data?.disposal,
        }}
        selectRecord={selectedRecord}
        status={watch('status') as PRODUCTION_QUALITY_STATUS}
        statusM={watch('statusM') as MANUFACTURE_ORDER_STATUS}
      />
    </>
  );
};

export default ProductionQualityCreate;
