import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { SupplierContractsContext } from '../supplier-contracts-storage-provider';

const SupplierContractsRejectSuccessModal = ({
  directUrl,
  isLiquidation,
}: {
  directUrl?: string;
  isLiquidation?: boolean;
}) => {
  const navigate = useNavigate();

  const { isOpenRejectSuccess, toggleRejectSuccess } = useContext(
    SupplierContractsContext,
  );

  const liquidationText = isLiquidation ? 'thanh lý ' : 'hợp đồng mua';

  return (
    <Modal
      isOpen={isOpenRejectSuccess}
      toggle={() => {
        toggleRejectSuccess();
        if (directUrl) navigate(directUrl);
      }}
      cancel={false}
      titleHeader={`Từ chối ${liquidationText} thành công`}
    >
      <Typography
        level={4}
      >{`Bạn đã từ chối ${liquidationText} thành công`}</Typography>
    </Modal>
  );
};

export default SupplierContractsRejectSuccessModal;
