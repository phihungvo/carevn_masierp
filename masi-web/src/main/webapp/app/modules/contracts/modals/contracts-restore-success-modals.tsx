import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IContractRestoreSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const ContractRestoreSuccessModals = (props: IContractRestoreSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-customer-success"
      cancel={false}
      titleHeader='Phục hồi thành công'
    >
      <Typography level={4}>Bạn đã phục hồi hợp đồng thành công</Typography>
    </Modal>
  );
};

export default ContractRestoreSuccessModals;
