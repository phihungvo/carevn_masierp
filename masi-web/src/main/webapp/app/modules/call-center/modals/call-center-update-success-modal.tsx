import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { CallCenterContext } from '../call-center-provider';

const CallCenterUpdateSuccessModal = () => {
  const { isOpenUpdateSuccess, toggleUpdateSuccess } =
    useContext(CallCenterContext);

  const navigate = useNavigate();

  const onOk = () => {
    toggleUpdateSuccess();
    navigate(PATH.CALL_CENTER);
  };

  return (
    <Modal
      isOpen={isOpenUpdateSuccess}
      toggle={toggleUpdateSuccess}
      cancel={false}
      titleHeader={`Cập nhật cuộc gọi thành công`}
      onOk={onOk}
    >
      <Typography level={4}>{`Bạn đã cập nhật cuộc gọi thành công`}</Typography>
    </Modal>
  );
};

export default CallCenterUpdateSuccessModal;
