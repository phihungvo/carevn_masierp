import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IQualityCancelSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const QualityCancelSuccessModals = (
  props: IQualityCancelSuccessModalsProps,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      titleHeader="Hủy thành công"
    >
      <Typography level={4}>Bạn đã hủy mẫu thành công</Typography>
    </Modal>
  );
};

export default QualityCancelSuccessModals;
