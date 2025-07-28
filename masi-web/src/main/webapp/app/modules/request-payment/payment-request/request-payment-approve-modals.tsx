import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import usePaymentRequest from 'app/hooks/use-payment-request';

const { useSendApprovePaymentRequest } = usePaymentRequest;

interface IRequestPaymentApproveModalsProps {
  isOpen: boolean;
  selectedRecord: string;
  toggle: () => void;
  toggleSuccess: () => void;
}

const RequestPaymentApproveModals = (
  props: IRequestPaymentApproveModalsProps,
) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord } = props;

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
      titleHeader="Trình duyệt đề nghị thanh toán"
    >
      <Typography level={4}>
        Bạn có muốn trình duyệt đề nghị thanh toán ?
      </Typography>
    </Modal>
  );
};

export default RequestPaymentApproveModals;
