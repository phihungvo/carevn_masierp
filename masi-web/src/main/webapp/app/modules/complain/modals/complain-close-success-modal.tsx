import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { ComplainContext } from '../complain-provider';

const ComplainCloseSuccessModal = ({ directUrl }: { directUrl?: string }) => {
  const navigate = useNavigate();

  const { isOpenCloseSuccess, toggleCloseSuccess } =
    useContext(ComplainContext);

  return (
    <Modal
      isOpen={isOpenCloseSuccess}
      toggle={() => {
        toggleCloseSuccess();
        if (directUrl) navigate(directUrl);
      }}
      cancel={false}
      titleHeader={`Đóng khiếu nại thành công`}
    >
      <Typography level={4}>{`Bạn đã đóng khiếu nại thành công`}</Typography>
    </Modal>
  );
};

export default ComplainCloseSuccessModal;
