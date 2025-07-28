import React, { useContext } from 'react';
import { InventoriesStorageContext } from '../inventories-storage-provider';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useNavigate } from 'react-router';
import { PATH } from 'app/constants/path';
import { useSearchParams } from 'react-router-dom';
import { InventoriesWarehouse } from 'app/shared/model/enumerations/warehouse.model';

const InventoriesUpdateSuccessModal = () => {
  const { isOpenUpdateSuccess, toggleUpdateSuccess } = useContext(
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
    toggleUpdateSuccess();
    navigate(PATH.INVENTORIES_STORAGE + location.search);
  };

  return (
    <Modal
      isOpen={isOpenUpdateSuccess}
      toggle={toggleUpdateSuccess}
      cancel={false}
      titleHeader={`Cập nhật đơn nhập kho ${titlePrefix}thành công`}
      onOk={onOk}
    >
      <Typography
        level={4}
      >{`Bạn đã cập nhật đơn nhập kho ${titlePrefix}thành công`}</Typography>
    </Modal>
  );
};

export default InventoriesUpdateSuccessModal;
