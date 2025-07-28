import { zodResolver } from '@hookform/resolvers/zod';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import { useAppSelector } from 'app/config/store';
import { PATH } from 'app/constants/path';
import { FORM } from 'app/shared/model/enumerations/form.model';
import {
  complainSchema,
  ComplainSchema,
} from 'app/validation/complain.validation';
import { FormProvider, useForm } from 'react-hook-form';
import { useNavigate } from 'react-router';
import ComplainProvider from './complain-provider';
import ComplainForm from './components/complain-form';
import ComplainCreateSuccessModal from './modals/complain-create-success-modal';
import AuthGuard from 'app/components/guards/auth-guard';

const ComplainCreateForm = () => {
  const navigate = useNavigate();
  const account = useAppSelector(state => state.authentication.account);

  const methods = useForm<ComplainSchema>({
    resolver: zodResolver(complainSchema),
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
    <ComplainProvider>
      <FormProvider {...methods}>
        <CardV2
          header={
            <Flex justify="space-between" align="center">
              <Typography level={4}>Thêm khiếu nại</Typography>
              <Flex align="center" gap={10}>
                <ButtonV2 onClick={() => navigate(PATH.COMPLAIN)}>
                  Đóng
                </ButtonV2>
                <AuthGuard permissionKey='CUSTOMER_SERVICES_COMPLAIN.CREATE'>
                  <ButtonV2
                    color="blue"
                    variant="solid"
                    form={FORM.COMPLAIN}
                    type="submit"
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
      </FormProvider>

      <ComplainCreateSuccessModal />
    </ComplainProvider>
  );
};

export default ComplainCreateForm;
