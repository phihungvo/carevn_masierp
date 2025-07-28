import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IPriceListUpdateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
  toogleSuccess?: () => void;
}

const PriceListUpdateSuccessModals = (props: IPriceListUpdateSuccessModals) => {
  const { isOpen, toggle, toogleSuccess } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-pl-success"
      onOk={toogleSuccess}
      cancel={false}
      titleHeader='Cập nhật thành công'
    >
      <Typography level={4}>Bạn đã cập nhật bảng báo giá thành công</Typography>
    </Modal>
  );
};

export default PriceListUpdateSuccessModals;
