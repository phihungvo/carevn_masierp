import { zodResolver } from '@hookform/resolvers/zod';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import { useAppSelector } from 'app/config/store';
import { PATH } from 'app/constants/path';
import { FORM } from 'app/shared/model/enumerations/form.model';
import {
  callCenterSchema,
  CallCenterSchema,
} from 'app/validation/call-center.validation';
import { FormProvider, useForm } from 'react-hook-form';
import { useNavigate } from 'react-router';
import CallCenterProvider from './call-center-provider';
import CallCenterForm from './components/call-center-form';
import CallCenterCreateSuccessModal from './modals/call-center-create-success-modal';
import AuthGuard from 'app/components/guards/auth-guard';

const CallCenterCreateForm = () => {
  const navigate = useNavigate();
  const account = useAppSelector(state => state.authentication.account);

  const methods = useForm<CallCenterSchema>({
    resolver: zodResolver(callCenterSchema),
    defaultValues: {
      createdAt: new Date().toISOString(),
      createdBy: `${account?.lastName} ${account?.firstName}`,
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

  return (
    <CallCenterProvider>
      <FormProvider {...methods}>
        <CardV2
          header={
            <Flex justify="space-between" align="center">
              <Typography level={4}>Thêm cuộc gọi</Typography>
              <Flex align="center" gap={10}>
                <ButtonV2 onClick={() => navigate(PATH.CALL_CENTER)}>
                  Đóng
                </ButtonV2>
                <AuthGuard permissionKey='CUSTOMER_SERVICES_CALL_CENTER.CREATE'>
                  <ButtonV2
                    color="blue"
                    variant="solid"
                    form={FORM.CALL_CENTER}
                    type="submit"
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
      </FormProvider>

      <CallCenterCreateSuccessModal />
    </CallCenterProvider>
  );
};

export default CallCenterCreateForm;
