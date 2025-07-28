import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useProductionQualityControl from 'app/hooks/use-production-quality-control';

const { useDeleteQualityCheckSample } = useProductionQualityControl;

interface IQualityDeleteModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (id: string) => void;
}

const QualityDeleteModals = (props: IQualityDeleteModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } =
    props;

  const { mutate, isPending } = useDeleteQualityCheckSample();

  const onOk = () => {
    mutate(selectedRecord, {
      onSuccess: () => {
        toggle();
        toggleSuccess();
      },
    });
    setSelectedRecord(null);
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Xác nhận"
      onOk={onOk}
      disabledOk={isPending}
      titleHeader="Xóa kiểm tra chất lượng"
    >
      <Typography level={4}>Bạn muốn xóa kiểm tra chất lượng này ?</Typography>
    </Modal>
  );
};

export default QualityDeleteModals;
