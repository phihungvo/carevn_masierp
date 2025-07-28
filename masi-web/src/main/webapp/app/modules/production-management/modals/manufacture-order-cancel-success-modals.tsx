import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IManufactureOrderCancelSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const ManufactureOrderCancelSuccessModals = (
  props: IManufactureOrderCancelSuccessModals,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      titleHeader="Hủy lệnh sản xuất thành công"
    >
      <Typography level={4}>Bạn đã hủy lệnh sản xuất thành công</Typography>
    </Modal>
  );
};

export default ManufactureOrderCancelSuccessModals;
