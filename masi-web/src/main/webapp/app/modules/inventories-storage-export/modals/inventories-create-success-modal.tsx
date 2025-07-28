import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import { InventoriesWarehouse } from 'app/shared/model/enumerations/warehouse.model';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { useSearchParams } from 'react-router-dom';
import { InventoriesExportStorageContext } from '../inventories-storage-export-provider';

const InventoriesCreateSuccessModal = () => {
  const { isOpenCreateSuccess, toggleCreateSuccess } = useContext(
    InventoriesExportStorageContext,
  );

  const navigate = useNavigate();

  const [searchParams] = useSearchParams();
  const warehouseImportType = searchParams.get('warehouse');

  const titlePrefix =
    warehouseImportType ===
    (InventoriesWarehouse?.WAREHOUSE_DEPRECIATION_EXPORT as string)
      ? 'TS - CCDC '
      : '';

  const onOk = () => {
    toggleCreateSuccess();
    navigate(PATH.INVENTORIES_STORAGE_EXPORT + location.search);
  };

  return (
    <Modal
      isOpen={isOpenCreateSuccess}
      toggle={toggleCreateSuccess}
      cancel={false}
      titleHeader={`Tạo mới đơn xuất kho ${titlePrefix}thành công`}
      onOk={onOk}
    >
      <Typography
        level={4}
      >{`Bạn đã tạo mới đơn xuất kho ${titlePrefix}thành công`}</Typography>
    </Modal>
  );
};

export default InventoriesCreateSuccessModal;
