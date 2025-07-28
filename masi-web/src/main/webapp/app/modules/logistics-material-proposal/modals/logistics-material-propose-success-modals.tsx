import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface ILogisticsMaterialProposeSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const LogisticsMaterialProposeSuccessModals = (props: ILogisticsMaterialProposeSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="logistics-material-propose-success-modals" cancel={false}>
      <Typography level={3}>Gửi thành công</Typography>
      <Typography level={4}>Bạn đã gửi yêu cầu xét duyệt thành công</Typography>
    </Modal>
  );
};

export default LogisticsMaterialProposeSuccessModals;
