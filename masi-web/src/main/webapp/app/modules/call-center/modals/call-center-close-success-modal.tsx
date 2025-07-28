import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { CallCenterContext } from '../call-center-provider';

const CallCenterCloseSuccessModal = ({ directUrl }: { directUrl?: string }) => {
  const navigate = useNavigate();

  const { isOpenCloseSuccess, toggleCloseSuccess } =
    useContext(CallCenterContext);

  return (
    <Modal
      isOpen={isOpenCloseSuccess}
      toggle={() => {
        toggleCloseSuccess();
        if (directUrl) navigate(directUrl);
      }}
      cancel={false}
      titleHeader={`Đóng cuộc gọi thành công`}
    >
      <Typography level={4}>{`Bạn đã đóng cuộc gọi thành công`}</Typography>
    </Modal>
  );
};

export default CallCenterCloseSuccessModal;
