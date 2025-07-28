import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IPriceListDeleteErrorModals {
  isOpen: boolean;
  toggle: () => void;
}

const PriceListDeleteErrorModals = (props: IPriceListDeleteErrorModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-pl-success"
      cancel={false}
      titleHeader='Xoá đơn thất bại'
    >
      <Typography level={4}>Không thể xóa BPTCP khi trạng thái là “KH từ chối” hoặc “KH đã duyệt”</Typography>
    </Modal>
  );
};

export default PriceListDeleteErrorModals;
