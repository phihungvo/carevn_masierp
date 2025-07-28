import SupplierContractsDashboard from './supplier-contracts-dashboard';
import SupplierContractsProvider from './supplier-contracts-storage-provider';

const SupplierContract = () => {
  return (
    <SupplierContractsProvider>
      <SupplierContractsDashboard />
    </SupplierContractsProvider>
  );
};

export default SupplierContract;
