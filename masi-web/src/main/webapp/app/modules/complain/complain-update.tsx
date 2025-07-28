import ComplainProvider from './complain-provider';
import ComplainUpdateForm from './complain-update-form';

const ComplainUpdate = () => {
  return (
    <ComplainProvider>
      <ComplainUpdateForm />
    </ComplainProvider>
  );
};

export default ComplainUpdate;
