import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IModalDisposeProductionStandardSuccess {
  isOpen: boolean;
  toggle: () => void;
}

export const ModalDisposeProductionStandardSuccess = ({
  isOpen,
  toggle,
}: IModalDisposeProductionStandardSuccess) => {
  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-dispose-production-standard-success"
      cancel={false}
      titleHeader="Hủy thành công"
    >
      <Typography level={4}>
        Bạn đã hủy kiểm định mức sản xuất thành công
      </Typography>
    </Modal>
  );
};
