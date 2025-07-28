import React from 'react';

import Modal from 'app/components/modal/modal';
import Button from 'app/components/button/button';
import { Typography } from 'app/components/typography/typography';

interface ILogisticsMaterialProposalApproveModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleApprove: () => void;
  toggleReject: () => void;
}

const LogisticsMaterialProposalApproveModals = (props: ILogisticsMaterialProposalApproveModalsProps) => {
  const { isOpen, toggle, toggleApprove, toggleReject } = props;

  const handleReject = () => {
    toggle();
    toggleReject();
  };

  const handleApprove = () => {
    toggle();
    toggleApprove();
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      ok={false}
      footer={
        <>
          <Button color="primary" onClick={handleReject}>
            Từ chối
          </Button>
          <Button color="primary" onClick={handleApprove}>
            Đồng ý
          </Button>
        </>
      }
    >
      <Typography level={3}>Xét duyệt đề xuất vật tư</Typography>
      <Typography level={4}>Bạn có muốn xét duyệt đề xuất vật tư này không?</Typography>
    </Modal>
  );
};

export default LogisticsMaterialProposalApproveModals;
