import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IPriceListCusSendSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const PriceListCusSendSuccessModals = (props: IPriceListCusSendSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-in-approve-pl"
      cancel={false}
      titleHeader='Gửi thành công'
    >
      <Typography level={4}>Bạn đã gửi bảng báo giá cho khách hàng thành công</Typography>
    </Modal>
  );
};

export default PriceListCusSendSuccessModals;
