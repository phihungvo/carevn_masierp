import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IPriceListApproveInternalSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const PriceListApproveInternalSuccessModals = (props: IPriceListApproveInternalSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-pl-success"
      cancel={false}
      titleHeader='Xét duyệt thành công'
    >
      <Typography level={4}>Bạn đã xét duyệt bảng báo giá thành công</Typography>
    </Modal>
  );
};

export default PriceListApproveInternalSuccessModals;
