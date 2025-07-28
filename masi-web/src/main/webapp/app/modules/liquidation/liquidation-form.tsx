import { zodResolver } from '@hookform/resolvers/zod';
import { Col, Divider, Flex, Row, Spin, Typography } from 'antd';
import ButtonPrint from 'app/components/ButtonV2/ButtonPrint';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import WrapDate from 'app/components/wrap-date/WrapDate';
import PersonLabel from 'app/components/wrap-input-text/PersonLabel';
import WrapInputText from 'app/components/wrap-input-text/WrapInputText';
import WrapSelect from 'app/components/wrap-select/WrapSelect';
import WrapTextArea from 'app/components/wrap-text-area/WrapTextArea';
import { DATE_FORMAT } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import useAccountApp from 'app/hooks/use-account-app';
import useGoTo from 'app/hooks/use-go-to';
import useAction from 'app/hooks/user-action';
import {
  isEnableApprovalOrReject,
  isEnableCancel,
  isEnableRequestApproval,
  isEnableUpdate,
} from 'app/shared/util/logicActions';
import dayjs from 'dayjs';
import { useEffect } from 'react';
import { FormProvider, SubmitHandler, useForm } from 'react-hook-form';
import { useParams } from 'react-router';
import PersonSign from '../request-payment/components/PersonSign/PersonSign';
import {
  useLiquidationCreate,
  useLiquidationDetail,
  useLiquidationUpdate,
} from './apis/hook';
import Accept from './component/actions/Accept';
import Cancel from './component/actions/Cancel';
import Reject from './component/actions/Reject';
import RequestAccept from './component/actions/RequestAccept';
import HumanResourceTable from './component/human-resource-table';
import PropertyListTable from './component/property-list-table';
import { statusData } from './constants/status';
import './liquidation.scss';
import { Liquidation } from './types/list';
import { convertValues } from './utils/convertValues';
import {
  liquidationFormSchema,
  LiquidationFormSchemaType,
} from './validations/liquidation-form.validation';
import { useSearchParams } from 'react-router-dom';
import AuthGuard from 'app/components/guards/auth-guard';

const LiquidationForm = () => {
  const { goTo } = useGoTo();
  const params = useParams();
  const [searchParams, setSearchParams] = useSearchParams();
  const id = params.id;
  const isEditMode = !!id;

  const { employeeId } = useAccountApp();
  const { actions, handleToggleAction } = useAction();

  const methods = useForm<LiquidationFormSchemaType>({
    resolver: zodResolver(liquidationFormSchema),
    defaultValues: {
      reflectNumber: dayjs().format(DATE_FORMAT.DATE),
      createdAt: dayjs().toISOString(),
      liquidationDate: dayjs().toISOString(),
      requestApprovals: [],
      createdBy: employeeId,
    },
  });
  const {
    handleSubmit,
    watch,
    getValues,
    reset,
    formState: { errors },
  } = methods;

  const detailQuery = useLiquidationDetail(id);
  const createMutation = useLiquidationCreate();
  const updateMutation = useLiquidationUpdate(id);

  const onSubmit: SubmitHandler<LiquidationFormSchemaType> = values => {
    values = convertValues(values);
    isEditMode ? updateMutation.mutate(values) : createMutation.mutate(values);
  };

  useEffect(() => {
    if (detailQuery?.data) {
      reset(detailQuery?.data);
    }
  }, [detailQuery?.data]);

  return (
    <div className="page_container">
      <Spin spinning={false}>
        <FormProvider {...methods}>
          <Flex justify="space-between" align="center">
            <Typography.Text>{watch('reflectNumber')}</Typography.Text>
            <Flex gap={10}>
              <ButtonV2 onClick={goTo(PATH.LIQUIDATION)}>Đóng</ButtonV2>
              {isEditMode && (
                <>
                  <ButtonV2
                    onClick={handleToggleAction({ key: 'cancel', id })}
                    disabled={
                      !isEnableCancel<Liquidation>({
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
                      !isEnableRequestApproval<Liquidation>({
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
                      !isEnableApprovalOrReject<Liquidation>({
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
                      !isEnableApprovalOrReject<Liquidation>({
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
              <AuthGuard permissionKey={isEditMode ? 'ASSET_LIQUIDATION.EDIT' : 'ASSET_LIQUIDATION.CREATE'}>
                <ButtonV2
                  onClick={handleSubmit(onSubmit)}
                  disabled={
                    !isEnableUpdate<Liquidation>({
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
              <AuthGuard permissionKey='ASSET_LIQUIDATION.EXPORT'>
                <ButtonPrint
                  onClick={() => {
                    if (id) {
                      setSearchParams({
                        printId: id,
                        printType: 'LIQUIDATION',
                      });
                      setTimeout(() => window.print(), 500);
                    }
                  }}
                />
              </AuthGuard>
            </Flex>
          </Flex>
          <Divider />
          <Flex vertical gap={24}>
            <Row gutter={[16, 16]}>
              <Col span={24}>
                <Typography.Text>Thông tin cơ bản</Typography.Text>
              </Col>
              <Col span={24}>
                <Row gutter={[20, 12]}>
                  <Col span={8}>
                    <WrapInputText<LiquidationFormSchemaType>
                      label="Số tham chiếu"
                      name="reflectNumber"
                      disabled
                    />
                  </Col>
                  <Col span={8}>
                    <WrapDate<LiquidationFormSchemaType>
                      label="Ngày thanh lý"
                      name="liquidationDate"
                      disabled
                    />
                  </Col>
                  <Col span={8}>
                    <WrapSelect<LiquidationFormSchemaType>
                      label="Lý do thanh lý"
                      name="reason"
                      data={statusData}
                    />
                  </Col>
                  <Col span={8}>
                    <PersonLabel<LiquidationFormSchemaType>
                      label="Người tạo"
                      name="createdBy"
                    />
                  </Col>
                  <Col span={8}>
                    <WrapDate<LiquidationFormSchemaType>
                      label="Ngày tạo"
                      name="createdAt"
                      disabled
                    />
                  </Col>
                  <Col span={24}>
                    <WrapTextArea<LiquidationFormSchemaType>
                      label="Diễn giải"
                      name="description"
                      rows={3}
                    />
                  </Col>
                </Row>
              </Col>
            </Row>
            <HumanResourceTable />
            <PropertyListTable />
            <PersonSign disabledCheckBox />
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

export default LiquidationForm;
