import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useCallCenter from 'app/hooks/use-call-center';
import { useContext } from 'react';
import { CallCenterContext } from '../call-center-provider';

const { useCloseCallCenterMutation } = useCallCenter;

const CallCenterConfirmCloseModal = () => {
  const {
    isOpenClose,
    toggleClose,
    selectedRecord,
    setSelectedRecord,
    toggleCloseSuccess,
  } = useContext(CallCenterContext);

  const { mutate, isPending } = useCloseCallCenterMutation();

  const onOk = () => {
    mutate(selectedRecord, {
      onSuccess: () => {
        setSelectedRecord(null);
        toggleClose();
        toggleCloseSuccess();
      },
    });
  };

  return (
    <Modal
      isOpen={isOpenClose}
      toggle={toggleClose}
      okText="Xác nhận"
      onOk={onOk}
      loadingOk={isPending}
      titleHeader="Xác nhận đóng cuộc gọi"
    >
      <Typography level={4}>
        {`Bạn có chắc chắn muốn đóng cuộc gọi này không?`}
      </Typography>
    </Modal>
  );
};

export default CallCenterConfirmCloseModal;
