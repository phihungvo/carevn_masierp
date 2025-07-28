import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useCallCenter from 'app/hooks/use-call-center';
import { useContext } from 'react';
import { ComplainContext } from '../complain-provider';

const { useApproveCallCenterMutation } = useCallCenter;
const ComplainConfirmApproveModal = () => {
  const {
    isOpenApprove,
    toggleApprove,
    selectedRecord,
    setSelectedRecord,
    toggleApproveSuccess,
  } = useContext(ComplainContext);

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
      titleHeader="Xác nhận xử lý khiếu nại"
    >
      <Typography level={4}>
        {`Bạn có chắc chắn muốn xử lý khiếu nại này không?`}
      </Typography>
    </Modal>
  );
};

export default ComplainConfirmApproveModal;
