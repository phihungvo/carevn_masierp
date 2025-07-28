import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IItemDeleteSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const ItemDeleteSuccessModals = (props: IItemDeleteSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-delete-item-success"
      cancel={false}
      titleHeader='Xóa thành công'
    >
      <Typography level={4}>Bạn đã xóa thành công</Typography>
    </Modal>
  );
};

export default ItemDeleteSuccessModals;
