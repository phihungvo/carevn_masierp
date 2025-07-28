import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IContractApproveLiquidSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const ContractApproveLiquidSuccessModals = (props: IContractApproveLiquidSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-customer-success"
      cancel={false}
      titleHeader='Xét duyệt thành công'
    >
      <Typography level={4}>Bạn đã xét duyệt thanh lý hợp đồng thành công</Typography>
    </Modal>
  );
};

export default ContractApproveLiquidSuccessModals;
