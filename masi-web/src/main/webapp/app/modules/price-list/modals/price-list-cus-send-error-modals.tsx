import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IPriceListCusSendErrorModals {
  isOpen: boolean;
  toggle: () => void;
}

const PriceListCusSendErrorModals = (props: IPriceListCusSendErrorModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-pl-success"
      cancel={false}
      titleHeader='Gửi bảng báo giá thất bại'
    >
      <Typography level={4}>Chỉ có thể gửi cho KH khi bảng báo giá đang ở trạng thái “Đã duyệt nội bộ”</Typography>
    </Modal>
  );
};

export default PriceListCusSendErrorModals;
