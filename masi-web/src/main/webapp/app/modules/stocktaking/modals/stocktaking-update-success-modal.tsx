import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { StocktakingContext } from '../stocktaking-provider';

const StocktakingUpdateSuccessModal = () => {
  const { isOpenUpdateSuccess, toggleUpdateSuccess } =
    useContext(StocktakingContext);

  const navigate = useNavigate();

  const onOk = () => {
    toggleUpdateSuccess();
    navigate(PATH.STOCKTAKING);
  };

  return (
    <Modal
      isOpen={isOpenUpdateSuccess}
      toggle={toggleUpdateSuccess}
      cancel={false}
      titleHeader={`Cập nhật kiểm kê thành công`}
      onOk={onOk}
    >
      <Typography level={4}>{`Bạn đã cập nhật kiểm kê thành công`}</Typography>
    </Modal>
  );
};

export default StocktakingUpdateSuccessModal;
