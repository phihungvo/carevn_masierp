import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { CallCenterContext } from '../call-center-provider';

const CallCenterCreateSuccessModal = () => {
  const { isOpenCreateSuccess, toggleCreateSuccess } =
    useContext(CallCenterContext);

  const navigate = useNavigate();

  const onOk = () => {
    toggleCreateSuccess();
    navigate(PATH.CALL_CENTER);
  };

  return (
    <Modal
      isOpen={isOpenCreateSuccess}
      toggle={toggleCreateSuccess}
      cancel={false}
      titleHeader={`Tạo mới cuộc gọi thành công`}
      onOk={onOk}
    >
      <Typography level={4}>{`Bạn đã tạo mới cuộc gọi thành công`}</Typography>
    </Modal>
  );
};

export default CallCenterCreateSuccessModal;
