import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IItemUpdateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const ItemUpdateSuccessModals = (props: IItemUpdateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-item-success"
      cancel={false}
      titleHeader='Cập nhật thành công'
    >
      <Typography level={4}>Bạn đã cập nhật vật phẩm thành công</Typography>
    </Modal>
  );
};

export default ItemUpdateSuccessModals;
