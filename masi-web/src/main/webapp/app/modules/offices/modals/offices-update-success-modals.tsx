import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IOfficesUpdateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const OfficesUpdateSuccessModals = (props: IOfficesUpdateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-create-offices-success" cancel={false}>
      <Typography level={3}>Cập nhât thành công</Typography>
      <Typography level={4}>Bạn đã cập nhật văn phòng/nhà máy thành công</Typography>
    </Modal>
  );
};

export default OfficesUpdateSuccessModals;
