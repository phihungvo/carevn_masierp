import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IContractProposeApproveSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const ContractProposeApproveSuccessModals = (props: IContractProposeApproveSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-customer-success"
      cancel={false}
      titleHeader='Đề xuất xét duyệt thành công'
    >
      <Typography level={4}>Bạn đã đề xuất xét duyệt hợp đồng thành công</Typography>
    </Modal>
  );
};

export default ContractProposeApproveSuccessModals;
