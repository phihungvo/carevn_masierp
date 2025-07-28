import React from 'react';

import Modal from 'app/components/modal/modal';
import Button from 'app/components/button/button';
import { Typography } from 'app/components/typography/typography';

interface IContractsApproveModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleApprove: () => void;
  toggleReject: () => void;
}

const ContractsApproveModals = (props: IContractsApproveModalsProps) => {
  const { isOpen, toggle, toggleApprove, toggleReject } = props;

  const handleReject = () => {
    toggle();
    toggleReject();
  };

  const handleApprove = () => {
    toggle();
    toggleApprove();
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      ok={false}
      className='contracts-approve-modals'
      footer={
        <>
          <Button color="primary" onClick={handleReject}>
            Từ chối
          </Button>
          <Button color="primary" onClick={handleApprove}>
            Đồng ý
          </Button>
        </>
      }
      titleHeader='Xét duyệt hợp đồng'
    >
      <Typography level={4}>Bạn có muốn xét duyệt hợp đồng?</Typography>
    </Modal>
  );
};

export default ContractsApproveModals;
