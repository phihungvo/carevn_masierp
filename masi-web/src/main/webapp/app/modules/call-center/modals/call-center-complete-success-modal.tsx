import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { CallCenterContext } from '../call-center-provider';

const CallCenterCompleteSuccessModal = ({
  directUrl,
}: {
  directUrl?: string;
}) => {
  const navigate = useNavigate();

  const { isOpenCompleteSuccess, toggleCompleteSuccess } =
    useContext(CallCenterContext);

  return (
    <Modal
      isOpen={isOpenCompleteSuccess}
      toggle={() => {
        toggleCompleteSuccess();
        if (directUrl) navigate(directUrl);
      }}
      cancel={false}
      titleHeader={`Hoàn thành cuộc gọi thành công`}
    >
      <Typography
        level={4}
      >{`Bạn đã hoàn thành cuộc gọi thành công`}</Typography>
    </Modal>
  );
};

export default CallCenterCompleteSuccessModal;
