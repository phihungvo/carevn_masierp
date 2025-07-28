import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IUomGroupDeleteSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const UomGroupDeleteSuccessModals = (props: IUomGroupDeleteSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-delete-uom-success" cancel={false}>
      <Typography level={3}>Xóa nhóm đơn vị thành công</Typography>
      <Typography level={4}>Bạn đã xóa nhóm đơn vị thành công</Typography>
    </Modal>
  );
};

export default UomGroupDeleteSuccessModals;
