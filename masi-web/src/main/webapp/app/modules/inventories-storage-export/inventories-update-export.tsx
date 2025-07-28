import InventoriesExportStorageProvider from './inventories-storage-export-provider';
import InventoriesExportUpdateForm from './inventories-update-export-form';

const InventoriesExportUpdate = () => {
  return (
    <InventoriesExportStorageProvider>
      <InventoriesExportUpdateForm />
    </InventoriesExportStorageProvider>
  );
};

export default InventoriesExportUpdate;
