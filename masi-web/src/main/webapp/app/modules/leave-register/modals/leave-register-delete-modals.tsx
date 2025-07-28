import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useLeaveRegime from 'app/hooks/use-leave-regime';
import React from 'react';

const { useDeleteLeaveRegime } = useLeaveRegime;

interface ILeaveRegisterDeleteModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord?: string;
  setSelectedRecord?: (record: string) => void;
}

const LeaveRegisterDeleteModals = (props: ILeaveRegisterDeleteModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { mutate, isPending } = useDeleteLeaveRegime(toggle, toggleSuccess);

  const onOk = () => {
    mutate(selectedRecord);
    setSelectedRecord(null);
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      disabledOk={isPending}
      onOk={onOk}
      titleHeader='Xóa đăng ký nghỉ chế độ'
    >
      <Typography level={4}>Bạn muốn xóa đăng ký chế độ này?</Typography>
    </Modal>
  );
};

export default LeaveRegisterDeleteModals;
