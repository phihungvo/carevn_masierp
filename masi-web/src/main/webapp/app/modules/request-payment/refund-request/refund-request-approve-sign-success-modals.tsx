import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IRefundRequestApproveSignSuccessModalsProps {
  isOpen: boolean;
  toggle: () => void;
}

const RefundRequestApproveSignSuccessModals = (
  props: IRefundRequestApproveSignSuccessModalsProps,
) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-voucher-request-of-advance-sign-success"
      cancel={false}
      titleHeader="Xét duyệt thành công"
    >
      <Typography level={4}>
        Bạn đã xét duyệt hoàn tạm ứng thành công
      </Typography>
    </Modal>
  );
};

export default RefundRequestApproveSignSuccessModals;
