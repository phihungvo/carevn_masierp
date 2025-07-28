import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IContractDeleteSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const ContractDeleteSuccessModals = (props: IContractDeleteSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-customer-success"
      cancel={false}
      titleHeader='Xoá thành công'
    >
      <Typography level={4}>Bạn đã xoá hợp đồng thành công</Typography>
    </Modal>
  );
};

export default ContractDeleteSuccessModals;
