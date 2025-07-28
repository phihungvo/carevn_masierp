import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IContractApproveSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const ContractApproveSuccessModals = (props: IContractApproveSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-customer-success"
      cancel={false}
      titleHeader='Xét duyệt thành công'
    >
      <Typography level={4}>Bạn đã xét duyệt hợp đồng thành công</Typography>
    </Modal>
  );
};

export default ContractApproveSuccessModals;
