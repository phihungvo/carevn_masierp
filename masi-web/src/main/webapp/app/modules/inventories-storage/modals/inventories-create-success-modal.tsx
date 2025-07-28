import React, { useContext } from 'react';
import { InventoriesStorageContext } from '../inventories-storage-provider';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useNavigate } from 'react-router';
import { PATH } from 'app/constants/path';
import { useSearchParams } from 'react-router-dom';
import { InventoriesWarehouse } from 'app/shared/model/enumerations/warehouse.model';

const InventoriesCreateSuccessModal = () => {
  const { isOpenCreateSuccess, toggleCreateSuccess } = useContext(
    InventoriesStorageContext,
  );

  const navigate = useNavigate();

  const [searchParams] = useSearchParams();
  const warehouseImportType = searchParams.get('warehouse');

  const titlePrefix =
    warehouseImportType ===
    (InventoriesWarehouse?.WAREHOUSE_DEPRECIATION_IMPORT as string)
      ? 'TS - CCDC '
      : '';

  const onOk = () => {
    toggleCreateSuccess();
    navigate(PATH.INVENTORIES_STORAGE + location.search);
  };

  return (
    <Modal
      isOpen={isOpenCreateSuccess}
      toggle={toggleCreateSuccess}
      cancel={false}
      titleHeader={`Tạo mới đơn nhập kho ${titlePrefix}thành công`}
      onOk={onOk}
    >
      <Typography
        level={4}
      >{`Bạn đã tạo mới đơn nhập kho ${titlePrefix}thành công`}</Typography>
    </Modal>
  );
};

export default InventoriesCreateSuccessModal;
