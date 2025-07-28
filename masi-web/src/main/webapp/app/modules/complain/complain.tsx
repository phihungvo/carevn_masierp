import ComplainDashboard from './complain-dashboard';
import ComplainProvider from './complain-provider';

export const Complain = () => {
  return (
    <ComplainProvider>
      <ComplainDashboard />
    </ComplainProvider>
  );
};
