import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { StocktakingContext } from '../stocktaking-provider';

const StocktakingApproveSuccessModal = ({
  directUrl,
}: {
  directUrl?: string;
}) => {
  const navigate = useNavigate();

  const { isOpenApproveSuccess, toggleApproveSuccess } =
    useContext(StocktakingContext);

  return (
    <Modal
      isOpen={isOpenApproveSuccess}
      toggle={() => {
        toggleApproveSuccess();
        if (directUrl) navigate(directUrl);
      }}
      cancel={false}
      titleHeader="Xét duyệt thành công"
    >
      <Typography level={4}>{`Bạn đã xét duyệt kiểm kê thành công`}</Typography>
    </Modal>
  );
};

export default StocktakingApproveSuccessModal;
