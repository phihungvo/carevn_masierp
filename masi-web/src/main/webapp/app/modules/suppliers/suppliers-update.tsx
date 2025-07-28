import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import { useNavigate, useParams } from 'react-router';
import SupplierForm from './components/supplier-form';
import useSupplier from 'app/hooks/use-supplier';
import { useModalsSupplier } from 'app/hooks/use-modals-supplier';
import SupplierCreateSuccessModals from './modals/supplier-create-success-modals';
import SupplierUpdateSuccessModals from './modals/supplier-update-success-modals';

const { useGetSupplierById } = useSupplier;

const SuppliersUpdate = () => {
  const navigate = useNavigate();
  const { id } = useParams();

  const [
    { openCreate, toggleCreate },
    { openCreateSuccess, toggleCreateSuccess },
    { openUpdate, toggleUpdate },
    { openUpdateSuccess, toggleUpdateSuccess },
    { openDelete, toggleDelete },
    { openDeleteSuccess, toggleDeleteSuccess },
    { openFilter, toggleFilter },
    { openDetail, toggleDetail },
    { openItem, toggleItem },
    { openDispose, toggleDispose },
    { openDisposeSuccess, toggleDisposeSuccess },
    { openActivate, toggleActivate },
    { openActivateSuccess, toggleActivateSuccess },
  ] = useModalsSupplier();

  const { data: detail } = useGetSupplierById(id);

  return (
    <CardV2
      header={
        <Flex justify="space-between" align="center">
          <Typography level={4}>{detail?.code}</Typography>
          <Flex align="center" gap={10}>
            <ButtonV2 onClick={() => navigate(PATH.SUPPLIERS)}>Đóng</ButtonV2>
            <ButtonV2
              color="blue"
              variant="solid"
              form={FORM.SUPPLIER}
              type="submit"
            >
              Lưu
            </ButtonV2>
          </Flex>
        </Flex>
      }
    >
      <SupplierForm
        type="update"
        detail={detail}
        toggleSuccess={toggleUpdateSuccess}
      />

      <SupplierUpdateSuccessModals
        isOpen={openUpdateSuccess}
        toggle={toggleUpdateSuccess}
      />
    </CardV2>
  );
};

export default SuppliersUpdate;
