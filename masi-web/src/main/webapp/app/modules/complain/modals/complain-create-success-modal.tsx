import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { ComplainContext } from '../complain-provider';

const ComplainCreateSuccessModal = () => {
  const { isOpenCreateSuccess, toggleCreateSuccess } =
    useContext(ComplainContext);

  const navigate = useNavigate();

  const onOk = () => {
    toggleCreateSuccess();
    navigate(PATH.COMPLAIN);
  };

  return (
    <Modal
      isOpen={isOpenCreateSuccess}
      toggle={toggleCreateSuccess}
      cancel={false}
      titleHeader={`Tạo mới khiếu nại thành công`}
      onOk={onOk}
    >
      <Typography level={4}>{`Bạn đã tạo mới khiếu nại thành công`}</Typography>
    </Modal>
  );
};

export default ComplainCreateSuccessModal;
