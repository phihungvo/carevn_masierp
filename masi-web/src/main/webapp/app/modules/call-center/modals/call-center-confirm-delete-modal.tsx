import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useCallCenter from 'app/hooks/use-call-center';
import { useContext } from 'react';
import { CallCenterContext } from '../call-center-provider';

const { useDeleteCallCenterMutation } = useCallCenter;
const CallCenterConfirmDeleteModal = () => {
  const {
    isOpenConfirmDelete,
    toggleConfirmDelete,
    selectedRecord,
    setSelectedRecord,
    toggleDeleteSuccess,
  } = useContext(CallCenterContext);

  const { mutate, isPending } = useDeleteCallCenterMutation();

  const onOk = () => {
    mutate(selectedRecord, {
      onSuccess: () => {
        setSelectedRecord(null);
        toggleConfirmDelete();
        toggleDeleteSuccess();
      },
    });
  };

  return (
    <Modal
      isOpen={isOpenConfirmDelete}
      toggle={toggleConfirmDelete}
      className="modal-delete-process"
      okText="Xác nhận"
      onOk={onOk}
      loadingOk={isPending}
      titleHeader="Xác nhận xóa"
    >
      <Typography level={4}>
        {`Bạn có chắc chắn muốn xóa cuộc gọi này không?`}
      </Typography>
    </Modal>
  );
};

export default CallCenterConfirmDeleteModal;
