import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import UomGroupForm from '../components/uom-group-form';

const { UPDATE_UOM_GROUP } = MUTATION_KEY;

interface IUomGroupUpdateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string | null;
  setSelectedRecord: (value: string | null) => void;
}

const UomGroupUpdateModals = (props: IUomGroupUpdateModals) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const isUpdating = useIsMutating({
    mutationKey: [UPDATE_UOM_GROUP],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      // className="modals-create-uom"
      fullscreen
      okText="Cập nhật"
      okSubmitForm={FORM.UOM}
      disabledOk={!!isUpdating}
    >
      <Typography level={3}>Cập nhật nhóm đơn vị</Typography>
      <UomGroupForm
        type="update"
        toggle={toggle}
        toggleSuccess={toggleSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
    </Modal>
  );
};

export default UomGroupUpdateModals;
