import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IItemCreateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const ItemCreateSuccessModals = (props: IItemCreateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-uom-success"
      cancel={false}
      titleHeader='Tạo mới thành công'
    >
      <Typography level={4}>Bạn đã tạo mới vật phẩm thành công</Typography>
    </Modal>
  );
};

export default ItemCreateSuccessModals;
