import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface ILogisticsMaterialProposalDeleteSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const LogisticsMaterialProposalDeleteSuccessModals = (props: ILogisticsMaterialProposalDeleteSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="logistics-material-proposal-delete-success-modals" cancel={false}>
      <Typography level={3}>Xóa đề xuất vật tư thành công</Typography>
      <Typography level={4}>Bạn đã xóa đề xuất vật tư thành công</Typography>
    </Modal>
  );
};

export default LogisticsMaterialProposalDeleteSuccessModals;
