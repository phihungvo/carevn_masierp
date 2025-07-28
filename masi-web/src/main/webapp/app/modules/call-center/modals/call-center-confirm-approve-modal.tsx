import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useCallCenter from 'app/hooks/use-call-center';
import { useContext } from 'react';
import { CallCenterContext } from '../call-center-provider';

const { useApproveCallCenterMutation } = useCallCenter;
const CallCenterConfirmApproveModal = () => {
  const {
    isOpenApprove,
    toggleApprove,
    selectedRecord,
    setSelectedRecord,
    toggleApproveSuccess,
  } = useContext(CallCenterContext);

  const { mutate, isPending } = useApproveCallCenterMutation();

  const onOk = () => {
    mutate(selectedRecord, {
      onSuccess: () => {
        setSelectedRecord(null);
        toggleApprove();
        toggleApproveSuccess();
      },
    });
  };

  return (
    <Modal
      isOpen={isOpenApprove}
      toggle={toggleApprove}
      okText="Xác nhận"
      onOk={onOk}
      loadingOk={isPending}
      titleHeader="Xác nhận xử lý cuộc gọi"
    >
      <Typography level={4}>
        {`Bạn có chắc chắn muốn xử lý cuộc gọi này không?`}
      </Typography>
    </Modal>
  );
};

export default CallCenterConfirmApproveModal;
