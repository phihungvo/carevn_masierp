import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IOfficesDeleteSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const OfficesDeleteSuccessModals = (props: IOfficesDeleteSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-delete-offices-success" cancel={false}>
      <Typography level={3}>Xoá văn phòng/nhà máy thành công</Typography>
      <Typography level={4}>Bạn đã xóa văn phòng/nhà máy thành công</Typography>
    </Modal>
  );
};

export default OfficesDeleteSuccessModals;
