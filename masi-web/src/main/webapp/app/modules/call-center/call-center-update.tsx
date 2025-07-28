import CallCenterProvider from './call-center-provider';
import CallCenterUpdateForm from './call-center-update-form';

const CallCenterUpdate = () => {
  return (
    <CallCenterProvider>
      <CallCenterUpdateForm />
    </CallCenterProvider>
  );
};

export default CallCenterUpdate;
