import { zodResolver } from '@hookform/resolvers/zod';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import AuthGuard from 'app/components/guards/auth-guard';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import useCompany from 'app/hooks/use-company';
import { FORM } from 'app/shared/model/enumerations/form.model';
import {
  CompanySchema,
  companySchema,
} from 'app/validation/company.validation';
import { useContext, useEffect } from 'react';
import { FormProvider, useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { useNavigate, useParams } from 'react-router';
import { CompanyContext } from './company-provider';
import CompanyForm from './components/company-form';
import CompanyInactiveModal from './modals/company-confirm-inactive-modal';
import CompanyInactiveSuccessModal from './modals/company-inactive-success-modal';
import CompanyUpdateSuccessModal from './modals/company-update-success-modal';
import CompanyActiveModal from './modals/company-confirm-active-modal';
import CompanyActiveSuccessModal from './modals/company-active-success-modal';

const { useGetCompanyByIdQuery } = useCompany;

const CompanyUpdateForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const { setSelectedRecord, toggleConfirmActive, toggleConfirmInactive } =
    useContext(CompanyContext);

  const { data } = useGetCompanyByIdQuery(id);

  const methods = useForm<CompanySchema>({
    resolver: zodResolver(companySchema),
  });
  const { watch, setValue } = methods;
  const activeWatch = watch('isActive');

  const handleInactive = () => {
    setSelectedRecord(data?.id);
    toggleConfirmInactive();
  };

  const handleActive = () => {
    setSelectedRecord(data?.id);
    toggleConfirmActive();
  };

  useEffect(() => {
    if (data) {
      setValue('name', data?.name);
      setValue('description', data?.description ?? '');
      setValue('parentId', data?.parentId ?? '');
      setValue('normalizedName', data?.normalizedName ?? '');
      setValue('code', data?.code ?? '');
      setValue('taxCode', data?.taxCode ?? '');
      setValue('website', data?.website ?? '');
      setValue('callcenter', data?.callcenter ?? '');
      setValue('address', data?.address ?? '');
      setValue('representativeName', data?.representativeName ?? '');
      setValue('representativePhone', data?.representativePhone ?? '');
      setValue('representativeEmail', data?.representativeEmail ?? '');
      setValue('representativeDob', new DateObject(data?.representativeDob));
      setValue('representativeIdNumber', data?.representativeIdNumber ?? '');
      setValue('imageId', data?.imageId ?? '');

      setValue('isActive', data?.isActivated ?? false);
    }
  }, [data]);

  return (
    <FormProvider {...methods}>
      <CardV2
        header={
          <Flex justify="space-between" align="center">
            <Typography level={4}>{data?.name ?? ''}</Typography>
            <Flex align="center" gap={10}>
              <ButtonV2 onClick={() => navigate(PATH.COMPANY)}>Đóng</ButtonV2>

              <AuthGuard permissionKey="COMPANY.EDIT">
                {activeWatch && (
                  <ButtonV2
                    onClick={handleInactive}
                    style={{ borderColor: '#FDA29B', color: '#B42318' }}
                  >
                    Ngưng hoạt động
                  </ButtonV2>
                )}

                {!activeWatch && (
                  <ButtonV2 onClick={handleActive}>Hoạt động</ButtonV2>
                )}

                <ButtonV2
                  color="blue"
                  variant="solid"
                  form={FORM.COMPANY}
                  type="submit"
                  disabled={!activeWatch}
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

      <CompanyUpdateSuccessModal />

      <CompanyInactiveModal />
      <CompanyInactiveSuccessModal directUrl={PATH.COMPANY} />

      <CompanyActiveModal />
      <CompanyActiveSuccessModal directUrl={PATH.COMPANY} />
    </FormProvider>
  );
};

export default CompanyUpdateForm;
