import CompanyDashboard from './company-dashboard';
import CompanyProvider from './company-provider';

const Company = () => {
  return (
    <CompanyProvider>
      <CompanyDashboard />
    </CompanyProvider>
  );
};

export default Company;
