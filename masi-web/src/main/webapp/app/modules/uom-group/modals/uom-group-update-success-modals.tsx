import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IUomGroupUpdateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const UomGroupUpdateSuccessModals = (props: IUomGroupUpdateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-create-uom-success" cancel={false}>
      <Typography level={3}>Cập nhật thành công</Typography>
      <Typography level={4}>Bạn đã cập nhật nhóm đồng phục thành công</Typography>
    </Modal>
  );
};

export default UomGroupUpdateSuccessModals;
