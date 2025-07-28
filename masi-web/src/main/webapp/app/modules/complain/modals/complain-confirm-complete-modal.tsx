import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useCallCenter from 'app/hooks/use-call-center';
import { useContext } from 'react';
import { ComplainContext } from '../complain-provider';

const { useCompleteCallCenterMutation } = useCallCenter;

const ComplainConfirmCompleteModal = () => {
  const {
    isOpenComplete,
    toggleComplete,
    selectedRecord,
    setSelectedRecord,
    toggleCompleteSuccess,
  } = useContext(ComplainContext);

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
      titleHeader="Xác nhận hoàn thành khiếu nại"
    >
      <Typography level={4}>
        {`Bạn có chắc chắn muốn hoàn thành khiếu nại này không?`}
      </Typography>
    </Modal>
  );
};

export default ComplainConfirmCompleteModal;
