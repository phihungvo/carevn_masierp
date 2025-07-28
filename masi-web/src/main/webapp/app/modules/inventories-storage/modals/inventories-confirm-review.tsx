import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useReviewInventories } from 'app/hooks/use-inventories';
import { useContext } from 'react';
import { InventoriesStorageContext } from '../inventories-storage-provider';
import { useSearchParams } from 'react-router-dom';
import { InventoriesWarehouse } from 'app/shared/model/enumerations/warehouse.model';

const InventoriesConfirmReview = () => {
  const {
    isOpenReview,
    toggleReview,
    toggleReviewSuccess,
    selectedRecord,
    setSelectedRecord,
  } = useContext(InventoriesStorageContext);

  const [searchParams] = useSearchParams();
  const warehouseImportType = searchParams.get('warehouse');

  const titlePrefix =
    warehouseImportType ===
    (InventoriesWarehouse?.WAREHOUSE_DEPRECIATION_IMPORT as string)
      ? 'TS - CCDC '
      : '';

  const { mutate, isPending } = useReviewInventories();

  const onOk = () => {
    mutate(selectedRecord, {
      onSuccess: () => {
        setSelectedRecord(null);
        toggleReview();
        toggleReviewSuccess();
      },
    });
  };

  return (
    <Modal
      isOpen={isOpenReview}
      toggle={toggleReview}
      okText="Xác nhận"
      onOk={onOk}
      loadingOk={isPending}
      titleHeader="Xác nhận trình duyệt"
    >
      <Typography level={4}>
        {`Bạn có chắc chắn muốn trình duyệt nhập kho ${titlePrefix}này không?`}
      </Typography>
    </Modal>
  );
};

export default InventoriesConfirmReview;
