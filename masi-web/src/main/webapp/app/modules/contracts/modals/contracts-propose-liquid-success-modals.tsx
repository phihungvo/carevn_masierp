import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IContractProposeLiquidSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const ContractProposeLiquidSuccessModals = (props: IContractProposeLiquidSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-customer-success"
      cancel={false}
      titleHeader='Đề xuất thanh lý thành công'
    >
      <Typography level={4}>Bạn đã đề xuất thanh lý hợp đồng thành công</Typography>
    </Modal>
  );
};

export default ContractProposeLiquidSuccessModals;
