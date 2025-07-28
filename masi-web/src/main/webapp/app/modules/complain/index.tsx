import ComplainDashboard from './complain-dashboard';
import ComplainProvider from './complain-provider';

const SupplierContract = () => {
  return (
    <ComplainProvider>
      <ComplainDashboard />
    </ComplainProvider>
  );
};

export default SupplierContract;
