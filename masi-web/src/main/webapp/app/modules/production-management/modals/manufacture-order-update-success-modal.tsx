import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface Props {
  isOpen: boolean;
  toggle: () => void;
}
const ManufactureOrderUpdateSuccessModal = ({ isOpen, toggle }: Props) => {
  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      titleHeader="Cập nhật công đoạn sản xuất thành công"
      cancel={false}
    >
      <Typography level={4}>
        Bạn đã cập nhật công đoạn sản xuất thành công
      </Typography>
    </Modal>
  );
};

export default ManufactureOrderUpdateSuccessModal;
