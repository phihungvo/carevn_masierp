import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface ILogisticsMaterialProposalCreateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
  toogleSuccess?: () => void;
}

const LogisticsMaterialProposalCreateSuccessModals = (props: ILogisticsMaterialProposalCreateSuccessModals) => {
  const { isOpen, toggle, toogleSuccess } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="logistics-material-proposal-create-success-modals" onOk={toogleSuccess} cancel={false}>
      <Typography level={3}>Tạo mới thành công</Typography>
      <Typography level={4}>Bạn đã tạo mới đề xuất vật tư thành công</Typography>
    </Modal>
  );
};

export default LogisticsMaterialProposalCreateSuccessModals;
