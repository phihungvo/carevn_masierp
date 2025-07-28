import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IQualityRejectSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const QualityRejectSuccessModals = (
  props: IQualityRejectSuccessModalsProps,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      titleHeader="Từ chối thành công"
    >
      <Typography level={4}>Bạn đã từ chối đơn hủy mẫu thành công</Typography>
    </Modal>
  );
};

export default QualityRejectSuccessModals;
