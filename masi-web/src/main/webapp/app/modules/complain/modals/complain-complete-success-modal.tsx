import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { ComplainContext } from '../complain-provider';

const ComplainCompleteSuccessModal = ({
  directUrl,
}: {
  directUrl?: string;
}) => {
  const navigate = useNavigate();

  const { isOpenCompleteSuccess, toggleCompleteSuccess } =
    useContext(ComplainContext);

  return (
    <Modal
      isOpen={isOpenCompleteSuccess}
      toggle={() => {
        toggleCompleteSuccess();
        if (directUrl) navigate(directUrl);
      }}
      cancel={false}
      titleHeader={`Hoàn thành khiếu nại thành công`}
    >
      <Typography
        level={4}
      >{`Bạn đã hoàn thành khiếu nại thành công`}</Typography>
    </Modal>
  );
};

export default ComplainCompleteSuccessModal;
