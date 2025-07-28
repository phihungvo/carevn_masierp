import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IContractsRejectSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const ContractsRejectSuccessModals = (props: IContractsRejectSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="contracts-reject-success-modals"
      cancel={false}
      titleHeader='Từ chối xét duyệt thành công'
    >
      <Typography level={4}>Bạn đã từ chối xét duyệt Hợp đồng thành công</Typography>
    </Modal>
  );
};

export default ContractsRejectSuccessModals;
