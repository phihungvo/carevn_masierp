import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import { useNavigate } from 'react-router';
import TransferAssetsForm from './components/transfer-assets-form';
import { useModalsTransferAssets } from 'app/hooks/use-modals-transfer-assets';
import TransferAssetsCreateSuccessModals from './modals/transfer-assets-create-success-modals';
import AuthGuard from 'app/components/guards/auth-guard';

const TransferAssetsCreate = () => {
  const navigate = useNavigate();

  const {
    createSuccess: { openCreateSuccess, toggleCreateSuccess },
  } = useModalsTransferAssets();

  return (
    <CardV2
      header={
        <Flex justify="space-between" align="center">
          <Typography level={4}>Điều chuyển TS</Typography>
          <Flex align="center" gap={10}>
            <ButtonV2 onClick={() => navigate(PATH.TRANSFER_ASSETS)}>Đóng</ButtonV2>
            <AuthGuard permissionKey='ASSET_TRANSFER.CREATE'>
              <ButtonV2
                color="blue"
                variant="solid"
                form={FORM.TRANSFER_ASSETS}
                type="submit"
              >
                Lưu
              </ButtonV2>
            </AuthGuard>
          </Flex>
        </Flex>
      }
    >
      <TransferAssetsForm type="create" toggleSuccess={toggleCreateSuccess} />

      <TransferAssetsCreateSuccessModals
        isOpen={openCreateSuccess}
        toggle={toggleCreateSuccess}
      />
    </CardV2>
  );
};

export default TransferAssetsCreate;
