import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IDocumentaryDeleteSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const DocumentaryDeleteSuccessModals = (props: IDocumentaryDeleteSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Xóa công văn thành công'
    >
      <Typography level={4}>Bạn đã xóa công văn thành công</Typography>
    </Modal>
  );
};

export default DocumentaryDeleteSuccessModals;
