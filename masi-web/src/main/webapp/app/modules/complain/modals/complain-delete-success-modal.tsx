import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { ComplainContext } from '../complain-provider';

const ComplainDeleteSuccessModal = ({ directUrl }: { directUrl?: string }) => {
  const navigate = useNavigate();

  const { isOpenDeleteSuccess, toggleDeleteSuccess } =
    useContext(ComplainContext);

  return (
    <Modal
      isOpen={isOpenDeleteSuccess}
      toggle={() => {
        toggleDeleteSuccess();
        if (directUrl) navigate(directUrl);
      }}
      cancel={false}
      titleHeader={`Xóa khiếu nại thành công`}
    >
      <Typography level={4}>{`Bạn đã xóa khiếu nại thành công`}</Typography>
    </Modal>
  );
};

export default ComplainDeleteSuccessModal;
