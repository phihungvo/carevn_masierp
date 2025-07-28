import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IDocumentaryUpdateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const DocumentaryUpdateSuccessModals = (props: IDocumentaryUpdateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Cập nhật thành công'
    >
      <Typography level={4}>Bạn đã cập nhật công văn thành công</Typography>
    </Modal>
  );
};

export default DocumentaryUpdateSuccessModals;
