import { zodResolver } from '@hookform/resolvers/zod';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import { DATE_FORMAT } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import { FORM } from 'app/shared/model/enumerations/form.model';
import {
  ManufactureOrderByOrderCreateSchema,
  manufactureOrderByOrderCreateSchema,
} from 'app/validation/manufacture-order.validation';
import dayjs from 'dayjs';
import { FormProvider, useForm } from 'react-hook-form';
import { useNavigate, useParams } from 'react-router';
import ProductionManagementProvider from '../production-provider';
import ManufactureOrderByOrderCreateSuccessModal from './manufacture-order-by-order-create-success-modal';
import ManufactureOrderByOrderForm from './manufacture-order-by-order-form';
import ManufactureOrderByOrderUpdateSuccessModal from './manufacture-order-by-order-update-success-modal';
import AuthGuard from 'app/components/guards/auth-guard';

const ManufactureOrderByOrderCreate = () => {
  const navigate = useNavigate();
  const { id } = useParams();

  const methods = useForm<ManufactureOrderByOrderCreateSchema>({
    resolver: zodResolver(manufactureOrderByOrderCreateSchema),
    defaultValues: {
      code: dayjs().format(DATE_FORMAT.DATE_TIME),
      createdAt: new Date(),
    },
  });

  return (
    <ProductionManagementProvider>
      <FormProvider {...methods}>
        <CardV2
          header={
            <Flex justify="space-between" align="center">
              <Typography level={4}>
                {id ? 'Cập nhật' : 'Thêm mới'} lệnh trộn bột
              </Typography>
              <Flex align="center" gap={10}>
                <ButtonV2
                  onClick={() => {
                    if (id)
                      navigate(
                        PATH.PRODUCTION_PROCESS_UPDATE.replace(':id', id),
                      );
                    else navigate(PATH.PRODUCTION_MANUFACTURE_ORDER_BY_ORDER);
                  }}
                >
                  Đóng
                </ButtonV2>

                <AuthGuard permissionKey={ id ? 'PRODUCTION_MANUFACTURE_ORDER.EDIT' : 'PRODUCTION_MANUFACTURE_ORDER.CREATE'}>
                  <ButtonV2
                    color="blue"
                    variant="solid"
                    form={FORM.MANUFACTURE_ORDER_BY_ORDER}
                    type="submit"
                  >
                    Lưu
                  </ButtonV2>
                </AuthGuard>
              </Flex>
            </Flex>
          }
        >
          <ManufactureOrderByOrderForm />
        </CardV2>

        <ManufactureOrderByOrderCreateSuccessModal
          directUrl={PATH.PRODUCTION_MANUFACTURE_ORDER_BY_ORDER}
        />

        <ManufactureOrderByOrderUpdateSuccessModal
          directUrl={PATH.PRODUCTION_PROCESS_UPDATE.replace(':id', id)}
        />
      </FormProvider>
    </ProductionManagementProvider>
  );
};

export default ManufactureOrderByOrderCreate;
