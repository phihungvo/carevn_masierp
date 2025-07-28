import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IUomGroupCreateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const UomGroupCreateSuccessModals = (props: IUomGroupCreateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-create-uom-success" cancel={false}>
      <Typography level={3}>Tạo mới thành công</Typography>
      <Typography level={4}>Bạn đã tạo mới nhóm đơn vị thành công</Typography>
    </Modal>
  );
};

export default UomGroupCreateSuccessModals;
