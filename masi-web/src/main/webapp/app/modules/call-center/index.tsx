import CallCenterDashboard from './call-center-dashboard';
import CallCenterProvider from './call-center-provider';

const CallCenter = () => {
  return (
    <CallCenterProvider>
      <CallCenterDashboard />
    </CallCenterProvider>
  );
};

export default CallCenter;
