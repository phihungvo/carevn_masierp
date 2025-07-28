import { zodResolver } from '@hookform/resolvers/zod';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import { useAppSelector } from 'app/config/store';
import { PATH } from 'app/constants/path';
import { FORM } from 'app/shared/model/enumerations/form.model';
import {
  StocktakingSchema,
  stocktakingSchema,
} from 'app/validation/stocktaking.validation';
import { FormProvider, useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { useNavigate } from 'react-router';
import StocktakingForm from './components/stocktaking-form';
import StocktakingCreateSuccessModal from './modals/stocktaking-create-success-modal';
import StocktakingProvider from './stocktaking-provider';
import AuthGuard from 'app/components/guards/auth-guard';

const StocktakingCreate = () => {
  const navigate = useNavigate();

  const account = useAppSelector(state => state.authentication.account);

  const methods = useForm<StocktakingSchema>({
    resolver: zodResolver(stocktakingSchema),
    defaultValues: {
      createdAt: new DateObject(),
      checkDate: new DateObject(),
      requestApprovals: [],
      createdByName: `${account?.lastName} ${account?.firstName}`,
    },
  });

  return (
    <StocktakingProvider>
      <FormProvider {...methods}>
        <CardV2
          header={
            <Flex justify="space-between" align="center">
              <Typography level={4}>Thêm kiểm kê</Typography>
              <Flex align="center" gap={10}>
                <ButtonV2 onClick={() => navigate(PATH.STOCKTAKING)}>
                  Đóng
                </ButtonV2>
                <AuthGuard permissionKey='LOGISTICS_STOCKTAKING.CREATE'>
                  <ButtonV2
                    color="blue"
                    variant="solid"
                    form={FORM.STOCKTAKING}
                    type="submit"
                  >
                    Lưu
                  </ButtonV2>
                </AuthGuard>
              </Flex>
            </Flex>
          }
        >
          <StocktakingForm type="create" />
        </CardV2>
      </FormProvider>

      <StocktakingCreateSuccessModal />
    </StocktakingProvider>
  );
};

export default StocktakingCreate;
