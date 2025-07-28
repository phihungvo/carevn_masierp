import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useConfirmImportInventories } from 'app/hooks/use-inventories';
import { InventoriesWarehouse } from 'app/shared/model/enumerations/warehouse.model';
import { useContext } from 'react';
import { useSearchParams } from 'react-router-dom';
import { InventoriesStorageContext } from '../inventories-storage-provider';

const InventoriesConfirmImport = () => {
  const {
    isOpenConfirm,
    toggleConfirm,
    toggleConfirmSuccess,
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

  const { mutate, isPending } = useConfirmImportInventories();

  const onOk = () => {
    mutate(selectedRecord, {
      onSuccess: () => {
        setSelectedRecord(null);
        toggleConfirm();
        toggleConfirmSuccess();
      },
    });
  };

  return (
    <Modal
      isOpen={isOpenConfirm}
      toggle={toggleConfirm}
      okText="Xác nhận"
      onOk={onOk}
      loadingOk={isPending}
      titleHeader="Xác nhận nhập kho"
    >
      <Typography level={4}>
        {`Bạn có chắc chắn muốn xác nhận nhập kho ${titlePrefix}này không?`}
      </Typography>
    </Modal>
  );
};

export default InventoriesConfirmImport;
