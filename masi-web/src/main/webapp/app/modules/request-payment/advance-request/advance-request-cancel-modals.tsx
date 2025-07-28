import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import usePaymentRequest from 'app/hooks/use-payment-request';

const { useCancelPaymentRequest } = usePaymentRequest;

interface IAdvanceRequestCancelModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
}

const AdvanceRequestCancelModals = (
  props: IAdvanceRequestCancelModalsProps,
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
      className="modal-voucher-advance-repayment-cancel"
      okText="Xác nhận"
      onOk={onOk}
      disabledOk={isPending}
      titleHeader="Hủy đề nghị tạm ứng"
    >
      <Typography level={4}>Bạn muốn hủy đề nghị tạm ứng này ?</Typography>
    </Modal>
  );
};

export default AdvanceRequestCancelModals;
