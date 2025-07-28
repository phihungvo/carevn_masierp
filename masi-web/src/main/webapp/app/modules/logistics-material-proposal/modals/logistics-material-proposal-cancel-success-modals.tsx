import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface LogisticsMaterialProposalsCancelSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const LogisticsMaterialProposalCancelSuccessModals = (props: LogisticsMaterialProposalsCancelSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="logistics-material-proposal-cancel-success-modals" cancel={false}>
      <Typography level={3}>Huỷ đề xuất vật tư thành công</Typography>
      <Typography level={4}>Bạn đã huỷ đề xuất vật tư thành công</Typography>
    </Modal>
  );
};

export default LogisticsMaterialProposalCancelSuccessModals;
