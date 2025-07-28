import { useIsMutating } from '@tanstack/react-query';
import Modal from 'app/components/modal/modal';
import { MUTATION_KEY } from 'app/constants/query-key';
import { FORM } from 'app/shared/model/enumerations/form.model';
import ProductionMaintenanceForm from '../components/production-packages-form';

const { CREATE_PRODUCTION_PACKAGE } = MUTATION_KEY;

interface IProductionPackagesCreateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
}

const ProductionPackagesCreateModals = (
  props: IProductionPackagesCreateModals,
) => {
  const { isOpen, toggle, toggleSuccess } = props;

  const isCreating = useIsMutating({
    mutationKey: [CREATE_PRODUCTION_PACKAGE],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Tạo mới"
      okSubmitForm={FORM.PRODUCTION_PACKAGES}
      disabledOk={!!isCreating}
      titleHeader="Tạo mới đóng gói"
      style={{ width: '600px' }}
    >
      <ProductionMaintenanceForm
        type="create"
        toggle={toggle}
        toggleSuccess={toggleSuccess}
      />
    </Modal>
  );
};

export default ProductionPackagesCreateModals;
