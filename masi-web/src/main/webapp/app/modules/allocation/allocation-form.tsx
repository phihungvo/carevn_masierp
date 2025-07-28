import { zodResolver } from '@hookform/resolvers/zod';
import { Button, Col, Divider, Flex, Row, Typography } from 'antd';
import ButtonPrint from 'app/components/ButtonV2/ButtonPrint';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import WrapInputText from 'app/components/wrap-input-text/WrapInputText';
import PersonSelect from 'app/components/wrap-select/Person';
import WrapTextArea from 'app/components/wrap-text-area/WrapTextArea';
import { PATH } from 'app/constants/path';
import useAccountApp from 'app/hooks/use-account-app';
import useGoTo from 'app/hooks/use-go-to';
import useAction from 'app/hooks/user-action';
import { IEmployee } from 'app/shared/model/employee.model';
import { isEnableApprovalOrReject, isEnableCancel, isEnableRequestApproval, isEnableUpdate } from 'app/shared/util/logicActions';
import { useEffect } from 'react';
import { FormProvider, SubmitHandler, useForm } from 'react-hook-form';
import { useParams } from 'react-router';
import WrapDate from '../../components/wrap-date/WrapDate';
import { convertValues } from '../depreciation/utils/convertValues';
import PersonSign from '../request-payment/components/PersonSign/PersonSign';
import './allocation.scss';
import { useAllocationCreate, useAllocationDetail, useAllocationUpdate, useAllociationDelete } from './apis/hook';
import Accept from './component/actions/Accept';
import Cancel from './component/actions/Cancel';
import Reject from './component/actions/Reject';
import RequestAccept from './component/actions/RequestAccept';
import AllocationTableForm from './component/allocation-form-table';
import { Allociation } from './types/list';
import { allociationFormSchema, AllociationFormSchemaType } from './validations/allociation-form.validation';
import AuthGuard from 'app/components/guards/auth-guard';
import MonthYearSelect from 'app/components/wrap-select/MonthYearSelect';

const AllocationForm = () => {
  const { goTo } = useGoTo();
  const params = useParams()
  const id = params?.id
  const isEditMode = !!id

  const { employeeId } = useAccountApp()
  const { actions, handleToggleAction } = useAction()

  const methods = useForm<AllociationFormSchemaType>({
    resolver: zodResolver(allociationFormSchema),
    defaultValues: {
      requestApprovals: [],
      createdAt: !isEditMode && new Date().toISOString(),
      createdBy: !isEditMode && employeeId,
    },
  });
  const { handleSubmit, setValue, reset, watch } = methods;
  const code = watch('code')

  const detailQuery = useAllocationDetail(id)
  const createMutation = useAllocationCreate()
  const updateMutation = useAllocationUpdate(id)
  const deleteMutation = useAllociationDelete(id)

  const onSubmit: SubmitHandler<AllociationFormSchemaType> = values => {
    let converted = convertValues(values, isEditMode)
    let isDeleteAll = converted.isDeleteAll
    if (isDeleteAll && isEditMode) {
      deleteMutation.mutate(converted)
      return
    }
    isEditMode ? updateMutation.mutate(converted) : createMutation.mutate(converted)
  };

  useEffect(() => {
    if (detailQuery?.data) {
      setValue('code', detailQuery?.data?.code)
      setValue('depreciationDate', detailQuery?.data?.depreciationDate)
      setValue('accountingDate', detailQuery?.data?.accountingDate)
      setValue('employeeId', detailQuery?.data?.employeeId)
      setValue('createdBy', detailQuery?.data?.createdBy)
      setValue('createdAt', detailQuery?.data?.createdAt)
      setValue('requestApprovals', detailQuery?.data?.requestApprovals)
    }
  }, [detailQuery?.data])

  return (
    <div className="page_container">
      <FormProvider {...methods}>
        <Flex justify="space-between" align="center">
          <Typography.Text>{code}</Typography.Text>
          <Flex gap={10}>
            <Button onClick={goTo(PATH.ALLOCATION)}>Đóng</Button>
            {isEditMode && (
              <>
                <ButtonV2
                  onClick={handleToggleAction({ key: 'cancel', id })}
                  disabled={
                    !isEnableCancel<Allociation>({
                      employeeId,
                      data: detailQuery?.data as any,
                      createdBy: 'createdBy',
                      statusKey: 'status',
                    })
                  }
                >
                  Hủy
                </ButtonV2>
                <ButtonV2
                  onClick={handleToggleAction({ key: 'request_accept', id })}
                  disabled={
                    !isEnableRequestApproval<Allociation>({
                      data: detailQuery?.data as any,
                      employeeId,
                      createdBy: 'createdBy',
                      statusKey: 'status',
                    })
                  }
                >
                  Trình duyệt
                </ButtonV2>
                <ButtonV2
                  onClick={handleToggleAction({ key: 'reject', id })}
                  disabled={
                    !isEnableApprovalOrReject<Allociation>({
                      data: detailQuery?.data as any,
                      employeeId,
                      requestApproval: 'requestApprovals',
                      statusKey: 'status',
                    })
                  }
                >
                  Từ chối
                </ButtonV2>
                <ButtonV2
                  onClick={handleToggleAction({ key: 'accept', id })}
                  disabled={
                    !isEnableApprovalOrReject<Allociation>({
                      data: detailQuery?.data as any,
                      employeeId,
                      requestApproval: 'requestApprovals',
                      statusKey: 'status',
                    })
                  }
                >
                  Duyệt
                </ButtonV2>
              </>
            )}
            <AuthGuard
              permissionKey={
                isEditMode ? 'ASSET_ALLOCATION.EDIT' : 'ASSET_ALLOCATION.CREATE'
              }
            >
              <ButtonV2
                onClick={handleSubmit(onSubmit)}
                disabled={
                  !isEnableUpdate<Allociation>({
                    isEditMode,
                    employeeId,
                    data: detailQuery?.data as any,
                    createdBy: 'createdBy',
                    statusKey: 'status',
                  })
                }
                isLoading={createMutation.isPending || updateMutation.isPending || deleteMutation.isPending}
              >
                Lưu
              </ButtonV2>
            </AuthGuard>
            <AuthGuard permissionKey="ASSET_ALLOCATION.EXPORT">
              <ButtonPrint />
            </AuthGuard>
          </Flex>
        </Flex>
        <Divider />
        <Flex vertical gap={24}>
          <Row gutter={[16, 16]}>
            <Col span={24}>
              <Typography.Text>Thông tin cơ bản</Typography.Text>
            </Col>
            <Col span={12}>
              <Row gutter={[20, 12]}>
                <Col span={12}>
                  <MonthYearSelect<AllociationFormSchemaType>
                    label="Kỳ phân bổ"
                    name="code"
                  />
                </Col>
                <Col span={12}>
                  <WrapDate<AllociationFormSchemaType>
                    label="Ngày tính phân bổ"
                    name="depreciationDate"
                  />
                </Col>
                <Col span={12}>
                  <WrapDate<AllociationFormSchemaType>
                    label="Ngày hạch toán"
                    name="accountingDate"
                  />
                </Col>
                <Col span={12}>
                  <PersonSelect<AllociationFormSchemaType>
                    label="Người tính"
                    name="employeeId"
                    onSelectChange={(person: IEmployee) => {
                      setValue('name', person?.employeeProfile?.fullName);
                    }}
                  />
                </Col>
                <Col span={12}>
                  <PersonSelect<AllociationFormSchemaType>
                    label="Người tạo"
                    name="createdBy"
                    isDisabled
                  />
                </Col>
                <Col span={12}>
                  <WrapDate<AllociationFormSchemaType>
                    label="Ngày tạo"
                    name="createdAt"
                    disabled
                  />
                </Col>
              </Row>
            </Col>
            <Col span={12}>
              <WrapTextArea<AllociationFormSchemaType>
                rows={5}
                label="Diễn giải"
                name="description"
              />
            </Col>
          </Row>
          <AllocationTableForm />
          <Row>
            <Col span={12}>
              <PersonSign disabledCheckBox />
            </Col>
          </Row>
        </Flex>
      </FormProvider>
      <Accept
        id_detail={actions?.id}
        isOpen={actions?.accept}
        toggle={handleToggleAction({ key: 'accept' })}
      />
      <Reject
        id_detail={actions?.id}
        isOpen={actions?.reject}
        toggle={handleToggleAction({ key: 'reject' })}
      />
      <Cancel
        id_detail={actions?.id}
        isOpen={actions?.cancel}
        toggle={handleToggleAction({ key: 'cancel' })}
      />
      <RequestAccept
        id_detail={actions?.id}
        isOpen={actions?.request_accept}
        toggle={handleToggleAction({ key: 'request_accept' })}
      />
    </div>
  );
};

export default AllocationForm;
