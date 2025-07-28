import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import { useNavigate } from 'react-router';
import SuppliesForm from './components/supplies-form';
import { useModalsSupplies } from 'app/hooks/use-modals-supplies';
import SuppliesCreateSuccessModals from './modals/supplies-create-success-modals';
import AuthGuard from 'app/components/guards/auth-guard';

const SuppliesCreate = () => {
  const navigate = useNavigate();

  const {
    create: { openCreate, toggleCreate },
    filter: { openFilter, toggleFilter },
    approve: { toggleApprove },
    createSuccess: { openCreateSuccess, toggleCreateSuccess },
    delete: { openDelete, toggleDelete },
    deleteSuccess: { openDeleteSuccess, toggleDeleteSuccess },
    detail: { openDetail, toggleDetail },
    propose: { togglePropose },
    update: { openUpdate, toggleUpdate },
    updateSuccess: { openUpdateSuccess, toggleUpdateSuccess },
    dispose: { openDispose, toggleDispose },
    disposeSuccess: { openDisposeSuccess, toggleDisposeSuccess },
    activate: { openActivate, toggleActivate },
    activateSuccess: { openActivateSuccess, toggleActivateSuccess },
  } = useModalsSupplies();

  return (
    <CardV2
      header={
        <Flex justify="space-between" align="center">
          <Typography level={4}>Thêm mới hàng hoá</Typography>
          <Flex align="center" gap={10}>
            <ButtonV2 onClick={() => navigate(PATH.SUPPLIES)}>Đóng</ButtonV2>
            <AuthGuard permissionKey='LOGISTICS_SUPPLIES.CREATE'>
              <ButtonV2
                color="blue"
                variant="solid"
                form={FORM.SUPPLIES}
                type="submit"
              >
                Lưu
              </ButtonV2>
            </AuthGuard>
          </Flex>
        </Flex>
      }
    >
      <SuppliesForm type="create" toggleSuccess={toggleCreateSuccess} />

      <SuppliesCreateSuccessModals
        isOpen={openCreateSuccess}
        toggle={toggleCreateSuccess}
      />
    </CardV2>
  );
};

export default SuppliesCreate;
