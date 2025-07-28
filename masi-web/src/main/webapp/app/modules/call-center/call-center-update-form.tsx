import { zodResolver } from '@hookform/resolvers/zod';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import { useAppSelector } from 'app/config/store';
import { PATH } from 'app/constants/path';
import useCallCenter from 'app/hooks/use-call-center';
import { CALL_CENTER_STATUS } from 'app/shared/model/enumerations/call-center';
import { FORM } from 'app/shared/model/enumerations/form.model';
import {
  callCenterSchema,
  CallCenterSchema,
} from 'app/validation/call-center.validation';
import { useContext, useEffect } from 'react';
import { FormProvider, useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { useNavigate, useParams } from 'react-router';
import { CallCenterContext } from './call-center-provider';
import CallCenterForm from './components/call-center-form';
import CallCenterApproveSuccessModal from './modals/call-center-approve-success-modal';
import CallCenterCloseSuccessModal from './modals/call-center-close-success-modal';
import CallCenterCompleteSuccessModal from './modals/call-center-complete-success-modal';
import CallCenterConfirmApproveModal from './modals/call-center-confirm-approve-modal';
import CallCenterConfirmCloseModal from './modals/call-center-confirm-close-modal';
import CallCenterConfirmCompleteModal from './modals/call-center-confirm-complete-modal';
import CallCenterUpdateSuccessModal from './modals/call-center-update-success-modal';
import dayjs from 'dayjs';
import AuthGuard from 'app/components/guards/auth-guard';

const { useGetCallCenterByIdQuery } = useCallCenter;
const CallCenterUpdateForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const { toggleApprove, toggleComplete, toggleClose, setSelectedRecord } =
    useContext(CallCenterContext);

  const { data } = useGetCallCenterByIdQuery(id);

  const account = useAppSelector(state => state.authentication.account);

  const methods = useForm<CallCenterSchema>({
    resolver: zodResolver(callCenterSchema),
    defaultValues: {
      status: CALL_CENTER_STATUS.NEW,
      solutions: [
        {
          createdAt: new Date(),
          resolutionContent: '',
          responseContent: '',
          employeeId: account?.id,
        },
      ],
    },
  });

  const { watch, setValue } = methods;
  const statusWatch = watch('status');

  useEffect(() => {
    if (data) {
      setValue('status', data?.status);
      setValue('customerId', data?.customerId);
      setValue('employeeCreatedId', data?.employeeCreatedId);
      setValue('groupCS', data?.groupCS);
      setValue('phoneOfCaller', data?.phoneOfCaller);
      setValue('phoneOfName', data?.phoneOfName);

      setValue('receptionDate', data?.receptionDate);
      setValue('typeCS', data?.typeCS);
      setValue('problemContent', data?.problemContent);
      setValue(
        'solutions',
        data?.attribute?.map(x => ({ ...x, createdAt: new Date(x.createdAt) })),
      );

      const createdByName = [
        data?.createdByEmployee?.employeeCode,
        data?.createdByEmployee?.fullName,
      ].join(' - ');
      setValue('createdBy', createdByName);
      setValue('createdAt', dayjs(data?.createdAt).toISOString());
    }
  }, [data]);

  const handleApprove = () => {
    toggleApprove();
    setSelectedRecord(data.id);
  };

  const handleClose = () => {
    toggleClose();
    setSelectedRecord(data.id);
  };

  const handleComplete = () => {
    toggleComplete();
    setSelectedRecord(data?.id);
  };

  return (
    <FormProvider {...methods}>
      <CardV2
        header={
          <Flex justify="space-between" align="center">
            <Typography level={4}>Cập nhật cuộc gọi</Typography>
            <Flex align="center" gap={10}>
              <ButtonV2 onClick={() => navigate(PATH.CALL_CENTER)}>
                Đóng
              </ButtonV2>

              <AuthGuard permissionKey='CUSTOMER_SERVICES_CALL_CENTER.EDIT'>
                {statusWatch === (CALL_CENTER_STATUS.NEW as string) && (
                  <ButtonV2 onClick={handleApprove}>Xử lý</ButtonV2>
                )}
  
                {statusWatch === (CALL_CENTER_STATUS.PROCESSING as string) && (
                  <ButtonV2 onClick={handleClose}>Đóng case</ButtonV2>
                )}
  
                {/* {statusWatch === (CALL_CENTER_STATUS.PROCESSING as string) && (
                  <ButtonV2 onClick={handleComplete}>Hoàn thành</ButtonV2>
                )} */}
                <ButtonV2
                  color="blue"
                  variant="solid"
                  form={FORM.CALL_CENTER}
                  type="submit"
                  disabled={
                    statusWatch === (CALL_CENTER_STATUS.COMPLETED as string) ||
                    statusWatch === (CALL_CENTER_STATUS.CLOSED as string)
                  }
                >
                  Lưu
                </ButtonV2>
              </AuthGuard>
            </Flex>
          </Flex>
        }
      >
        <CallCenterForm />
      </CardV2>

      <CallCenterUpdateSuccessModal />

      <CallCenterConfirmApproveModal />
      <CallCenterApproveSuccessModal directUrl={PATH.CALL_CENTER} />
      <CallCenterConfirmCloseModal />
      <CallCenterCloseSuccessModal directUrl={PATH.CALL_CENTER} />
      <CallCenterConfirmCompleteModal />
      <CallCenterCompleteSuccessModal directUrl={PATH.CALL_CENTER} />
    </FormProvider>
  );
};

export default CallCenterUpdateForm;
