import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { StocktakingContext } from '../stocktaking-provider';

const StocktakingDeleteSuccessModal = ({
  directUrl,
}: {
  directUrl?: string;
}) => {
  const navigate = useNavigate();

  const { isOpenDeleteSuccess, toggleDeleteSuccess } =
    useContext(StocktakingContext);

  return (
    <Modal
      isOpen={isOpenDeleteSuccess}
      toggle={() => {
        toggleDeleteSuccess();
        if (directUrl) navigate(directUrl);
      }}
      cancel={false}
      titleHeader={`Hủy kiểm kê thành công`}
    >
      <Typography level={4}>{`Bạn đã huỷ kiểm kê thành công`}</Typography>
    </Modal>
  );
};

export default StocktakingDeleteSuccessModal;
