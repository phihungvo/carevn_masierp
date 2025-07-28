import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IDocumentaryRejectSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const DocumentaryRejectSuccessModals = (props: IDocumentaryRejectSuccessModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-order-success"
      cancel={false}
      titleHeader='Từ chối xét duyệt thành công'
    >
      <Typography level={4}>Bạn đã từ chối xét duyệt công văn thành công</Typography>
    </Modal>
  );
};

export default DocumentaryRejectSuccessModals;
