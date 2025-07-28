import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IPriceListCreateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
  toogleSuccess?: () => void;
}

const PriceListCreateSuccessModals = (props: IPriceListCreateSuccessModals) => {
  const { isOpen, toggle, toogleSuccess } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-in-approve-pl"
      onOk={toogleSuccess}
      cancel={false}
      titleHeader='Tạo mới thành công'
    >
      <Typography level={4}>Bạn đã tạo mới bảng báo giá thành công</Typography>
    </Modal>
  );
};

export default PriceListCreateSuccessModals;
