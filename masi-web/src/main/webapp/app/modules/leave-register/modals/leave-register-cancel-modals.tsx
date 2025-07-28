import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useLeaveRegime from 'app/hooks/use-leave-regime';
import React from 'react';

const { useCancelLeaveRegime } = useLeaveRegime;

interface ILeaveRegisterCancelModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord?: string;
  setSelectedRecord?: (record: string) => void;
}

const LeaveRegisterCancelModals = (props: ILeaveRegisterCancelModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { mutate, isPending } = useCancelLeaveRegime(toggle, toggleSuccess);

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
      titleHeader='Hủy đăng ký nghỉ chế độ'
    >
      <Typography level={4}>Bạn muốn hủy đăng ký chế độ này?</Typography>
    </Modal>
  );
};

export default LeaveRegisterCancelModals;
