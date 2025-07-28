import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IVoucherRequestOfAdvanceApproveSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const VoucherRequestOfAdvanceApproveSuccessModals = (
  props: IVoucherRequestOfAdvanceApproveSuccessModalsProps,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-voucher-request-of-advance-sign-success"
      cancel={false}
      titleHeader="Trình duyệt thành công"
    >
      <Typography level={4}>
        Bạn đã trình duyệt hoàn tạm ứng thành công
      </Typography>
    </Modal>
  );
};

export default VoucherRequestOfAdvanceApproveSuccessModals;
