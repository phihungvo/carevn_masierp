import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IOfficesImportSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const OfficesImportSuccessModals = (props: IOfficesImportSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-import-offices-success" cancel={false}>
      <Typography level={3}>Tải lên NV thành công</Typography>
      <Typography level={4}>Bạn đã import NV thành công</Typography>
    </Modal>
  );
};

export default OfficesImportSuccessModals;
