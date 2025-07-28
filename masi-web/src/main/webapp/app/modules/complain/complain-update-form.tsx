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
  complainSchema,
  ComplainSchema,
} from 'app/validation/complain.validation';
import dayjs from 'dayjs';
import { useContext, useEffect } from 'react';
import { FormProvider, useForm } from 'react-hook-form';
import { useNavigate, useParams } from 'react-router';
import { ComplainContext } from './complain-provider';
import ComplainForm from './components/complain-form';
import ComplainApproveSuccessModal from './modals/complain-approve-success-modal';
import ComplainCloseSuccessModal from './modals/complain-close-success-modal';
import ComplainCompleteSuccessModal from './modals/complain-complete-success-modal';
import ComplainConfirmApproveModal from './modals/complain-confirm-approve-modal';
import ComplainConfirmCloseModal from './modals/complain-confirm-close-modal';
import ComplainConfirmCompleteModal from './modals/complain-confirm-complete-modal';
import ComplainUpdateSuccessModal from './modals/complain-update-success-modal';
import AuthGuard from 'app/components/guards/auth-guard';

const { useGetCallCenterByIdQuery } = useCallCenter;
const ComplainUpdateForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const { toggleApprove, toggleComplete, toggleClose, setSelectedRecord } =
    useContext(ComplainContext);

  const { data } = useGetCallCenterByIdQuery(id);

  const account = useAppSelector(state => state.authentication.account);

  const methods = useForm<ComplainSchema>({
    resolver: zodResolver(complainSchema),
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
      setValue('sourceCS', data?.sourceCs);
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
      setValue('createdAt', dayjs(data?.createdAt)?.toISOString());
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
    setSelectedRecord(data.id);
  };

  return (
    <FormProvider {...methods}>
      <CardV2
        header={
          <Flex justify="space-between" align="center">
            <Typography level={4}>Cập nhật khiếu nại</Typography>
            <Flex align="center" gap={10}>
              <ButtonV2 onClick={() => navigate(PATH.COMPLAIN)}>Đóng</ButtonV2>

              <AuthGuard permissionKey='CUSTOMER_SERVICES_COMPLAIN.EDIT'>
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
                  form={FORM.COMPLAIN}
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
        <ComplainForm />
      </CardV2>

      <ComplainUpdateSuccessModal />

      <ComplainConfirmApproveModal />
      <ComplainApproveSuccessModal directUrl={PATH.COMPLAIN} />
      <ComplainConfirmCloseModal />
      <ComplainCloseSuccessModal directUrl={PATH.COMPLAIN} />
      <ComplainConfirmCompleteModal />
      <ComplainCompleteSuccessModal directUrl={PATH.COMPLAIN} />
    </FormProvider>
  );
};

export default ComplainUpdateForm;
