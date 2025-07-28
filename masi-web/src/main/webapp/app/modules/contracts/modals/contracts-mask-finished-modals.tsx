import React from 'react';

import Modal from 'app/components/modal/modal';
import useContracts from 'app/hooks/use-contracts';
import { Typography } from 'app/components/typography/typography';

const { usePatchContractMaskFinished } = useContracts;

interface IContractsMaskFinishedModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string;
  setSelectedRecord?: (id: string) => void;
}

const ContractsMaskFinishedModals = (props: IContractsMaskFinishedModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { mutate } = usePatchContractMaskFinished(selectedRecord, toggle, toggleSuccess);

  const onOk = () => {
    mutate();
    setSelectedRecord(null);
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      onOk={onOk}
      titleHeader='Hoàn tất hợp đồng'
    >
      <Typography level={4}>Bạn có chắc rằng muốn hoàn tất hợp đồng này?</Typography>
    </Modal>
  );
};

export default ContractsMaskFinishedModals;
