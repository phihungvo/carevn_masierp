import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { ComplainContext } from '../complain-provider';

const ComplainUpdateSuccessModal = () => {
  const { isOpenUpdateSuccess, toggleUpdateSuccess } =
    useContext(ComplainContext);

  const navigate = useNavigate();

  const onOk = () => {
    toggleUpdateSuccess();
    navigate(PATH.COMPLAIN);
  };

  return (
    <Modal
      isOpen={isOpenUpdateSuccess}
      toggle={toggleUpdateSuccess}
      cancel={false}
      titleHeader={`Cập nhật khiếu nại thành công`}
      onOk={onOk}
    >
      <Typography
        level={4}
      >{`Bạn đã cập nhật khiếu nại thành công`}</Typography>
    </Modal>
  );
};

export default ComplainUpdateSuccessModal;
