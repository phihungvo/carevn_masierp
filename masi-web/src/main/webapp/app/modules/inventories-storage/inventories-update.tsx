import InventoriesStorageProvider from './inventories-storage-provider';
import InventoriesUpdateForm from './inventories-update-form';

const InventoriesUpdate = () => {
  return (
    <InventoriesStorageProvider>
      <InventoriesUpdateForm />
    </InventoriesStorageProvider>
  );
};

export default InventoriesUpdate;
