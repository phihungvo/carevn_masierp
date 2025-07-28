import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { SupplierContractsContext } from '../supplier-contracts-storage-provider';

const SupplierContractsApproveSuccessModal = ({
  directUrl,
  isLiquidation,
}: {
  directUrl?: string;
  isLiquidation?: boolean;
}) => {
  const navigate = useNavigate();

  const { isOpenApproveSuccess, toggleApproveSuccess } = useContext(
    SupplierContractsContext,
  );

  const liquidationText = isLiquidation ? 'thanh lý' : 'hợp đồng mua';

  return (
    <Modal
      isOpen={isOpenApproveSuccess}
      toggle={() => {
        toggleApproveSuccess();
        if (directUrl) navigate(directUrl);
      }}
      cancel={false}
      titleHeader={`Xét duyệt ${liquidationText} thành công`}
    >
      <Typography level={4}>
        {`Bạn đã xét duyệt ${liquidationText} thành công`}
      </Typography>
    </Modal>
  );
};

export default SupplierContractsApproveSuccessModal;
