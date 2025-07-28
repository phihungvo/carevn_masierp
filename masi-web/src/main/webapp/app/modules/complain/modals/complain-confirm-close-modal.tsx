import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useCallCenter from 'app/hooks/use-call-center';
import { useContext } from 'react';
import { ComplainContext } from '../complain-provider';

const { useCloseCallCenterMutation } = useCallCenter;

const ComplainConfirmCloseModal = () => {
  const {
    isOpenClose,
    toggleClose,
    selectedRecord,
    setSelectedRecord,
    toggleCloseSuccess,
  } = useContext(ComplainContext);

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
      titleHeader="Xác nhận đóng khiếu nại"
    >
      <Typography level={4}>
        {`Bạn có chắc chắn muốn đóng khiếu nại này không?`}
      </Typography>
    </Modal>
  );
};

export default ComplainConfirmCloseModal;
