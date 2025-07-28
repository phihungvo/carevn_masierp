import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { CompanyContext } from '../company-provider';

const CompanyCreateSuccessModal = () => {
  const { isOpenCreateSuccess, toggleCreateSuccess } =
    useContext(CompanyContext);

  const navigate = useNavigate();

  const onOk = () => {
    toggleCreateSuccess();
    navigate(PATH.COMPANY);
  };

  return (
    <Modal
      isOpen={isOpenCreateSuccess}
      toggle={toggleCreateSuccess}
      cancel={false}
      titleHeader={`Tạo mới công ty thành công`}
      onOk={onOk}
    >
      <Typography level={4}>{`Bạn đã tạo mới công ty thành công`}</Typography>
    </Modal>
  );
};

export default CompanyCreateSuccessModal;
