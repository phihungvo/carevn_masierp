import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface Props {
  isOpen: boolean;
  toggle: () => void;
}
const ManufactureOrderCompleteSuccessModal = ({ isOpen, toggle }: Props) => {
  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      titleHeader="Hoàn thành công đoạn sản xuất thành công"
      cancel={false}
    >
      <Typography level={4}>
        Bạn đã hoàn thành công đoạn sản xuất thành công
      </Typography>
    </Modal>
  );
};

export default ManufactureOrderCompleteSuccessModal;
