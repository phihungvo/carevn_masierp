import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useProductionQualityControl from 'app/hooks/use-production-quality-control';

const { usePatchQualityCheckSampleByIdReviewFail } =
  useProductionQualityControl;

interface IQualityCancelModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
}

const QualityCancelModals = (props: IQualityCancelModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord } = props;

  const { mutate, isPending } = usePatchQualityCheckSampleByIdReviewFail(
    selectedRecord,
    toggle,
    toggleSuccess,
  );

  const onOk = () => mutate();

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Xác nhận"
      onOk={onOk}
      disabledOk={isPending}
      titleHeader="Xác nhận hủy mẫu"
    >
      <Typography level={4}>Bạn muốn hủy mẫu này ?</Typography>
    </Modal>
  );
};

export default QualityCancelModals;
