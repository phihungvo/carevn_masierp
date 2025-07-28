import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useContracts from 'app/hooks/use-contracts';
import React from 'react';

const { useRecoverContract } = useContracts;

interface IContractsRestoreModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string;
  setSelectedRecord?: (id: string) => void;
}

const ContractsRestoreModals = (props: IContractsRestoreModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { mutate: recover } = useRecoverContract(selectedRecord, toggle, toggleSuccess);

  const onOk = () => {
    recover();
    setSelectedRecord(null);
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      onOk={onOk}
      titleHeader='Phục hồi hợp đồng'
    >
      <Typography level={4}>Bạn có chắc rằng muốn phục hồi hợp đồng này?</Typography>
    </Modal>
  );
};

export default ContractsRestoreModals;
