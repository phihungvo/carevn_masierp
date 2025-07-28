import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IFactoriesDeleteSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const FactoriesDeleteSuccessModals = (props: IFactoriesDeleteSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-factories-success"
      cancel={false}
      titleHeader="Xóa nhà máy thành công"
    >
      <Typography level={4}>Bạn đã xóa nhà máy thành công</Typography>
    </Modal>
  );
};

export default FactoriesDeleteSuccessModals;
