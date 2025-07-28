import Modal from "app/components/modal/modal";
import useSuppliesRequest from "app/hooks/use-supplies-request";
import { useMemo, useState } from "react";
import { useRequestAcceptDepreciation } from "../../apis/hook";

const {
  useReviewSuppliesRequest
} = useSuppliesRequest;

type Props = {
  isOpen: boolean
  id_detail: string
  toggle?: () => void
}

const RequestAccept = (props: Props) => {
  const { isOpen, id_detail, toggle } = props

  const [step, setStep] = useState<'action' | 'modal'>('action')
  const [status, setStatus] = useState<'init' | 'success' | 'fail'>('init')
  const modalLayout = useMemo(() => {
    switch (status) {
      case 'init':
        return {
          header_title: 'Trình duyệt đề xuất',
          content: 'Bạn có muốn trình duyệt đề xuất này không?',
          okText: 'Xác nhận',
          step: 'modal',
        }
      case 'success':
        return {
          header_title: 'Thành công',
          content: 'Trình duyệt thành công',
          okText: 'Trở về',
          step: 'modal',
        };
      default:
        return {
          header_title: 'Thất bại',
          content: 'Có lỗi xảy ra, vui lòng thử lại sau',
          okText: 'Trở về',
          step: 'modal',
        };
    }
  }, [status]);

  const rejectMutation = useRequestAcceptDepreciation(
    id_detail,
    () => {
      setStatus('success')
      setStep('modal')
    },
    (_) => {
      setStatus('fail')
      setStep('modal')
    }
  )

  const onCloseModal = () => {
    setStatus('init')
    setStep('action')
    toggle()
  }

  const onReject = rejectMutation.mutate

  return (
    <Modal
      isOpen={isOpen}
      toggle={onCloseModal}
      className="modal-reject-order"
      okText={modalLayout.okText}
      disabledOk={rejectMutation.isPending}
      loadingOk={rejectMutation.isPending}
      onOk={step === 'action' ? onReject : onCloseModal}
      titleHeader={modalLayout.header_title}
      cancel={step === 'action'}
    >
      {modalLayout.content}
    </Modal>
  )
}

export default RequestAccept
