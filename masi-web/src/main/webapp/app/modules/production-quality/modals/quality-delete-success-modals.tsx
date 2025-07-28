import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IQualityDeleteSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const QualityDeleteSuccessModals = (
  props: IQualityDeleteSuccessModalsProps,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      titleHeader="Xóa thành công"
    >
      <Typography level={4}>
        Bạn đã xóa kiểm tra chất lượng thành công
      </Typography>
    </Modal>
  );
};

export default QualityDeleteSuccessModals;
