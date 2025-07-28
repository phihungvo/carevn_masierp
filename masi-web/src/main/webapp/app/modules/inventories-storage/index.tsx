import React from 'react';
import InventoriesStorageProvider from './inventories-storage-provider';

import InventoriesDashboard from './inventories-dashboard';

const InventoriesStorage = () => {
  return (
    <InventoriesStorageProvider>
      <InventoriesDashboard />
    </InventoriesStorageProvider>
  );
};

export default InventoriesStorage;
