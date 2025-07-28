import { zodResolver } from '@hookform/resolvers/zod';
import {
  Button,
  Col,
  Divider,
  Flex,
  Row,
  Spin,
  Typography
} from 'antd';
import ButtonPrint from 'app/components/ButtonV2/ButtonPrint';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import WrapDate from 'app/components/wrap-date/WrapDate';
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
import '../../_antd.scss';
import PersonSign from '../request-payment/components/PersonSign/PersonSign';
import { useDepreciationDelete, useDepriciationCreate, useDepriciationDetail, useDepriciationUpdate } from './apis/hook';
import Accept from './component/actions/Accept';
import Cancel from './component/actions/Cancel';
import Reject from './component/actions/Reject';
import RequestAccept from './component/actions/RequestAccept';
import DepreciationTable from './component/depreciation-table';
import { Depreciation } from './types/list';
import { convertValues } from './utils/convertValues';
import { formSchema, FormSchemaType } from './validations/form.validation';
import AuthGuard from 'app/components/guards/auth-guard';
import MonthYearSelect from 'app/components/wrap-select/MonthYearSelect';

const DepreciationCreate = () => {
  const { goTo } = useGoTo();
  const params = useParams()
  const id = params?.id
  const isEditMode = !!id

  const { employeeId } = useAccountApp()
  const { actions, handleToggleAction } = useAction()

  const methods = useForm<FormSchemaType>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      requestApprovals: [],
      createdAt: !isEditMode && new Date().toISOString(),
      createdBy: !isEditMode && employeeId,
    }
  });
  const {
    handleSubmit, watch, setValue, getValues, reset, formState: { errors }
  } = methods;

  const detailQuery = useDepriciationDetail(id)
  const createMutation = useDepriciationCreate()
  const updateMutation = useDepriciationUpdate(id)
  const deleteMutaion = useDepreciationDelete(id)
  const isMutationLoading = createMutation.isPending || updateMutation.isPending || deleteMutaion.isPending

  const onSubmit: SubmitHandler<FormSchemaType> = (values) => {
    values = convertValues(values, isEditMode)
    let isDeleteAll = values?.isDeleteAll
    if (isDeleteAll && isEditMode) {
      deleteMutaion.mutate(values)
      return
    }
    isEditMode ? updateMutation.mutate(values) : createMutation.mutate(values)
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
    <div className="page_container page_transfer-assets">
      <Spin spinning={false}>
        <FormProvider {...methods}>
          <Flex justify="space-between" align="center">
            <Typography.Text>{watch('code')}</Typography.Text>
            <Flex gap={10}>
              <Button loading={false} onClick={goTo(PATH.DEPRECIATION)}>
                Đóng
              </Button>
              {isEditMode && (
                <>
                  <ButtonV2
                    onClick={handleToggleAction({ key: 'cancel', id })}
                    disabled={
                      !isEnableCancel<Depreciation>({
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
                      !isEnableRequestApproval<Depreciation>({
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
                      !isEnableApprovalOrReject<Depreciation>({
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
                      !isEnableApprovalOrReject<Depreciation>({
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
                  isEditMode
                    ? 'ASSET_DEPRECIATION.EDIT'
                    : 'ASSET_DEPRECIATION.CREATE'
                }
              >
                <ButtonV2
                  onClick={handleSubmit(onSubmit)}
                  isLoading={isMutationLoading}
                  disabled={
                    !isEnableUpdate<Depreciation>({
                      isEditMode,
                      employeeId,
                      data: detailQuery?.data as any,
                      createdBy: 'createdBy',
                      statusKey: 'status',
                    })
                  }
                  variant='primary'
                >
                  Lưu
                </ButtonV2>
              </AuthGuard>
              <AuthGuard permissionKey="ASSET_DEPRECIATION.EXPORT">
                <ButtonPrint />
              </AuthGuard>
            </Flex>
          </Flex>
          <Divider
            style={{
              border: '1px solid #98A2B3',
              marginTop: 10,
              marginBottom: 24,
            }}
          />
          <Flex vertical gap={20}>
            <Row gutter={[20, 12]}>
              <Col span={24}>
                <Typography.Text>Thông tin cơ bản</Typography.Text>
              </Col>
              <Col span={12}>
                <Row gutter={[20, 12]}>
                  <Col span={12}>
                    <MonthYearSelect<FormSchemaType>
                      label="Kỳ khấu hao"
                      name="code"
                    />
                  </Col>
                  <Col span={12}>
                    <WrapDate<FormSchemaType>
                      label="Ngày tính khấu hao"
                      name="depreciationDate"
                    />
                  </Col>
                  <Col span={12}>
                    <WrapDate<FormSchemaType>
                      label="Ngày hạch toán"
                      name="accountingDate"
                    />
                  </Col>
                  <Col span={12}>
                    <PersonSelect<FormSchemaType>
                      label="Người tính"
                      name="employeeId"
                      onSelectChange={(person: IEmployee) => {
                        setValue('name', person?.employeeProfile?.fullName);
                      }}
                    />
                  </Col>
                  <Col span={12}>
                    <PersonSelect<FormSchemaType>
                      label="Người tạo"
                      name="createdBy"
                      isDisabled
                    />
                  </Col>
                  <Col span={12}>
                    <WrapDate<FormSchemaType>
                      label="Ngày tạo"
                      name="createdAt"
                      disabled
                    />
                  </Col>
                </Row>
              </Col>
              <Col span={12}>
                <WrapTextArea<FormSchemaType>
                  rows={5}
                  label="Diễn giải"
                  name="description"
                />
              </Col>
            </Row>
            <DepreciationTable />
            <Row>
              <Col span={12}>
                <PersonSign disabledCheckBox />
              </Col>
            </Row>
          </Flex>
        </FormProvider>
      </Spin>
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

export default DepreciationCreate;
