import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IContractUpdateErrorModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const ContractUpdateErrorModals = (props: IContractUpdateErrorModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-customer-success"
      cancel={false}
      titleHeader='Không thể cập nhật'
    >
      <Typography level={4}>Không thể cập nhật HĐ khi đã được xét duyệt bao gồm các tình trạng sau: Đã duyệt, Thanh lý, Hủy.</Typography>
    </Modal>
  );
};

export default ContractUpdateErrorModals;
