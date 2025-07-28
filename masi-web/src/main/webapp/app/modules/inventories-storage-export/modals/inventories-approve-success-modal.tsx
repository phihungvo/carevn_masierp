import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { InventoriesWarehouse } from 'app/shared/model/enumerations/warehouse.model';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { useSearchParams } from 'react-router-dom';
import { InventoriesExportStorageContext } from '../inventories-storage-export-provider';

const InventoriesApproveSuccessModal = ({
  directUrl,
}: {
  directUrl?: string;
}) => {
  const navigate = useNavigate();

  const [searchParams] = useSearchParams();
  const warehouseImportType = searchParams.get('warehouse');

  const titlePrefix =
    warehouseImportType ===
    (InventoriesWarehouse?.WAREHOUSE_DEPRECIATION_EXPORT as string)
      ? 'TS - CCDC '
      : '';

  const { isOpenApproveSuccess, toggleApproveSuccess } = useContext(
    InventoriesExportStorageContext,
  );

  return (
    <Modal
      isOpen={isOpenApproveSuccess}
      toggle={() => {
        toggleApproveSuccess();
        if (directUrl) navigate(directUrl);
      }}
      cancel={false}
      titleHeader="Xét duyệt thành công"
    >
      <Typography level={4}>
        {`Bạn đã xét duyệt xuất kho ${titlePrefix}thành công`}
      </Typography>
    </Modal>
  );
};

export default InventoriesApproveSuccessModal;
