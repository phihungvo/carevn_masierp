import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { CallCenterContext } from '../call-center-provider';

const CallCenterApproveSuccessModal = ({
  directUrl,
}: {
  directUrl?: string;
}) => {
  const navigate = useNavigate();

  const { isOpenApproveSuccess, toggleApproveSuccess } =
    useContext(CallCenterContext);

  return (
    <Modal
      isOpen={isOpenApproveSuccess}
      toggle={() => {
        toggleApproveSuccess();
        if (directUrl) navigate(directUrl);
      }}
      cancel={false}
      titleHeader={`Nhập xử lý cuộc gọi thành công`}
    >
      <Typography
        level={4}
      >{`Bạn đã nhận xử lý cuộc gọi thành công`}</Typography>
    </Modal>
  );
};

export default CallCenterApproveSuccessModal;
