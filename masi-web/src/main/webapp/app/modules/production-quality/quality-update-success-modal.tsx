import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IQualityUpdateSuccessModals {
  isOpen: boolean;
  toggle: () => void;
}

const QualityUpdateSuccessModals = (props: IQualityUpdateSuccessModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      titleHeader="Cập nhập kiểm tra chất lượng thành công"
    >
      <Typography level={4}>
        Bạn đã cập nhập kiểm tra chất lượng thành công
      </Typography>
    </Modal>
  );
};

export default QualityUpdateSuccessModals;
