import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IOfficesPrintModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const OfficesPrintModals = (props: IOfficesPrintModalsProps) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modal-print-offices">
      <Typography level={3}>In hợp đồng lao động</Typography>
      <Typography level={4}>Bạn có chắc rằng muốn in hợp đồng lao động này?</Typography>
    </Modal>
  );
};

export default OfficesPrintModals;
