import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IPriceListRejectSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const PriceListRejectSuccessModals = (props: IPriceListRejectSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-pl-success"
      cancel={false}
      titleHeader='Từ chối thành công'
    >
      <Typography level={4}>Bạn đã từ chối bảng báo giá thành công</Typography>
    </Modal>
  );
};

export default PriceListRejectSuccessModals;
