import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useCallCenter from 'app/hooks/use-call-center';
import { useContext } from 'react';
import { ComplainContext } from '../complain-provider';

const { useDeleteCallCenterMutation } = useCallCenter;
const ComplainConfirmDeleteModal = () => {
  const {
    isOpenConfirmDelete,
    toggleConfirmDelete,
    selectedRecord,
    setSelectedRecord,
    toggleDeleteSuccess,
  } = useContext(ComplainContext);

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
        {`Bạn có chắc chắn muốn xóa khiếu nại này không?`}
      </Typography>
    </Modal>
  );
};

export default ComplainConfirmDeleteModal;
