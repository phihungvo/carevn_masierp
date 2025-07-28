import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { StocktakingContext } from '../stocktaking-provider';

const StocktakingCreateSuccessModal = () => {
  const { isOpenCreateSuccess, toggleCreateSuccess } =
    useContext(StocktakingContext);

  const navigate = useNavigate();

  const onOk = () => {
    toggleCreateSuccess();
    navigate(PATH.STOCKTAKING);
  };

  return (
    <Modal
      isOpen={isOpenCreateSuccess}
      toggle={toggleCreateSuccess}
      cancel={false}
      titleHeader={`Tạo mới kiểm kê thành công`}
      onOk={onOk}
    >
      <Typography level={4}>{`Bạn đã tạo mới kiểm kê thành công`}</Typography>
    </Modal>
  );
};

export default StocktakingCreateSuccessModal;
