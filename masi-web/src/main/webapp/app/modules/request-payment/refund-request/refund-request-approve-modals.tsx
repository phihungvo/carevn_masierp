import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import usePaymentRequest from 'app/hooks/use-payment-request';

interface IRefundRequestApproveModalsProps {
  isOpen: boolean;
  selectedRecord: string;
  toggle: () => void;
  toggleSuccess: () => void;
}

const { useSendApprovePaymentRequest } = usePaymentRequest;

const RefundRequestApproveModals = (
  props: IRefundRequestApproveModalsProps,
) => {
  const { isOpen, toggle, selectedRecord, toggleSuccess } = props;

  const { mutate, isPending } = useSendApprovePaymentRequest();

  const onOk = () => {
    mutate(selectedRecord, {
      onSuccess: () => {
        toggle();
        toggleSuccess();
      },
    });
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Xác nhận"
      className="modal-voucher-request-payment-cancel"
      onOk={onOk}
      disabledOk={isPending}
      titleHeader="Trình duyệt hoàn tạm ứng"
    >
      <Typography level={4}>Bạn có muốn trình duyệt hoàn tạm ứng?</Typography>
    </Modal>
  );
};

export default RefundRequestApproveModals;
