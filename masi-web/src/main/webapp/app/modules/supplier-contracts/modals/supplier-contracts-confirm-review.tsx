import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import {
  useReviewLiquidationSupplierContracts,
  useReviewSupplierContracts,
} from 'app/hooks/use-supplier-contract';
import { useContext } from 'react';
import { SupplierContractsContext } from '../supplier-contracts-storage-provider';

const SupplierContractsConfirmReview = (props: { isLiquidation?: boolean }) => {
  const {
    isOpenReview,
    toggleReview,
    toggleReviewSuccess,
    selectedRecord,
    setSelectedRecord,
  } = useContext(SupplierContractsContext);
  const { isLiquidation } = props;

  const { mutate, isPending } = useReviewSupplierContracts();
  const { mutate: mutateLiq, isPending: isPendingLiq } =
    useReviewLiquidationSupplierContracts();

  const onOk = () => {
    const onSuccess = () => {
      setSelectedRecord(null);
      toggleReview();
      toggleReviewSuccess();
    };

    if (!isLiquidation) mutate(selectedRecord, { onSuccess: onSuccess });
    else mutateLiq(selectedRecord, { onSuccess: onSuccess });
  };

  const liquidationText = isLiquidation ? 'thanh lý ' : 'hợp đồng mua';

  return (
    <Modal
      isOpen={isOpenReview}
      toggle={toggleReview}
      okText="Xác nhận"
      onOk={onOk}
      loadingOk={isPending || isPendingLiq}
      titleHeader={`Xác nhận trình duyệt ${liquidationText}`}
    >
      <Typography level={4}>
        {`Bạn có chắc chắn muốn trình duyệt ${liquidationText} này không?`}
      </Typography>
    </Modal>
  );
};

export default SupplierContractsConfirmReview;
