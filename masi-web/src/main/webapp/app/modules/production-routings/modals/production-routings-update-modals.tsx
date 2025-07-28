import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import ProductionRoutingsForm from '../components/production-routings-form';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useAppSelector } from 'app/config/store';
import { isHasPermission } from 'app/constants/common';

const { UPDATE_PRODUCTION_ROUTING } = MUTATION_KEY;

interface IProductionRoutingsUpdateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const ProductionRoutingsUpdateModals = (
  props: IProductionRoutingsUpdateModals,
) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } =
    props;

  const isUpdating = useIsMutating({
    mutationKey: [UPDATE_PRODUCTION_ROUTING],
  });

  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Cập nhật"
      ok={isHasPermission(authorities, 'PRODUCTION_ROUTINGS.EDIT')}
      okSubmitForm={FORM.PRODUCTION_ROUTINGS}
      disabledOk={!!isUpdating}
      titleHeader="Cập nhật lưu kho"
      style={{ width: '600px' }}
    >
      <ProductionRoutingsForm
        type="update"
        toggle={toggle}
        toggleSuccess={toggleSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
    </Modal>
  );
};

export default ProductionRoutingsUpdateModals;
