import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IDocumentaryCreateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const DocumentaryCreateSuccessModals = (props: IDocumentaryCreateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Tạo mới thành công'
    >
      <Typography level={4}>Bạn đã tạo mới công văn thành công</Typography>
    </Modal>
  );
};

export default DocumentaryCreateSuccessModals;
