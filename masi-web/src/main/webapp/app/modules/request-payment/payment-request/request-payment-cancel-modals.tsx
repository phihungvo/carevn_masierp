import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import usePaymentRequest from 'app/hooks/use-payment-request';

const { useCancelPaymentRequest } = usePaymentRequest;

interface IRequestPaymentCancelModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
}

const RequestPaymentCancelModals = (
  props: IRequestPaymentCancelModalsProps,
) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord } = props;

  const { mutate, isPending } = useCancelPaymentRequest();

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
      className="modal-voucher-request-payment-cancel"
      okText="Xác nhận"
      onOk={onOk}
      disabledOk={isPending}
      titleHeader="Hủy đề nghị thanh toán"
    >
      <Typography level={4}>Bạn muốn hủy đề nghị thanh toán này ?</Typography>
    </Modal>
  );
};

export default RequestPaymentCancelModals;
