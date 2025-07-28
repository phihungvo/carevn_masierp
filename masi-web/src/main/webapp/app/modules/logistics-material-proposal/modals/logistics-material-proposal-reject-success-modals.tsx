import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface ILogisticsMaterialProposalRejectSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const LogisticsMaterialProposalRejectSuccessModals = (props: ILogisticsMaterialProposalRejectSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="logistics-material-proposal-reject-success-modals" cancel={false}>
      <Typography level={3}>Từ chối xét duyệt thành công</Typography>
      <Typography level={4}>Bạn đã từ chối xét duyệt đề xuất thanh toán thành công</Typography>
    </Modal>
  );
};

export default LogisticsMaterialProposalRejectSuccessModals;
