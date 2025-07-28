import { zodResolver } from '@hookform/resolvers/zod';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import { useAppSelector } from 'app/config/store';
import { PATH } from 'app/constants/path';
import { FORM } from 'app/shared/model/enumerations/form.model';
import {
  inventoriesExportSchema,
  InventoriesExportSchema,
} from 'app/validation/inventories-export.validation';
import { FormProvider, useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { useNavigate } from 'react-router';
import { useSearchParams } from 'react-router-dom';
import InventoriesForm from './components/inventories-export-form';
import InventoriesExportStorageProvider from './inventories-storage-export-provider';
import InventoriesAttachmentModal from './modals/inventories-attachment-modal';
import InventoriesCreateSuccessModal from './modals/inventories-create-success-modal';
import { InventoriesWarehouse } from 'app/shared/model/enumerations/warehouse.model';

const InventoriesExportCreate = () => {
  const navigate = useNavigate();

  const [searchParams] = useSearchParams();
  const warehouseImportType = searchParams.get('warehouse');

  const titlePrefix =
    warehouseImportType ===
    (InventoriesWarehouse?.WAREHOUSE_DEPRECIATION_EXPORT as string)
      ? 'TS - CCDC'
      : '';

  const account = useAppSelector(state => state.authentication.account);

  const methods = useForm<InventoriesExportSchema>({
    resolver: zodResolver(inventoriesExportSchema),
    defaultValues: {
      createdAt: new DateObject(),
      dateCreate: new DateObject(),
      requestApprovals: [],
      createdByName: `${account?.lastName} ${account?.firstName}`,
      warehouseGroupType: warehouseImportType,
    },
  });

  return (
    <InventoriesExportStorageProvider>
      <FormProvider {...methods}>
        <CardV2
          header={
            <Flex justify="space-between" align="center">
              <Typography level={4}>
                Thêm mới phiếu xuất kho {titlePrefix}
              </Typography>
              <Flex align="center" gap={10}>
                <ButtonV2
                  onClick={() =>
                    navigate(PATH.INVENTORIES_STORAGE_EXPORT + location.search)
                  }
                >
                  Đóng
                </ButtonV2>
                <ButtonV2
                  color="blue"
                  variant="solid"
                  form={FORM.INVENTORIES}
                  type="submit"
                >
                  Lưu
                </ButtonV2>
              </Flex>
            </Flex>
          }
        >
          <InventoriesForm type="create" />
        </CardV2>

        <InventoriesAttachmentModal />
      </FormProvider>

      <InventoriesCreateSuccessModal />
    </InventoriesExportStorageProvider>
  );
};

export default InventoriesExportCreate;
