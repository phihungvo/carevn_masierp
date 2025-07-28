import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IContractRejectLiquidSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const ContractRejectLiquidSuccessModals = (props: IContractRejectLiquidSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-customer-success"
      cancel={false}
      titleHeader='Từ chối thành công'
    >
      <Typography level={4}>Bạn đã từ chối thanh lý hợp đồng thành công</Typography>
    </Modal>
  );
};

export default ContractRejectLiquidSuccessModals;
