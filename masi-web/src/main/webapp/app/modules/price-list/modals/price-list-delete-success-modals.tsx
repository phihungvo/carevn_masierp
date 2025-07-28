import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IPriceListDeleteSuccessModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess?: () => void;
}

const PriceListDeleteSuccessModals = (props: IPriceListDeleteSuccessModals) => {
  const { isOpen, toggle, toggleSuccess } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-pl-success"
      onOk={toggleSuccess}
      cancel={false}
      titleHeader='Xóa bảng báo giá thành công'
    >
      <Typography level={4}>Bạn đã xóa bảng báo giá thành công</Typography>
    </Modal>
  );
};

export default PriceListDeleteSuccessModals;
