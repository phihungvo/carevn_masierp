import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useDeleteInventories } from 'app/hooks/use-inventories';
import { InventoriesWarehouse } from 'app/shared/model/enumerations/warehouse.model';
import { useContext } from 'react';
import { useSearchParams } from 'react-router-dom';
import { InventoriesExportStorageContext } from '../inventories-storage-export-provider';

const InventoriesConfirmDeleteModal = () => {
  const {
    isOpenConfirmDelete,
    toggleConfirmDelete,
    selectedRecord,
    setSelectedRecord,
    toggleDeleteSuccess,
  } = useContext(InventoriesExportStorageContext);

  const [searchParams] = useSearchParams();
  const warehouseImportType = searchParams.get('warehouse');

  const titlePrefix =
    warehouseImportType ===
    (InventoriesWarehouse?.WAREHOUSE_DEPRECIATION_EXPORT as string)
      ? 'TS - CCDC '
      : '';

  const { mutate, isPending } = useDeleteInventories();

  const onOk = () => {
    mutate(selectedRecord, {
      onSuccess: () => {
        setSelectedRecord(null);
        toggleConfirmDelete();
        toggleDeleteSuccess();
      },
    });
  };

  return (
    <Modal
      isOpen={isOpenConfirmDelete}
      toggle={toggleConfirmDelete}
      className="modal-delete-process"
      okText="Xác nhận"
      onOk={onOk}
      loadingOk={isPending}
      titleHeader="Xác nhận hủy"
    >
      <Typography level={4}>
        {`Bạn có chắc chắn muốn hủy xuất kho ${titlePrefix}này không?`}
      </Typography>
    </Modal>
  );
};

export default InventoriesConfirmDeleteModal;
