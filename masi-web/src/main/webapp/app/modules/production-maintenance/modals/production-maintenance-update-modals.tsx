import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import ProductionMaintenanceForm from '../components/production-maintenance-form';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';
import { useAppSelector } from 'app/config/store';
import { isHasPermission } from 'app/constants/common';

const { UPDATE_PRODUCTION_MAINTAIN } = MUTATION_KEY;

interface IProductionMaintenanceUpdateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const ProductionMaintenanceUpdateModals = (
  props: IProductionMaintenanceUpdateModals,
) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } =
    props;

  const isUpdating = useIsMutating({
    mutationKey: [UPDATE_PRODUCTION_MAINTAIN],
  });

  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Cập nhật"
      ok={isHasPermission(authorities, 'PRODUCTION_MAINTENANCE.EDIT')}
      okSubmitForm={FORM.PRODUCTION_MAINTENANCE}
      disabledOk={!!isUpdating}
      titleHeader="Cập nhật lô hàng"
      style={{ width: '600px' }}
    >
      <ProductionMaintenanceForm
        type="update"
        toggle={toggle}
        toggleSuccess={toggleSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
    </Modal>
  );
};

export default ProductionMaintenanceUpdateModals;
