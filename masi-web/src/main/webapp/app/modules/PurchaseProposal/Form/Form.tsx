import { zodResolver } from '@hookform/resolvers/zod';
import ButtonBack from 'app/components/ButtonV2/ButtonBack';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import { Typography } from 'app/components/typography/typography';
import useAccountApp from 'app/hooks/use-account-app';
import useModalRedux from 'app/hooks/use-modal-redux';
import useSuppliesRequest from 'app/hooks/use-supplies-request';
import useAction from 'app/hooks/user-action';
import Attachments from 'app/modules/request-payment/components/Attachments/Attachments';
import { SuppliesRequestStatus } from 'app/shared/model/enumerations/supplies-request';
import {
  ConvertedGoodsType,
  suppliersRequestSchemaV2,
  SuppliersRequestSchemaV2Type,
  SuppliesItemDTOSchemaType,
} from 'app/validation/supplies-request.validation';
import dayjs from 'dayjs';
import { useEffect, useState } from 'react';
import { FormProvider, SubmitHandler, useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { useParams } from 'react-router';
import { Col, Row } from 'reactstrap';
import Accept from '../Actions/Accept';
import Cancel from '../Actions/Cancel';
import Reject from '../Actions/Reject';
import RequestAccept from '../Actions/RequestAccept';
import Goods from './Goods';
import PersonSign from './PersonSign';
import Summary from './Summary';
import ButtonPrint from 'app/components/ButtonV2/ButtonPrint';
import { useSearchParams } from 'react-router-dom';
import useSupplier from 'app/hooks/use-supplier';
import FormSelect from 'app/components/form/form-select';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import AuthGuard from 'app/components/guards/auth-guard';
const { useGetSuppliers } = useSupplier;

const {
  useCreateSuppliesRequest,
  useUpdateSuppliesRequest,
  useGetSuppliesRequestById,
} = useSuppliesRequest;

type Props = {};

const FormProposal = (props: Props) => {
  // State
  const [_searchParams, setSearchParams] = useSearchParams();
  const account = useAccountApp();
  const param = useParams();
  const id_detail = param?.id;
  const isEditMode: boolean = !!id_detail;
  const { handleToggleSuccessModal, handleToggleFailModal } = useModalRedux();
  const { actions, handleToggleAction } = useAction();
  const [status, setStatus] = useState<{
    isCanRejectOrApprove: boolean;
    isCanRequestAccept: boolean;
    isCanCanceled: boolean;
  }>({
    isCanCanceled: false,
    isCanRejectOrApprove: false,
    isCanRequestAccept: false,
  });
  const { data: suppliers } = useGetSuppliers();

  // Query
  const detail_query = useGetSuppliesRequestById(id_detail);
  const createMutation = useCreateSuppliesRequest(
    () => handleToggleSuccessModal({ content: 'Tạo đề xuất thành công' }),
    () => handleToggleFailModal({ content: 'Tạo đề xuất thất bại' }),
  );
  const updateMutation = useUpdateSuppliesRequest(
    detail_query?.data?.id,
    () => handleToggleSuccessModal({ content: 'Cập nhật đề xuất thành công' }),
    () => handleToggleFailModal({ content: 'Cập nhật đề xuất thất bại' }),
  );

  // Form
  const methods = useForm<SuppliersRequestSchemaV2Type>({
    resolver: zodResolver(suppliersRequestSchemaV2),
    mode: 'onTouched',
    defaultValues: {
      createdByEmployeeId: account?.employeeId,
      requestByEmployeeId: account?.employeeId,
      requestDate: new DateObject(),
      requestApprovals: [],
      attachedFiles: [],
      suppliesItemDTO: [],
      tmp: {
        employeeName: account?.lastName + ' ' + account?.firstName,
        staffName: account?.workspace?.normalizedName,
      },
      invoiceSupplies: [],
      isDoNotSign: false,
      supplierContracts: [],
    },
  });

  const {
    control,
    handleSubmit,
    setValue,
    getValues,
    formState: { errors },
  } = methods;

  const onSubmit: SubmitHandler<SuppliersRequestSchemaV2Type> = values => {
    let payload: any = values;
    delete payload.tmp;
    payload.requestDate = dayjs(payload.requestDate)
      .add(7, 'hours')
      .toISOString();
    payload.suppliesItemDTO = values.invoiceSupplies.map(item => ({
      idItem: item?.itemDTO?.id,
      idUom: item?.uomDTO?.value,
      uomDTO: item?.uomDTO,
      price: item?.price,
      quantity: item?.quantity,
      note: item?.note,
      vat: item?.vatDTO?.percent,
      vatId: item?.vatDTO?.value,
      totalAmount: item?.totalItem,
      totalAmountAfterVat: item?.totalItemAfterVat,
    }));
    payload.isReview = !payload.isDoNotSign;
    if (payload?.deliveredQuantity === 0) delete payload.deliveredQuantity;
    if (payload?.remainingQuantity === 0) delete payload.remainingQuantity;
    isEditMode
      ? updateMutation.mutate(payload)
      : createMutation.mutate(payload);
  };

  // Side Effect
  useEffect(() => {
    if (detail_query?.data) {
      const {
        requestNumber,
        supplierId,
        supplier,
        requestType,
        note,
        suppliesItemDTO,
        deliveredQuantity,
        remainingQuantity,
        attachedFiles,
        requestStatus,
        createdBy,
        requestDate,
        totalAmountAfterVat,
        isReview,
        supplierContracts,
        supplierFullName,
        supplierPosition,
        supplierPhone,
        supplierEmail,
      } = detail_query?.data;

      setValue('code', requestNumber);

      setValue('supplierId', supplierId);
      setValue('supplierTaxCode', supplier?.taxCode);
      setValue('supplierAddress', supplier?.address);

      setValue('supplierFullName', supplierFullName);
      setValue('supplierPosition', supplierPosition);
      setValue('supplierPhone', supplierPhone);
      setValue('supplierEmail', supplierEmail);

      setValue('requestTypeId', requestType?.id);
      setValue('requestDate', new DateObject(requestDate));
      setValue('note', note);
      setValue('isDoNotSign', !isReview);
      setValue('deliveredQuantity', deliveredQuantity || 0);
      setValue('remainingQuantity', remainingQuantity || 0);
      setValue(
        'attachedFiles',
        attachedFiles.map(item => ({
          ...item,
          createdAt: new Date(item.createdAt),
        })) || [],
      );
      setValue(
        'invoiceSupplies',
        suppliesItemDTO?.map((record: any) => {
          let { item: itemDTO } = record;
          return {
            itemDTO: {
              id: itemDTO?.id,
              code: itemDTO?.code,
              name: itemDTO?.name,
              label: itemDTO?.code + ' - ' + itemDTO?.name,
            },
            note: record?.note,
            price: record?.price,
            quantity: record?.quantity,
            totalItem: record?.totalAmount,
            vat: record?.totalAmountAfterVat - record?.totalAmount,
            totalItemAfterVat: record?.totalAmountAfterVat,
            uomDTO: {
              label: '',
              value: itemDTO?.uom?.id,
            },
            vatDTO: {
              label: '',
              value: record?.vatId,
              percent: record?.vat,
            },
            feesBeforeImport: record?.['preImportFee'],
            importPercent: record?.['importTaxPercentage'],
            importTax: record?.['importTaxAmount'],
            envPercent: record?.['envFeePercentage'],
            envTax: record?.['envFeeAmount'],
            feesAfterImport: record?.['postImportFee'],
          };
        }),
      );
      let item = detail_query?.data?.requestApprovals?.find(
        item => item?.employeeId === account?.employeeId,
      );
      let isApprovalPerson = item?.employeeId === account?.employeeId;
      let isContainSignal = !!item?.approvedSign;
      let isCreatedByYourSelf = createdBy === account?.employeeId;
      let isNew = requestStatus === SuppliesRequestStatus.NEW;
      let isWaiting = requestStatus === SuppliesRequestStatus.WAITING_APPROVE;
      let isRejected = requestStatus === SuppliesRequestStatus.REJECTED;
      let isApproved = requestStatus === SuppliesRequestStatus.APPROVED;

      let isCanRejectOrApprove =
        isApprovalPerson && isWaiting && !isContainSignal;
      let isCanRequestAccept = isCreatedByYourSelf && (isNew || isRejected);
      let isCanCanceled = isCreatedByYourSelf && isNew;
      let isCanNotEdit = !isCreatedByYourSelf || isRejected || isApproved;
      setStatus({
        isCanRejectOrApprove,
        isCanRequestAccept,
        isCanCanceled,
      });
      setValue(
        'tmp.isApproved',
        requestStatus === SuppliesRequestStatus.APPROVED,
      );
      setValue(
        'tmp.isWaiting',
        requestStatus === SuppliesRequestStatus.WAITING_APPROVE,
      );
      setValue('tmp.isCanNotEdit', isCanNotEdit);
      setValue('status', requestStatus);
      let employeeIds = [];
      let requestApprovals = detail_query?.data?.requestApprovals?.map(item => {
        employeeIds.push(item.employeeId);
        return {
          employeeId: item.employeeId,
          employee: {
            code: '',
            fullName: '',
          },
          department: item.department,
          createdAt: item?.createdDate,
          updatedAt: item?.updatedAt,
          result: item?.result,
        };
      });
      setValue('requestApprovals', requestApprovals as any);
      setValue('supplierContracts', supplierContracts);
    }
  }, [detail_query?.data]);

  return (
    <FormProvider {...methods}>
      <Form>
        <CardV2
          header={
            <Flex justify="space-between" align="center">
              <Typography level={4}>
                {isEditMode ? detail_query?.data?.code : 'Thêm đề xuất'}
              </Typography>
              <Flex align="center" gap={10}>
                <ButtonBack>Đóng</ButtonBack>
                {isEditMode && status?.isCanCanceled && (
                  <ButtonBack
                    onClick={handleToggleAction({
                      key: 'cancel',
                      id: id_detail,
                    })}
                  >
                    Hủy
                  </ButtonBack>
                )}
                {isEditMode && status.isCanRejectOrApprove && (
                  <>
                    <ButtonV2
                      onClick={handleToggleAction({
                        key: 'reject',
                        id: id_detail,
                      })}
                    >
                      Từ chối
                    </ButtonV2>
                    <ButtonV2
                      onClick={handleToggleAction({
                        key: 'accept',
                        id: id_detail,
                      })}
                    >
                      Duyệt
                    </ButtonV2>
                  </>
                )}
                {isEditMode && status?.isCanRequestAccept && (
                  <ButtonV2
                    onClick={handleToggleAction({
                      key: 'request_accept',
                      id: id_detail,
                    })}
                  >
                    Trình Duyệt
                  </ButtonV2>
                )}
                {(!isEditMode ||
                  detail_query?.data?.createdBy === account.employeeId) && (
                  <AuthGuard permissionKey={isEditMode ? 'SUPPLIES_REQUESTS.EDIT' : 'SUPPLIES_REQUESTS.CREATE'}>
                    <ButtonV2
                      variant="solid"
                      color="blue"
                      onClick={handleSubmit(onSubmit)}
                      isLoading={
                        isEditMode
                          ? updateMutation.isPending
                          : createMutation.isPending
                      }
                    >
                      Lưu
                    </ButtonV2>
                  </AuthGuard>
                )}
                <AuthGuard permissionKey='SUPPLIES_REQUESTS.EXPORT'>
                  <ButtonPrint
                    onClick={() => {
                      if (id_detail) {
                        setSearchParams({
                          printId: id_detail,
                          printType: 'PURCHASE_PROPOSAL',
                        });
                        setTimeout(() => window.print(), 500);
                      }
                    }}
                    disabled={!id_detail}
                  />
                </AuthGuard>
              </Flex>
            </Flex>
          }
        >
          <Flex direction="column" rowGap={20}>
            <Summary />
            <Typography level={5}>NCC</Typography>
            <Row>
              <Col md={3}>
                <FormSelect
                  control={control}
                  name="supplierId"
                  label="Nhà cung cấp"
                  placeholder="Mã - Tên NCC"
                  options={suppliers?.data?.map(c => ({
                    label: `${c?.code} - ${c?.name || ''}`,
                    value: c?.id,
                  }))}
                  onChanges={e => {
                    const selected = suppliers?.data?.find(x => x.id === e);
                    if (selected) {
                      setValue('supplierTaxCode', selected?.taxCode);
                      setValue('supplierAddress', selected?.address);
                    }
                  }}
                />
              </Col>

              <Col md={3}>
                <FormInputV2
                  control={control}
                  name="supplierTaxCode"
                  label="MST"
                  placeholder="MST"
                  disabled
                />
              </Col>

              <Col md={6}>
                <FormInputV2
                  control={control}
                  name="supplierAddress"
                  label="Địa chỉ"
                  placeholder="Địa chỉ"
                  disabled
                />
              </Col>

              <Col md={3}>
                <FormInputV2
                  control={control}
                  name="supplierFullName"
                  label="Người đại diện"
                  placeholder="Điền"
                />
              </Col>

              <Col md={3}>
                <FormInputV2
                  control={control}
                  name="supplierPosition"
                  label="Chức vụ"
                  placeholder="Điền"
                />
              </Col>

              <Col md={3}>
                <FormInputV2
                  control={control}
                  name="supplierPhone"
                  label="Số điện thoại"
                  placeholder="Điền"
                />
              </Col>

              <Col md={3}>
                <FormInputV2
                  control={control}
                  name="supplierEmail"
                  label="Email"
                  placeholder="Điền"
                />
              </Col>
            </Row>
            <Goods />
            <Row>
              <Col md={6}>
                <PersonSign doNotSignKey="isDoNotSign" />
              </Col>
              <Col md={6}>
                <Attachments payload_key="attachedFiles" />
              </Col>
            </Row>
          </Flex>
        </CardV2>
      </Form>

      <Reject
        id_detail={id_detail}
        isOpen={actions.reject}
        toggle={handleToggleAction({ key: 'reject', id: id_detail })}
      />
      <Accept
        id_detail={id_detail}
        isOpen={actions.accept}
        toggle={handleToggleAction({ key: 'accept', id: id_detail })}
      />
      <Cancel
        id_detail={id_detail}
        isOpen={actions.cancel}
        toggle={handleToggleAction({ key: 'cancel', id: id_detail })}
      />
      <RequestAccept
        id_detail={id_detail}
        isOpen={actions.request_accept}
        toggle={handleToggleAction({ key: 'request_accept', id: id_detail })}
      />
    </FormProvider>
  );
};

export default FormProposal;
