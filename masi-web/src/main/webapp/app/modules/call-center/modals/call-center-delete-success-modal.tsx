import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { CallCenterContext } from '../call-center-provider';

const CallCenterDeleteSuccessModal = ({
  directUrl,
}: {
  directUrl?: string;
}) => {
  const navigate = useNavigate();

  const { isOpenDeleteSuccess, toggleDeleteSuccess } =
    useContext(CallCenterContext);

  return (
    <Modal
      isOpen={isOpenDeleteSuccess}
      toggle={() => {
        toggleDeleteSuccess();
        if (directUrl) navigate(directUrl);
      }}
      cancel={false}
      titleHeader={`Xóa cuộc gọi thành công`}
    >
      <Typography level={4}>{`Bạn đã xóa cuộc gọi thành công`}</Typography>
    </Modal>
  );
};

export default CallCenterDeleteSuccessModal;
