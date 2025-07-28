import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface ILogisticsMaterialProposalApproveSignSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const LogisticsMaterialProposalApproveSignSuccessModals = (props: ILogisticsMaterialProposalApproveSignSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="logistics-material-proposal-approve-sign-success-modals" cancel={false}>
      <Typography level={3}>Xét duyệt thành công</Typography>
      <Typography level={4}>Bạn đã xét duyệt đề xuất vật tư thành công</Typography>
    </Modal>
  );
};

export default LogisticsMaterialProposalApproveSignSuccessModals;
