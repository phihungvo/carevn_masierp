import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IContracMaskFinishedeSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const ContractMaskFinishedSuccessModals = (props: IContracMaskFinishedeSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-customer-success"
      cancel={false}
      titleHeader='Hoàn tất thành công'
    >
      <Typography level={4}>Bạn đã hoàn tất hợp đồng thành công</Typography>
    </Modal>
  );
};

export default ContractMaskFinishedSuccessModals;
