import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IQualityCreateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const QualityCreateSuccessModals = (props: IQualityCreateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      titleHeader="Tạo kiểm tra chất lượng thành công"
    >
      <Typography level={4}>
        Bạn đã tạo kiểm tra chất lượng thành công
      </Typography>
    </Modal>
  );
};

export default QualityCreateSuccessModals;
