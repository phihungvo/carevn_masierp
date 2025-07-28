import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { StocktakingContext } from '../stocktaking-provider';

const StocktakingReviewSuccessModal = ({
  directUrl,
}: {
  directUrl?: string;
}) => {
  const navigate = useNavigate();

  const { isOpenReviewSuccess, toggleReviewSuccess } =
    useContext(StocktakingContext);

  return (
    <Modal
      isOpen={isOpenReviewSuccess}
      toggle={() => {
        toggleReviewSuccess();
        if (directUrl) navigate(directUrl);
      }}
      cancel={false}
      titleHeader={`Trình duyệt kiểm kê thành công`}
    >
      <Typography
        level={4}
      >{`Bạn đã trình duyệt kiểm kê thành công`}</Typography>
    </Modal>
  );
};

export default StocktakingReviewSuccessModal;
