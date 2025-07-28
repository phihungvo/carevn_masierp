import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IPriceListInternalApproveSuccess {
  isOpen: boolean;
  toggle: () => void;
}

const PriceListInternalApproveSuccess = (props: IPriceListInternalApproveSuccess) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-pl-success"
      cancel={false}
      titleHeader='Gửi bảng báo giá duyệt nội bộ thành công'
    >
      <Typography level={4}>Bạn đã gửi bảng báo giá duyệt nội bộ thành công</Typography>
    </Modal>
  );
};

export default PriceListInternalApproveSuccess;
