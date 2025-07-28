import { useIsMutating } from '@tanstack/react-query';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { MUTATION_KEY } from 'app/constants/query-key';
import useProductionCommand from 'app/hooks/use-production-command';

const { DELETE_PRODUCTION_PACKAGE } = MUTATION_KEY;

const { useCancelProductionCommand } = useProductionCommand;

interface IManufactureOrderCancelModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const ManufactureOrderCancelModals = (props: IManufactureOrderCancelModals) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } =
    props;

  const { mutateAsync } = useCancelProductionCommand();
  const isDeleting = useIsMutating({
    mutationKey: [DELETE_PRODUCTION_PACKAGE],
  });

  const onOk = () => {
    if (selectedRecord) {
      mutateAsync(selectedRecord)
        .then(() => toggleSuccess())
        .finally(() => {
          toggle();
          setSelectedRecord(null);
        });
      return;
    }
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Xác nhận"
      onOk={onOk}
      disabledOk={!!isDeleting}
      titleHeader="Hủy lệnh sản xuất"
    >
      <Typography level={4}>Bạn muốn hủy lệnh sản xuất này?</Typography>
    </Modal>
  );
};

export default ManufactureOrderCancelModals;
