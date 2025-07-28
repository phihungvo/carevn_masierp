import { zodResolver } from '@hookform/resolvers/zod';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import AuthGuard from 'app/components/guards/auth-guard';
import { Typography } from 'app/components/typography/typography';
import { useAppSelector } from 'app/config/store';
import {
  DEFAULT_INTEGER_REGEX,
  DEFAULT_PAGE_SIZE_NAX,
} from 'app/constants/common';
import useCustomers from 'app/hooks/use-customers';
import useEmployee from 'app/hooks/use-employee';
import useItems from 'app/hooks/use-items';
import useProductionCommand from 'app/hooks/use-production-command';
import CancelTestingTemplate from 'app/modules/production-quality/components/cancel-testing-template';
import QualityApproveSignModals from 'app/modules/production-quality/modals/quality-approve-modals';
import QualityApproveSuccessModals from 'app/modules/production-quality/modals/quality-approve-success-modals';
import QualityCancelModals from 'app/modules/production-quality/modals/quality-cancel-modals';
import QualityCancelSuccessModals from 'app/modules/production-quality/modals/quality-cancel-success-modals';
import QualityRejectModals from 'app/modules/production-quality/modals/quality-reject-modals';
import QualityRejectSuccessModals from 'app/modules/production-quality/modals/quality-reject-success-modals';
import { FORM } from 'app/shared/model/enumerations/form.model';
import {
  MANUFACTURE_ORDER_STATUS,
  MANUFACTURE_ORDER_TYPE,
} from 'app/shared/model/enumerations/production-command.model';
import { PRODUCTION_QUALITY_STATUS } from 'app/shared/model/enumerations/production-quality-control.model';
import { ISampleDisposal } from 'app/shared/model/production-quality-control.model';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import { handleValidatePaste } from 'app/shared/util/handle-valid-decimal';
import {
  ManufactureOrderSchema,
  qualityCheckSampleSchema,
  QualityCheckSampleSchema,
} from 'app/validation/manufacture-order.validation';
import dayjs from 'dayjs';
import { useEffect, useState } from 'react';
import { FormProvider, useForm, useFormContext } from 'react-hook-form';
import { useParams } from 'react-router';
import { Col, Row } from 'reactstrap';

const { useGetEmployeesQuery } = useEmployee;
const { useGetEnabledCustomers } = useCustomers;
const { useGetItemsPercentProteinQuery } = useItems;
const { useGetProductionCommandById } = useProductionCommand;

const styleApprove = {
  color: '#475467',
  borderColor: '#98A2B3',
  fontWeight: '600',
};

export const TabQualityControl = ({
  onSubmit,
  handleCompleteWithoutCall,
  type,
}: {
  onSubmit: (values, complete) => void;
  handleCompleteWithoutCall: (res: boolean) => void;
  type?: string;
}) => {
  const { id } = useParams();

  const [openModal, setOpenModal] = useState<boolean>(false);
  const [openCancelModal, setOpenCancelModal] = useState<boolean>(false);
  const [openCancelSuccessModal, setOpenCancelSuccessModal] =
    useState<boolean>(false);
  const [openRejectModal, setOpenRejectModal] = useState<boolean>(false);
  const [openRejectSuccessModal, setOpenRejectSuccessModal] =
    useState<boolean>(false);
  const [openApproveModal, setOpenApproveModal] = useState<boolean>(false);
  const [openApproveSuccessModal, setOpenApproveSuccessModal] =
    useState<boolean>(false);

  const [isDone, setIsDone] = useState<boolean>(false);
  const [completeState, setCompleteState] = useState<boolean>(false);

  const account = useAppSelector(state => state.authentication.account);

  const methods = useForm<QualityCheckSampleSchema>({
    resolver: zodResolver(qualityCheckSampleSchema),
  });

  const { control, setValue, formState, handleSubmit, watch, setError } =
    methods;

  const statusWatch = watch('status');
  const disposalWatch = watch('disposal');
  const disposalId = watch('disposal.id');
  const idWatch = watch('id');

  const methodPrimary = useFormContext<ManufactureOrderSchema>();
  const { watch: watchPrimary } = methodPrimary;

  const qualityControlWatch = watchPrimary('productionQuality');
  const statusM = watchPrimary('status');

  const { data: employees, isLoading } = useGetEmployeesQuery();
  const { data: customers, isLoading: cusLoading } = useGetEnabledCustomers();
  // const { data: depreciations } = useGetItemsPercentProteinQuery({
  //   size: DEFAULT_PAGE_SIZE_NAX,
  // });

  const { data: _detailOrder, refetch } = useGetProductionCommandById(id);

  useEffect(() => {
    if (qualityControlWatch) {
      setValue('id', qualityControlWatch?.id);
      setValue('samplingDate', qualityControlWatch?.samplingDate);
      setValue('sampleNo', qualityControlWatch?.sampleNo);
      setValue('productType', qualityControlWatch?.productType);
      setValue('sampleWeight', qualityControlWatch?.sampleWeight);
      setValue('customer', qualityControlWatch?.customer);
      setValue('reason', qualityControlWatch?.reason);
      setValue('sampleReleaseDate', qualityControlWatch?.sampleReleaseDate);
      setValue('internalHum', qualityControlWatch?.internalHum);
      setValue('internalTvn', qualityControlWatch?.internalTvn);
      setValue('internalAsh', qualityControlWatch?.internalAsh);
      setValue('internalProtein', qualityControlWatch?.internalProtein);
      setValue('externalHum', qualityControlWatch?.externalHum);
      setValue('externalTvn', qualityControlWatch?.externalTvn);
      setValue('externalAsh', qualityControlWatch?.externalAsh);
      setValue('externalProtein', qualityControlWatch?.externalProtein);
      setValue('samplingEmployeeId', qualityControlWatch?.samplingEmployeeId);
      setValue('note', qualityControlWatch?.note);
      setValue('itemId', qualityControlWatch?.itemId);
      setValue(
        'proteinPercentageApply',
        qualityControlWatch?.proteinPercentageApply,
      );
      setValue('disposal', qualityControlWatch?.disposal);
      setValue('status', qualityControlWatch?.status);

      setValue('attributes', qualityControlWatch?.attributes);

      setIsDone(qualityControlWatch?.attributes?.isDone);
    }
  }, [qualityControlWatch]);

  useEffect(() => {
    if (completeState) {
      setCompleteState(false);
      handleCompleteWithoutCall(false);
      setValue('attributes.isDone', false);
    }
  }, [watch()]);

  const disabled =
    isDone || statusM === (MANUFACTURE_ORDER_STATUS.CANCELLED as string);

  const disabledRejected =
    statusWatch === (PRODUCTION_QUALITY_STATUS.DISPOSED as string) ||
    statusWatch === (PRODUCTION_QUALITY_STATUS.REJECTED as string) ||
    statusM === (MANUFACTURE_ORDER_STATUS.CANCELLED as string);

  const disabledApprove =
    statusWatch === (PRODUCTION_QUALITY_STATUS.DISPOSED as string) ||
    statusWatch === (PRODUCTION_QUALITY_STATUS.REJECTED as string) ||
    statusM === (MANUFACTURE_ORDER_STATUS.CANCELLED as string);

  const disabledConfirmReject =
    statusWatch === (PRODUCTION_QUALITY_STATUS.REJECTED as string) ||
    statusM === (MANUFACTURE_ORDER_STATUS.CANCELLED as string);

  return (
    <>
      <FormProvider {...methods}>
        <Form
          id={FORM.MANUFACTURE_ORDER_QUALITY_CHECK_SAMPLE}
          onSubmit={handleSubmit(values => onSubmit(values, completeState))}
        >
          <Flex direction="column" gap={16}>
            <Typography level="paragraph" className="bold test-check-heading">
              Thông tin chung:
            </Typography>

            <Row>
              <Col md={4}>
                <FormInputV2
                  control={control}
                  name="sampleNo"
                  label="Mã mẫu"
                  disabled={disabled}
                />
              </Col>

              <Col md={4}>
                <FormDatePickerV2
                  formState={formState}
                  setValue={setValue}
                  control={control}
                  label="Ngày lấy mẫu"
                  name="samplingDate"
                  disabled={disabled}
                />
              </Col>

              <Col md={4}>
                <FormInputV2
                  control={control}
                  name="productType"
                  label="Loại hàng"
                  disabled={disabled}
                />
              </Col>

              <Col md={4}>
                <FormInputV2
                  control={control}
                  name="sampleWeight"
                  label="Số lượng mẫu/ khối lượng"
                  onChange={e =>
                    setValue(
                      'sampleWeight',
                      formatDecimalPrecision(
                        Number(e?.target?.value?.toString()?.replace(/,/g, '')),
                      ),
                    )
                  }
                  onPaste={e => handleValidatePaste(e, DEFAULT_INTEGER_REGEX)}
                  disabled={disabled}
                />
              </Col>

              <Col md={4}>
                <FormSelect
                  control={control}
                  id="customer"
                  name="customer"
                  placeholder="Chọn khách hàng"
                  label="Khách hàng"
                  options={customers?.data?.map(c => ({
                    label: c?.companyName,
                    value: c?.id,
                  }))}
                  isLoading={cusLoading}
                  disabled={disabled}
                />
              </Col>

              <Col md={4}>
                <FormSelect
                  control={control}
                  id="samplingEmployeeId"
                  name="samplingEmployeeId"
                  placeholder="Chọn người lưu mẫu"
                  label="Người lưu mẫu"
                  options={employees?.data?.map(e => ({
                    label: `${e?.code || ''} - ${
                      e?.employeeProfile.fullName || ''
                    }`,
                    value: e?.id,
                  }))}
                  isLoading={isLoading}
                  disabled={disabled}
                />
              </Col>

              <Col md={12}>
                <FormInputV2
                  control={control}
                  name="reason"
                  label="Lý do"
                  disabled={disabled}
                />
              </Col>
            </Row>

            <Flex style={{ border: '1px solid #E4E7EC', borderRadius: '6px' }}>
              <Row style={{ margin: '16px', width: '100%' }}>
                <Col md={8} style={{ borderRight: '1px solid #d3d5d7' }}>
                  <Flex direction="column" gap={16}>
                    <Typography
                      level="paragraph"
                      className="bold test-check-heading"
                    >
                      Nội bộ:
                    </Typography>

                    <Row>
                      <Col md={6}>
                        <FormInputV2
                          control={control}
                          name="internalHum"
                          label="Độ ẩm"
                          disabled={disabled}
                        />
                      </Col>
                      <Col md={6}>
                        <FormInputV2
                          control={control}
                          name="internalTvn"
                          label="TVN"
                          disabled={disabled}
                        />
                      </Col>
                      <Col md={6}>
                        <FormInputV2
                          control={control}
                          name="internalAsh"
                          label="Tro"
                          disabled={disabled}
                        />
                      </Col>
                      <Col md={6}>
                        <FormInputV2
                          control={control}
                          name="internalProtein"
                          label="Protein"
                          disabled={disabled}
                        />
                      </Col>
                    </Row>
                  </Flex>
                  <Flex direction="column" gap={16}>
                    <Typography
                      level="paragraph"
                      className="bold test-check-heading"
                    >
                      Đối tác:
                    </Typography>

                    <Row>
                      <Col md={6}>
                        <FormInputV2
                          control={control}
                          name="externalHum"
                          label="Độ ẩm"
                          disabled={disabled}
                        />
                      </Col>

                      <Col md={6}>
                        <FormInputV2
                          control={control}
                          name="externalTvn"
                          label="TVN"
                          disabled={disabled}
                        />
                      </Col>
                      <Col md={6}>
                        <FormInputV2
                          control={control}
                          name="externalAsh"
                          label="Tro"
                          disabled={disabled}
                        />
                      </Col>
                      <Col md={6}>
                        <FormInputV2
                          control={control}
                          name="externalProtein"
                          label="Protein"
                          disabled={disabled}
                        />
                      </Col>
                    </Row>
                  </Flex>
                </Col>
                <Col md={4} style={{ margin: 'auto' }}>
                  {/* <FormSelect
                    control={control}
                    name="itemId"
                    label="% đạm áp dụng"
                    placeholder="Vui lòng chọn % đạm áp dụng"
                    options={depreciations?.data?.map(x => ({
                      value: x?.id,
                      label: `${x.percentProtein}`,
                    }))}
                    onChanges={e => {
                      const selected = depreciations?.data?.find(
                        x => x.id === e,
                      );
                      if (selected) {
                        setValue(
                          'proteinPercentageApply',
                          `${selected?.percentProtein ?? 0}`,
                        );
                        clearErrors('itemId');
                      }
                    }}
                    disabled={disabled}
                  /> */}

                  <FormInputV2
                    control={control}
                    name="proteinPercentageApply"
                    label="% đạm áp dụng"
                    placeholder="Vui lòng nhập % đạm áp dụng"
                    disabled={disabled}
                    onPaste={e => handleValidatePaste(e, DEFAULT_INTEGER_REGEX)}
                  />
                </Col>
              </Row>
            </Flex>
          </Flex>
          <div className="production-card__footer">
            <AuthGuard
              permissionKey={
                type == MANUFACTURE_ORDER_TYPE.MANUFACTURE_ORDER_BY_ORDER
                  ? 'PRODUCTION_MANUFACTURE_ORDER.EDIT'
                  : 'PRODUCTION_MANUFACTURE_ORDER_STANDARD.EDIT'
              }
            >
              <ButtonV2
                type="submit"
                onClick={e => {
                  if (watch('proteinPercentageApply')) {
                    setValue('attributes.isDone', true);
                    handleCompleteWithoutCall(true);
                    // setCompleteState(true);
                  } else {
                    setError('proteinPercentageApply', {
                      message: 'Vui lòng chọn % đạm áp dụng',
                    });
                    e.preventDefault();
                  }
                }}
                disabled={disabled}
                style={{ borderColor: '#027A48', color: '#027A48' }}
              >
                Hoàn thành
              </ButtonV2>
              {disposalId &&
                disposalWatch?.id &&
                disposalWatch?.reviewerId === account?.id &&
                !disabledRejected && (
                  <ButtonV2
                    style={{
                      color: '#B42318',
                      borderColor: '#FDA29B',
                      fontWeight: '600',
                    }}
                    onClick={() => setOpenRejectModal(true)}
                    disabled={disabledRejected}
                  >
                    Từ chối
                  </ButtonV2>
                )}
              {!disposalWatch?.id && (
                <ButtonV2
                  style={{ ...styleApprove }}
                  onClick={() => setOpenModal(true)}
                >
                  Tạo đơn hủy mẫu
                </ButtonV2>
              )}
              {disposalId && disposalWatch?.id && (
                <ButtonV2
                  style={{ ...styleApprove }}
                  onClick={() => setOpenModal(true)}
                >
                  Đơn hủy mẫu
                </ButtonV2>
              )}
              {disposalId &&
                disposalWatch?.id &&
                disposalWatch?.reviewerId === account?.id &&
                !disabledApprove && (
                  <ButtonV2
                    style={{ ...styleApprove }}
                    onClick={() => setOpenApproveModal(true)}
                    disabled={disabledApprove}
                  >
                    Duyệt
                  </ButtonV2>
                )}
              {disposalId &&
                disposalWatch?.id &&
                disabledApprove &&
                !disabledConfirmReject && (
                  <ButtonV2
                    style={{ ...styleApprove }}
                    onClick={() => setOpenCancelModal(true)}
                    disabled={disabledConfirmReject}
                  >
                    Xác nhận hủy
                  </ButtonV2>
                )}
              <ButtonV2
                variant="solid"
                color="blue"
                type="submit"
                disabled={disabled}
              >
                Lưu
              </ButtonV2>
            </AuthGuard>
          </div>
        </Form>
      </FormProvider>

      <CancelTestingTemplate
        isOpen={openModal}
        toggle={() => setOpenModal(false)}
        toggleSuccess={() => {
          setOpenModal(false);
          refetch();
        }}
        detail={
          {
            quantitySampleNo: watch('sampleNo'),
            quantityReleaseDate: watch('samplingDate')
              ? dayjs(watch('samplingDate')?.toDate()).toISOString()
              : null,
            ...disposalWatch,
            id: disposalId,
          } as ISampleDisposal
        }
        parentId={idWatch}
        selectRecord={disposalId}
        status={watch('status') as PRODUCTION_QUALITY_STATUS}
        statusM={watchPrimary('status') as MANUFACTURE_ORDER_STATUS}
      />

      <QualityRejectModals
        isOpen={openRejectModal}
        toggle={() => setOpenRejectModal(false)}
        toggleSuccess={() => setOpenRejectSuccessModal(true)}
        selectedRecord={disposalId}
      />

      <QualityRejectSuccessModals
        isOpen={openRejectSuccessModal}
        toggle={() => {
          setOpenRejectSuccessModal(false);
          refetch();
        }}
      />

      <QualityCancelModals
        isOpen={openCancelModal}
        toggle={() => setOpenCancelModal(false)}
        toggleSuccess={() => setOpenCancelSuccessModal(true)}
        selectedRecord={idWatch}
      />

      <QualityCancelSuccessModals
        isOpen={openCancelSuccessModal}
        toggle={() => {
          setOpenCancelSuccessModal(false);
          refetch();
        }}
      />

      <QualityApproveSignModals
        isOpen={openApproveModal}
        toggle={() => setOpenApproveModal(false)}
        toggleSuccess={() => setOpenApproveSuccessModal(true)}
        selectedRecord={disposalId}
      />

      <QualityApproveSuccessModals
        isOpen={openApproveSuccessModal}
        toggle={() => {
          setOpenApproveSuccessModal(false);
          refetch();
        }}
      />
    </>
  );
};
