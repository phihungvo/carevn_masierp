import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useRecruitment from 'app/hooks/use-recruitment';
import React from 'react';

const { useDeleteRecruitment } = useRecruitment;

interface IRecruitmentDeleteModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (id: string) => void;
}

const RecruitmentDeleteModals = (props: IRecruitmentDeleteModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { mutate, isPending } = useDeleteRecruitment(toggle, toggleSuccess);

  const onOk = () => {
    mutate(selectedRecord);
    setSelectedRecord(null);
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-delete-recruitment-success"
      okText="Xác nhận"
      onOk={onOk}
      disabledOk={isPending}
      titleHeader='Xóa yêu cầu tuyển dụng'
    >
      <Typography level={4}>Bạn muốn xóa yêu cầu tuyển dụng này ?</Typography>
    </Modal>
  );
};

export default RecruitmentDeleteModals;
