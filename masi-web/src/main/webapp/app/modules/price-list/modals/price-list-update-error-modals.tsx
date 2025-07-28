import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IPriceListUpdateErrorModals {
  isOpen: boolean;
  toggle: () => void;
}

const PriceListUpdateErrorModals = (props: IPriceListUpdateErrorModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-pl-success"
      cancel={false}
      titleHeader='Cập nhật thất bại'
    >
      <Typography level={4}>Không thể cập nhật bảng báo giá khi trạng thái là “KH từ chối” hoặc “KH đã duyệt”</Typography>
    </Modal>
  );
};

export default PriceListUpdateErrorModals;
