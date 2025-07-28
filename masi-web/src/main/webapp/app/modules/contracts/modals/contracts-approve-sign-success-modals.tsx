import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IContractsApproveSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const ContractsApproveSuccessModals = (props: IContractsApproveSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      lassName="contracs-approve-sign-success-modals"
      cancel={false}
      titleHeader='Xét duyệt thành công'
    >
      <Typography level={4}>Bạn đã xét duyệt hợp đồng thành công</Typography>
    </Modal>
  );
};

export default ContractsApproveSuccessModals;
