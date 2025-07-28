import SupplierContractsStorageProvider from './supplier-contracts-storage-provider';
import SupplierContractsUpdateForm from './supplier-contracts-update-form';

const SupplierContractsUpdate = () => {
  return (
    <SupplierContractsStorageProvider>
      <SupplierContractsUpdateForm />
    </SupplierContractsStorageProvider>
  );
};

export default SupplierContractsUpdate;
