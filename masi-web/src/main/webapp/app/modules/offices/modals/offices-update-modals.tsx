import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import OfficesForm from '../components/offices-form';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { UPDATE_WORKSPACE } = MUTATION_KEY;

interface IOfficesUpdateModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string | null;
  setSelectedRecord: (value: string | null) => void;
  setSelectedRowKeys?: React.Dispatch<React.SetStateAction<string[]>>;
}

const OfficesUpdateModals = (props: IOfficesUpdateModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord, setSelectedRowKeys } = props;

  const isUpdating = useIsMutating({
    mutationKey: [UPDATE_WORKSPACE],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modals-create-offices"
      okText="Cập nhật"
      okSubmitForm={FORM.OFFICES}
      disabledOk={!!isUpdating}
    >
      <Typography level={5}>Cập nhật văn phòng/ nhà máy</Typography>
      <OfficesForm
        type="update"
        toggle={toggle}
        toggleSuccess={toggleSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
        setSelectedRowKeys={setSelectedRowKeys}
      />
    </Modal>
  );
};

export default OfficesUpdateModals;
