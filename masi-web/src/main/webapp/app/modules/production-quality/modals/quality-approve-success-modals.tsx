import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IQualityApproveSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const QualityApproveSuccessModals = (
  props: IQualityApproveSuccessModalsProps,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-voucher-request-payment-approval-sign-success"
      cancel={false}
      titleHeader="Duyệt thành công"
    >
      <Typography level={4}>Bạn đã duyệt đơn hủy mẫu thành công</Typography>
    </Modal>
  );
};

export default QualityApproveSuccessModals;
