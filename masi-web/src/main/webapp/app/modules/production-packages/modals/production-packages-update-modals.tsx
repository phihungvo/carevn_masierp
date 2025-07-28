import { useIsMutating } from '@tanstack/react-query';
import Modal from 'app/components/modal/modal';
import { MUTATION_KEY } from 'app/constants/query-key';
import { FORM } from 'app/shared/model/enumerations/form.model';
import ProductionMaintenanceForm from '../components/production-packages-form';
import { useAppSelector } from 'app/config/store';
import { isHasPermission } from 'app/constants/common';

const { UPDATE_PRODUCTION_PACKAGE } = MUTATION_KEY;

interface IProductionPackagesUpdateModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const ProductionPackagesUpdateModals = (
  props: IProductionPackagesUpdateModals,
) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } =
    props;

    const authorities = useAppSelector(
      state => state.authentication.account.authorities,
    );

  const isUpdating = useIsMutating({
    mutationKey: [UPDATE_PRODUCTION_PACKAGE],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Cập nhật"
      okSubmitForm={FORM.PRODUCTION_PACKAGES}
      ok={isHasPermission(authorities, 'PRODUCTION_PACKAGES.EDIT')}
      disabledOk={!!isUpdating}
      titleHeader="Cập nhật đóng gói"
      style={{ width: '600px' }}
    >
      <ProductionMaintenanceForm
        type="update"
        toggle={toggle}
        toggleSuccess={toggleSuccess}
        selectedRecord={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
    </Modal>
  );
};

export default ProductionPackagesUpdateModals;
