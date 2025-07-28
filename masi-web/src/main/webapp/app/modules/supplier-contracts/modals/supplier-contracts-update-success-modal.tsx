import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { SupplierContractsContext } from '../supplier-contracts-storage-provider';

const SupplierContractsUpdateSuccessModal = () => {
  const { isOpenUpdateSuccess, toggleUpdateSuccess } = useContext(
    SupplierContractsContext,
  );

  const navigate = useNavigate();

  const onOk = () => {
    toggleUpdateSuccess();
    navigate(PATH.SUPPLIER_CONTRACTS);
  };

  return (
    <Modal
      isOpen={isOpenUpdateSuccess}
      toggle={toggleUpdateSuccess}
      cancel={false}
      titleHeader={`Cập nhật hợp đồng mua thành công`}
      onOk={onOk}
    >
      <Typography
        level={4}
      >{`Bạn đã cập nhật hợp đồng mua thành công`}</Typography>
    </Modal>
  );
};

export default SupplierContractsUpdateSuccessModal;
