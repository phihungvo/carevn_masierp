import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useDeleteStocktaking } from 'app/hooks/use-stocktaking';
import { useContext } from 'react';
import { StocktakingContext } from '../stocktaking-provider';

const StocktakingConfirmDeleteModal = () => {
  const {
    isOpenConfirmDelete,
    toggleConfirmDelete,
    selectedRecord,
    setSelectedRecord,
    toggleDeleteSuccess,
  } = useContext(StocktakingContext);

  const { mutate, isPending } = useDeleteStocktaking();

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
      titleHeader="Xác nhận hủy"
    >
      <Typography level={4}>
        {`Bạn có chắc chắn muốn hủy kiểm kê này không?`}
      </Typography>
    </Modal>
  );
};

export default StocktakingConfirmDeleteModal;
