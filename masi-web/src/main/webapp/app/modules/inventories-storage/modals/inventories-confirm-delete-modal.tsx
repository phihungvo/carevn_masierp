import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React, { useContext } from 'react';
import { InventoriesStorageContext } from '../inventories-storage-provider';
import { useDeleteInventories } from 'app/hooks/use-inventories';
import { useSearchParams } from 'react-router-dom';
import { InventoriesWarehouse } from 'app/shared/model/enumerations/warehouse.model';

const InventoriesConfirmDeleteModal = () => {
  const {
    isOpenConfirmDelete,
    toggleConfirmDelete,
    selectedRecord,
    setSelectedRecord,
    toggleDeleteSuccess,
  } = useContext(InventoriesStorageContext);

  const [searchParams] = useSearchParams();
  const warehouseImportType = searchParams.get('warehouse');

  const titlePrefix =
    warehouseImportType ===
    (InventoriesWarehouse?.WAREHOUSE_DEPRECIATION_IMPORT as string)
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
        {`Bạn có chắc chắn muốn hủy nhập kho ${titlePrefix}này không?`}
      </Typography>
    </Modal>
  );
};

export default InventoriesConfirmDeleteModal;
