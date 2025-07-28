import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import ProductionWorkCentersForm from '../components/production-work-centers-form';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useAppSelector } from 'app/config/store';
import { isHasPermission } from 'app/constants/common';

const { UPDATE_WORK_CENTER } = MUTATION_KEY;

interface IProductionWorkCentersUpdateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const ProductionWorkCentersUpdateModals = (
  props: IProductionWorkCentersUpdateModals,
) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } =
    props;

    const authorities = useAppSelector(
      state => state.authentication.account.authorities,
    );

  const isUpdating = useIsMutating({
    mutationKey: [UPDATE_WORK_CENTER],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Cập nhật"
      ok={isHasPermission(authorities, 'PRODUCTION_WORK_CENTERS.EDIT')}
      okSubmitForm={FORM.PRODUCTION_WORK_CENTERS}
      disabledOk={!!isUpdating}
      titleHeader="Cập nhật cụm máy sản xuất"
      style={{ width: '600px' }}
    >
      <ProductionWorkCentersForm
        type="update"
        toggle={toggle}
        toggleSuccess={toggleSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
    </Modal>
  );
};

export default ProductionWorkCentersUpdateModals;
