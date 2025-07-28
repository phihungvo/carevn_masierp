import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IOfficesCreateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const OfficesCreateSuccessModals = (props: IOfficesCreateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-create-offices-success" cancel={false}>
      <Typography level={3}>Tạo mới thành công</Typography>
      <Typography level={4}>Bạn đã tạo mới văn phòng/nhà máy thành công</Typography>
    </Modal>
  );
};

export default OfficesCreateSuccessModals;
