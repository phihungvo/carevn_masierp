import InventoriesExportDashboard from './inventories-export-dashboard';
import InventoriesExportStorageProvider from './inventories-storage-export-provider';

const InventoriesStorageExport = () => {
  return (
    <InventoriesExportStorageProvider>
      <InventoriesExportDashboard />
    </InventoriesExportStorageProvider>
  );
};

export default InventoriesStorageExport;
