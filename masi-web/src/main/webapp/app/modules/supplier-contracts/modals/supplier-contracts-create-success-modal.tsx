import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { SupplierContractsContext } from '../supplier-contracts-storage-provider';

const SupplierContractsCreateSuccessModal = () => {
  const { isOpenCreateSuccess, toggleCreateSuccess } = useContext(
    SupplierContractsContext,
  );

  const navigate = useNavigate();

  const onOk = () => {
    toggleCreateSuccess();
    navigate(PATH.SUPPLIER_CONTRACTS);
  };

  return (
    <Modal
      isOpen={isOpenCreateSuccess}
      toggle={toggleCreateSuccess}
      cancel={false}
      titleHeader={`Tạo mới hợp đồng mua thành công`}
      onOk={onOk}
    >
      <Typography
        level={4}
      >{`Bạn đã tạo mới hợp đồng mua thành công`}</Typography>
    </Modal>
  );
};

export default SupplierContractsCreateSuccessModal;
