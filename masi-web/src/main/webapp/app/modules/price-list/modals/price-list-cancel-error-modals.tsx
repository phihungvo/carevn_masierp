import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IPriceListCancelErrorModals {
  isOpen: boolean;
  toggle: () => void;
}

const PriceListCancelErrorModals = (props: IPriceListCancelErrorModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-pl-success"
      cancel={false}
      titleHeader='Hủy thất bại'
    >
      <Typography level={4}>Không thể hủy bảng báo giá khi trạng thái là “Đã gửi KH”, “KH đã duyệt” hoặc “KH từ chối”</Typography>
    </Modal>
  );
};

export default PriceListCancelErrorModals;
