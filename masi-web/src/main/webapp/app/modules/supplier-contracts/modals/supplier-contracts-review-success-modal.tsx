import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { SupplierContractsContext } from '../supplier-contracts-storage-provider';

const SupplierContractsReviewSuccessModal = ({
  directUrl,
  isLiquidation,
}: {
  directUrl?: string;
  isLiquidation?: boolean;
}) => {
  const navigate = useNavigate();

  const { isOpenReviewSuccess, toggleReviewSuccess } = useContext(
    SupplierContractsContext,
  );

  const liquidationText = isLiquidation ? 'thanh lý ' : 'hợp đồng mua';

  return (
    <Modal
      isOpen={isOpenReviewSuccess}
      toggle={() => {
        toggleReviewSuccess();
        if (directUrl) navigate(directUrl);
      }}
      cancel={false}
      titleHeader={`Trình duyệt ${liquidationText} thành công`}
    >
      <Typography
        level={4}
      >{`Bạn đã trình duyệt ${liquidationText} thành công`}</Typography>
    </Modal>
  );
};

export default SupplierContractsReviewSuccessModal;
