import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { CompanyContext } from '../company-provider';

const CompanyUpdateSuccessModal = () => {
  const { isOpenUpdateSuccess, toggleUpdateSuccess } =
    useContext(CompanyContext);

  const navigate = useNavigate();

  const onOk = () => {
    toggleUpdateSuccess();
    navigate(PATH.COMPANY);
  };

  return (
    <Modal
      isOpen={isOpenUpdateSuccess}
      toggle={toggleUpdateSuccess}
      cancel={false}
      titleHeader={`Cập nhật công ty thành công`}
      onOk={onOk}
    >
      <Typography level={4}>{`Bạn đã cập nhật công ty thành công`}</Typography>
    </Modal>
  );
};

export default CompanyUpdateSuccessModal;
