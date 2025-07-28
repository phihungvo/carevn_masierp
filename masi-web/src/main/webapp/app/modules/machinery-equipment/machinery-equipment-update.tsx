import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import { useNavigate, useParams } from 'react-router';
import MachineryEquipmentForm from './components/machinery-equipment-form';
import MachineryEquipmentUpdateSuccessModals from './modals/machinery-equipment-update-success-modals';
import { useModalsMachineryEquipment } from 'app/hooks/use-modals-machinery-equipment';
import useInventoriesStorage from 'app/hooks/use-inventories-storage';
import AuthGuard from 'app/components/guards/auth-guard';

const { useGetInventoriesStorageByIdQuery } = useInventoriesStorage;

const MachineryEquipmentUpdate = () => {
  const navigate = useNavigate();
  const { id } = useParams();

  const {
    updateSuccess: { openUpdateSuccess, toggleUpdateSuccess },
  } = useModalsMachineryEquipment();

  const { data: detail } = useGetInventoriesStorageByIdQuery(id);

  return (
    <CardV2
      header={
        <Flex justify="space-between" align="center">
          <Typography level={4}>{detail?.code}</Typography>
          <Flex align="center" gap={10}>
            <ButtonV2 onClick={() => navigate(PATH.MACHINERY_EQUIPMENT)}>Đóng</ButtonV2>
            <AuthGuard permissionKey='TECHNICAL_MACHINERY_EQUIPMENT.EDIT'>
              <ButtonV2
                color="blue"
                variant="solid"
                form={FORM.MACHINERY_EQUIPMENT}
                type="submit"
              >
                Lưu
              </ButtonV2>
            </AuthGuard>
          </Flex>
        </Flex>
      }
    >
      <MachineryEquipmentForm
        type="update"
        toggleSuccess={toggleUpdateSuccess}
        detail={detail}
      />

      <MachineryEquipmentUpdateSuccessModals
        isOpen={openUpdateSuccess}
        toggle={toggleUpdateSuccess}
      />
    </CardV2>
  );
};

export default MachineryEquipmentUpdate;
