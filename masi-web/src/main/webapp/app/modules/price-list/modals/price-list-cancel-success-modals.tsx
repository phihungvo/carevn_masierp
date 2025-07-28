import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IPriceListCancelSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const PriceListCancelSuccessModals = (props: IPriceListCancelSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-pl-success"
      cancel={false}
      titleHeader='Huỷ thành công'
    >
      <Typography level={4}>Bạn đã huỷ bảng báo giá thành công</Typography>
    </Modal>
  );
};

export default PriceListCancelSuccessModals;
