import { useIsMutating } from '@tanstack/react-query';
import Modal from 'app/components/modal/modal';
import { MUTATION_KEY } from 'app/constants/query-key';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { ProductionStandardForm } from './production-standard-form';

const { CREATE_PRODUCTION_STANDARD } = MUTATION_KEY;

interface IModalCreateProductionStandard {
  isOpen: boolean;
  toggle: () => void;
}

export const ModalCreateProductionStandard = (
  props: IModalCreateProductionStandard,
) => {
  const { isOpen, toggle } = props;

  const isCreatingProdStand = useIsMutating({
    mutationKey: [CREATE_PRODUCTION_STANDARD],
  });

  return (
    <Modal
      disabledOk={!!isCreatingProdStand}
      isOpen={isOpen}
      toggle={toggle}
      okText="Lưu"
      okSubmitForm={FORM.PRODUCTION_STANDARD}
      titleHeader="Tạo mới định mức sản xuất"
      style={{ width: '600px' }}
    >
      <ProductionStandardForm toggle={toggle} type="create" />
    </Modal>
  );
};
