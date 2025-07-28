import React from 'react';

import Modal from 'app/components/modal/modal';
// import useRecruitment from 'app/hooks/use-recruitment';
import { Typography } from 'app/components/typography/typography';

// const { useDeleteRecruitment } = useRecruitment;

interface IRefundRequestDeleteModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (id: string) => void;
}

const RefundRequestDeleteModals = (props: IRefundRequestDeleteModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } =
    props;

  // const { mutate, isPending } = useDeleteRecruitment(toggle, toggleSuccess);

  const onOk = () => {
    // mutate(selectedRecord);
    setSelectedRecord(null);
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-delete-voucher-request-of-advance"
      okText="Xác nhận"
      onOk={onOk}
      titleHeader="Xóa hoàn tạm ứng"
    >
      <Typography level={4}>Bạn muốn xóa hoàn tạm ứng này ?</Typography>
    </Modal>
  );
};

export default RefundRequestDeleteModals;
