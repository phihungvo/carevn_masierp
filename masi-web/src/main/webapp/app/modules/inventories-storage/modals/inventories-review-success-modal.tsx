import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useContext } from 'react';
import { InventoriesStorageContext } from '../inventories-storage-provider';
import { useNavigate } from 'react-router';
import { useSearchParams } from 'react-router-dom';
import { InventoriesWarehouse } from 'app/shared/model/enumerations/warehouse.model';

const InventoriesReviewSuccessModal = ({
  directUrl,
}: {
  directUrl?: string;
}) => {
  const navigate = useNavigate();

  const [searchParams] = useSearchParams();
  const warehouseImportType = searchParams.get('warehouse');

  const titlePrefix =
    warehouseImportType ===
    (InventoriesWarehouse?.WAREHOUSE_DEPRECIATION_IMPORT as string)
      ? 'TS - CCDC '
      : '';

  const { isOpenReviewSuccess, toggleReviewSuccess } = useContext(
    InventoriesStorageContext,
  );

  return (
    <Modal
      isOpen={isOpenReviewSuccess}
      toggle={() => {
        toggleReviewSuccess();
        if (directUrl) navigate(directUrl);
      }}
      cancel={false}
      titleHeader={`Trình duyệt nhập kho ${titlePrefix}thành công`}
    >
      <Typography
        level={4}
      >{`Bạn đã trình duyệt nhập kho ${titlePrefix}thành công`}</Typography>
    </Modal>
  );
};

export default InventoriesReviewSuccessModal;
