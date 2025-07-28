import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { StocktakingContext } from '../stocktaking-provider';

const StocktakingRejectSuccessModal = ({
  directUrl,
}: {
  directUrl?: string;
}) => {
  const navigate = useNavigate();

  const { isOpenRejectSuccess, toggleRejectSuccess } =
    useContext(StocktakingContext);

  return (
    <Modal
      isOpen={isOpenRejectSuccess}
      toggle={() => {
        toggleRejectSuccess();
        if (directUrl) navigate(directUrl);
      }}
      cancel={false}
      titleHeader={`Từ chối kiểm kê thành công`}
    >
      <Typography level={4}>{`Bạn đã từ chối kiểm kê thành công`}</Typography>
    </Modal>
  );
};

export default StocktakingRejectSuccessModal;
