import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IProductionPackagesDeleteSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const ProductionPackagesDeleteSuccessModals = (
  props: IProductionPackagesDeleteSuccessModals,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-create-packages-success"
      cancel={false}
      titleHeader="Xóa quản lý đóng gói sản phẩm thành công"
    >
      <Typography level={4}>
        Bạn đã xoá quản lý đóng gói sản phẩm thành công
      </Typography>
    </Modal>
  );
};

export default ProductionPackagesDeleteSuccessModals;
