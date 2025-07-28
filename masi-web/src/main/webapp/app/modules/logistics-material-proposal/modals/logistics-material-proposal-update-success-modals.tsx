import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface LogisticsMaterialProposalsUpdateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const LogisticsMaterialProposalUpdateSuccessModals = (props: LogisticsMaterialProposalsUpdateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="logistics-material-proposal-update-success-modals" cancel={false}>
      <Typography level={3}>Cập nhật thành công</Typography>
      <Typography level={4}>Bạn đã cập nhật đề xuất vật tư thành công</Typography>
    </Modal>
  );
};

export default LogisticsMaterialProposalUpdateSuccessModals;
