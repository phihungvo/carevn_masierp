import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { SupplierContractsContext } from '../supplier-contracts-storage-provider';

const SupplierContractsDeleteSuccessModal = ({
  directUrl,
}: {
  directUrl?: string;
}) => {
  const navigate = useNavigate();

  const { isOpenDeleteSuccess, toggleDeleteSuccess } = useContext(
    SupplierContractsContext,
  );

  return (
    <Modal
      isOpen={isOpenDeleteSuccess}
      toggle={() => {
        toggleDeleteSuccess();
        if (directUrl) navigate(directUrl);
      }}
      cancel={false}
      titleHeader={`Hủy hợp đồng mua thành công`}
    >
      <Typography level={4}>{`Bạn đã huỷ hợp đồng mua thành công`}</Typography>
    </Modal>
  );
};

export default SupplierContractsDeleteSuccessModal;
