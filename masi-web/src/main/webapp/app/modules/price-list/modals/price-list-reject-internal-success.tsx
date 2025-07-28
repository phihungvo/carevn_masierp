import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IPriceListRejectInternalSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const PriceListRejectInternalSuccessModals = (props: IPriceListRejectInternalSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="price-list-reject-success-modals"
      cancel={false}
      titleHeader='Từ chối xét duyệt thành công'
    >
      <Typography level={4}>Bạn đã từ chối xét duyệt bảng báo giá thành công</Typography>
    </Modal>
  );
};

export default PriceListRejectInternalSuccessModals;
