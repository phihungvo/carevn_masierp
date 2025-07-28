import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useContext } from 'react';
import { StocktakingContext } from '../stocktaking-provider';
import { useReviewStocktaking } from 'app/hooks/use-stocktaking';

const StocktakingConfirmReview = () => {
  const {
    isOpenReview,
    toggleReview,
    toggleReviewSuccess,
    selectedRecord,
    setSelectedRecord,
  } = useContext(StocktakingContext);

  const { mutate, isPending } = useReviewStocktaking();

  const onOk = () => {
    mutate(selectedRecord, {
      onSuccess: () => {
        setSelectedRecord(null);
        toggleReview();
        toggleReviewSuccess();
      },
    });
  };

  return (
    <Modal
      isOpen={isOpenReview}
      toggle={toggleReview}
      okText="Xác nhận"
      onOk={onOk}
      loadingOk={isPending}
      titleHeader="Xác nhận trình duyệt"
    >
      <Typography level={4}>
        {`Bạn có chắc chắn muốn trình duyệt kiểm kê này không?`}
      </Typography>
    </Modal>
  );
};

export default StocktakingConfirmReview;
