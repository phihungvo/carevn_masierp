import Button from 'app/components/button/button';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IUniformOrdersApproveModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleApprove: () => void;
  toggleReject: () => void;
}

const UniformOrdersApproveModals = (props: IUniformOrdersApproveModalsProps) => {
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
      titleHeader='Xét duyệt đơn hàng'
    >
      <Typography level={4}>Bạn có muốn xét duyệt đơn hàng?</Typography>
    </Modal>
  );
};

export default UniformOrdersApproveModals;
