import { zodResolver } from '@hookform/resolvers/zod';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import { useAppSelector } from 'app/config/store';
import { PATH } from 'app/constants/path';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { InventoriesWarehouse } from 'app/shared/model/enumerations/warehouse.model';
import {
  inventoriesSchema,
  InventoriesSchema,
} from 'app/validation/inventories.validation';
import { FormProvider, useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { useNavigate } from 'react-router';
import { useSearchParams } from 'react-router-dom';
import InventoriesForm from './components/inventories-form';
import InventoriesStorageProvider from './inventories-storage-provider';
import InventoriesAttachmentModal from './modals/inventories-attachment-modal';
import InventoriesCreateSuccessModal from './modals/inventories-create-success-modal';
import AuthGuard from 'app/components/guards/auth-guard';

const InventoriesCreate = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const warehouseImportType = searchParams.get('warehouse');

  const titlePrefix =
    warehouseImportType ===
    (InventoriesWarehouse?.WAREHOUSE_DEPRECIATION_IMPORT as string)
      ? 'TS - CCDC'
      : '';

  const account = useAppSelector(state => state.authentication.account);

  const methods = useForm<InventoriesSchema>({
    resolver: zodResolver(inventoriesSchema),
    defaultValues: {
      createdAt: new DateObject(),
      dateCreate: new DateObject(),
      requestApprovals: [],
      createdByName: `${account?.lastName} ${account?.firstName}`,
    },
  });

  return (
    <InventoriesStorageProvider>
      <FormProvider {...methods}>
        <CardV2
          header={
            <Flex justify="space-between" align="center">
              <Typography level={4}>
                Thêm mới phiếu nhập kho {titlePrefix}
              </Typography>
              <Flex align="center" gap={10}>
                <ButtonV2
                  onClick={() =>
                    navigate(PATH.INVENTORIES_STORAGE + location.search)
                  }
                >
                  Đóng
                </ButtonV2>
                <AuthGuard permissionKey='LOGISTICS_INVENTORIES_STORAGE.CREATE'>
                  <ButtonV2
                    color="blue"
                    variant="solid"
                    form={FORM.INVENTORIES}
                    type="submit"
                  >
                    Lưu
                  </ButtonV2>
                </AuthGuard>
              </Flex>
            </Flex>
          }
        >
          <InventoriesForm type="create" />
        </CardV2>

        <InventoriesAttachmentModal />
      </FormProvider>

      <InventoriesCreateSuccessModal />
    </InventoriesStorageProvider>
  );
};

export default InventoriesCreate;
