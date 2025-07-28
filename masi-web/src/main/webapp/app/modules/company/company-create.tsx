import { zodResolver } from '@hookform/resolvers/zod';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import AuthGuard from 'app/components/guards/auth-guard';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import { FORM } from 'app/shared/model/enumerations/form.model';
import {
  CompanySchema,
  companySchema,
} from 'app/validation/company.validation';
import { FormProvider, useForm } from 'react-hook-form';
import { useNavigate } from 'react-router';
import CompanyProvider from './company-provider';
import CompanyForm from './components/company-form';
import CompanyCreateSuccessModal from './modals/company-create-success-modal';

const CompanyCreateForm = () => {
  const navigate = useNavigate();

  const methods = useForm<CompanySchema>({
    resolver: zodResolver(companySchema),
    defaultValues: {
      createdAt: new Date().toISOString(),
    },
  });

  return (
    <CompanyProvider>
      <FormProvider {...methods}>
        <CardV2
          header={
            <Flex justify="space-between" align="center">
              <Typography level={4}>Thêm công ty</Typography>
              <Flex align="center" gap={10}>
                <ButtonV2 onClick={() => navigate(PATH.COMPANY)}>Đóng</ButtonV2>
                <AuthGuard permissionKey="CUSTOMERS.CREATE">
                  <ButtonV2
                    color="blue"
                    variant="solid"
                    form={FORM.COMPANY}
                    type="submit"
                  >
                    Lưu
                  </ButtonV2>
                </AuthGuard>
              </Flex>
            </Flex>
          }
        >
          <CompanyForm />
        </CardV2>
      </FormProvider>

      <CompanyCreateSuccessModal />
    </CompanyProvider>
  );
};

export default CompanyCreateForm;
