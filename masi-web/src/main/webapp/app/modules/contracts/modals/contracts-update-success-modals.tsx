import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IContractUpdateSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const ContractUpdateSuccessModals = (props: IContractUpdateSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-customer-success"
      cancel={false}
      titleHeader='Cập nhật thành công'
    >
      <Typography level={4}>Bạn đã cập nhật hợp đồng thành công</Typography>
    </Modal>
  );
};

export default ContractUpdateSuccessModals;
