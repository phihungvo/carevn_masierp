import { zodResolver } from '@hookform/resolvers/zod';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import { useAppSelector } from 'app/config/store';
import { PATH } from 'app/constants/path';
import { FORM } from 'app/shared/model/enumerations/form.model';
import {
  supplierContractsSchema,
  SupplierContractsSchema,
} from 'app/validation/supplier-contracts.validation';
import { FormProvider, useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { useNavigate } from 'react-router';
import SupplierContractsForm from './components/supplier-contracts-form';
import SupplierContractsAttachmentModal from './modals/supplier-contracts-attachment-modal';
import SupplierContractsCreateSuccessModal from './modals/supplier-contracts-create-success-modal';
import SupplierContractsStorageProvider from './supplier-contracts-storage-provider';

const SupplierContractsCreate = () => {
  const navigate = useNavigate();

  const account = useAppSelector(state => state.authentication.account);

  const methods = useForm<SupplierContractsSchema>({
    resolver: zodResolver(supplierContractsSchema),
    defaultValues: {
      createdAt: new DateObject(),
      requestApprovals: [],
      createdByName: `${account?.lastName} ${account?.firstName}`,
    },
  });

  return (
    <SupplierContractsStorageProvider>
      <FormProvider {...methods}>
        <CardV2
          header={
            <Flex justify="space-between" align="center">
              <Typography level={4}>Thêm hợp đồng mua</Typography>
              <Flex align="center" gap={10}>
                <ButtonV2 onClick={() => navigate(PATH.SUPPLIER_CONTRACTS)}>
                  Đóng
                </ButtonV2>
                <ButtonV2
                  color="blue"
                  variant="solid"
                  form={FORM.SUPPLIER_CONTRACTS}
                  type="submit"
                >
                  Lưu
                </ButtonV2>
              </Flex>
            </Flex>
          }
        >
          <SupplierContractsForm type="create" />
        </CardV2>

        <SupplierContractsAttachmentModal />
      </FormProvider>

      <SupplierContractsCreateSuccessModal />
    </SupplierContractsStorageProvider>
  );
};

export default SupplierContractsCreate;
