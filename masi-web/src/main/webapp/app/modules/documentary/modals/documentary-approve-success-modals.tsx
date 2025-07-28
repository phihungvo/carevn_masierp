import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IDocumentaryApproveSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const DocumentaryApproveSuccessModals = (props: IDocumentaryApproveSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Xét duyệt thành công'
    >
      <Typography level={4}>Bạn đã xét duyệt công văn thành công</Typography>
    </Modal>
  );
};

export default DocumentaryApproveSuccessModals;
