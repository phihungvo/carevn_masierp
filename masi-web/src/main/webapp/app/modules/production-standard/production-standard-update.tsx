import { useIsMutating } from '@tanstack/react-query';
import Modal from 'app/components/modal/modal';
import { MUTATION_KEY } from 'app/constants/query-key';
import useProductionStandard from 'app/hooks/use-production-standard';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { DateObject } from 'react-multi-date-picker';
import { ProductionStandardForm } from './production-standard-form';

const { UPDATE_PRODUCTION_STANDARD } = MUTATION_KEY;
const { useGetProductionStandardById } = useProductionStandard;

// MODAL UPDATE PRODUCTION STANDARD
interface IModalUpdateProductionStandard {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord: string | null;
  setSelectedRecord: (record: string | null) => void;
}

export const ModalUpdateProductionStandard = (
  props: IModalUpdateProductionStandard,
) => {
  const { isOpen, toggle, selectedRecord, setSelectedRecord } = props;

  const { data } = useGetProductionStandardById(selectedRecord);
  const isUpdatingProdStand = useIsMutating({
    mutationKey: [UPDATE_PRODUCTION_STANDARD],
  });

  return (
    <Modal
      disabledOk={!!isUpdatingProdStand}
      isOpen={isOpen}
      toggle={toggle}
      okText="Cập nhật"
      okSubmitForm={FORM.PRODUCTION_STANDARD}
      titleHeader="Cập nhật định mức sản xuất"
      style={{ width: '600px' }}
      ok={false}
    >
      <ProductionStandardForm
        toggle={toggle}
        type="update"
        defaultValues={{
          code: data?.data.code,
          name: data?.data.name,
          dueDate: new DateObject(data?.data.dueDate).format('MM/YYYY'),
          productionPowderQty: data?.data.productionPowderQty.toString(),
          quantity: data?.data.quantity.toString(),
          note: data?.data?.note,
        }}
        id={selectedRecord}
        setSelectedRecord={setSelectedRecord}
      />
    </Modal>
  );
};
