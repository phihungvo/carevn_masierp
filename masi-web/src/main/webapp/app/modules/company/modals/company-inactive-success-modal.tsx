import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { CompanyContext } from '../company-provider';

const CompanyInactiveSuccessModal = ({ directUrl }: { directUrl?: string }) => {
  const navigate = useNavigate();

  const { isOpenInactiveSuccess, toggleInactiveSuccess } =
    useContext(CompanyContext);

  return (
    <Modal
      isOpen={isOpenInactiveSuccess}
      toggle={() => {
        toggleInactiveSuccess();
        if (directUrl) navigate(directUrl);
      }}
      cancel={false}
      titleHeader={`Cập nhật không hoạt động thành công`}
    >
      <Typography
        level={4}
      >{`Bạn đã cập nhật không hoạt động thành công`}</Typography>
    </Modal>
  );
};

export default CompanyInactiveSuccessModal;
