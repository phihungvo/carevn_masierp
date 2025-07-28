import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useReviewInventories } from 'app/hooks/use-inventories';
import { InventoriesWarehouse } from 'app/shared/model/enumerations/warehouse.model';
import { useContext } from 'react';
import { useSearchParams } from 'react-router-dom';
import { InventoriesExportStorageContext } from '../inventories-storage-export-provider';

const InventoriesConfirmReview = () => {
  const {
    isOpenReview,
    toggleReview,
    toggleReviewSuccess,
    selectedRecord,
    setSelectedRecord,
  } = useContext(InventoriesExportStorageContext);

  const [searchParams] = useSearchParams();
  const warehouseImportType = searchParams.get('warehouse');

  const titlePrefix =
    warehouseImportType ===
    (InventoriesWarehouse?.WAREHOUSE_DEPRECIATION_EXPORT as string)
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
        {`Bạn có chắc chắn muốn trình duyệt xuất kho ${titlePrefix}này không?`}
      </Typography>
    </Modal>
  );
};

export default InventoriesConfirmReview;
