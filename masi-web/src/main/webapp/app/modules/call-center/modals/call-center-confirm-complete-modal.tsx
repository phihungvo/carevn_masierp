import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useCallCenter from 'app/hooks/use-call-center';
import { useContext } from 'react';
import { CallCenterContext } from '../call-center-provider';

const { useCompleteCallCenterMutation } = useCallCenter;

const CallCenterConfirmCompleteModal = () => {
  const {
    isOpenComplete,
    toggleComplete,
    selectedRecord,
    setSelectedRecord,
    toggleCompleteSuccess,
  } = useContext(CallCenterContext);

  const { mutate, isPending } = useCompleteCallCenterMutation();

  const onOk = () => {
    mutate(selectedRecord, {
      onSuccess: () => {
        setSelectedRecord(null);
        toggleComplete();
        toggleCompleteSuccess();
      },
    });
  };

  return (
    <Modal
      isOpen={isOpenComplete}
      toggle={toggleComplete}
      okText="Xác nhận"
      onOk={onOk}
      loadingOk={isPending}
      titleHeader="Xác nhận hoàn thành cuộc gọi"
    >
      <Typography level={4}>
        {`Bạn có chắc chắn muốn hoàn thành cuộc gọi này không?`}
      </Typography>
    </Modal>
  );
};

export default CallCenterConfirmCompleteModal;
