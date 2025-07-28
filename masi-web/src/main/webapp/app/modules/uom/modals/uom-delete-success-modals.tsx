import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IUomDeleteSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const UomDeleteSuccessModals = (props: IUomDeleteSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-delete-uom-success"
      cancel={false}
      titleHeader='Xóa đơn vị thành công'
    >
      <Typography level={4}>Bạn đã xóa đơn vị thành công</Typography>
    </Modal>
  );
};

export default UomDeleteSuccessModals;
