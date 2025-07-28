import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { ComplainContext } from '../complain-provider';

const ComplainApproveSuccessModal = ({ directUrl }: { directUrl?: string }) => {
  const navigate = useNavigate();

  const { isOpenApproveSuccess, toggleApproveSuccess } =
    useContext(ComplainContext);

  return (
    <Modal
      isOpen={isOpenApproveSuccess}
      toggle={() => {
        toggleApproveSuccess();
        if (directUrl) navigate(directUrl);
      }}
      cancel={false}
      titleHeader={`Nhận xử lý khiếu nại thành công`}
    >
      <Typography
        level={4}
      >{`Bạn đã nhận xử lý khiếu nại thành công`}</Typography>
    </Modal>
  );
};

export default ComplainApproveSuccessModal;
