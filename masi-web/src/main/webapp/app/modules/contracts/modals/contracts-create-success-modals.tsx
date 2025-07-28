import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IContractCreateSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const ContractCreateSuccessModals = (props: IContractCreateSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-customer-success"
      cancel={false}
      titleHeader='Tạo mới thành công'
    >
      <Typography level={4}>Bạn đã tạo mới hợp đồng thành công</Typography>
    </Modal>
  );
};

export default ContractCreateSuccessModals;
