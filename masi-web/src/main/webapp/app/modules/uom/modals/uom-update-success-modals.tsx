import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IUomUpdateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const UomUpdateSuccessModals = (props: IUomUpdateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-uom-success"
      cancel={false}
      titleHeader='Cập nhật thành công'
    >
      <Typography level={4}>Bạn đã cập nhật đồng phục thành công</Typography>
    </Modal>
  );
};

export default UomUpdateSuccessModals;
