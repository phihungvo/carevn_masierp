import Modal from "app/components/modal/modal";
import { useMemo, useState } from "react";
import { useCancelDepreciation } from "../../apis/hook";

type Props = {
  isOpen: boolean
  id_detail: string
  toggle?: () => void
}

const Cancel = (props: Props) => {
  const { isOpen, id_detail, toggle } = props

  const [step, setStep] = useState<'action' | 'modal'>('action')
  const [status, setStatus] = useState<'init' | 'success' | 'fail'>('init')
  const modalLayout = useMemo(() => {
    switch (status) {
      case 'init':
        return {
          header_title: 'Hủy phê duyệt',
          content: 'Bạn có chắc chắn rằng muốn hủy phê duyệt này không?',
          okText: 'Xác nhận',
          cancelText: 'Hủy',
          step: 'modal',
        }
      case 'success':
        return {
          header_title: 'Thành công',
          content: 'Hủy phê duyệt thành công',
          okText: 'Đồng ý',
          step: 'modal',
        }
      default:
        return {
          header_title: 'Thất bại',
          content: 'Có lỗi xảy ra, vui lòng thử lại sau',
          okText: 'Đồng ý',
          step: 'modal',
        }
    }
  }, [status]);

  const rejectMutation = useCancelDepreciation(
    id_detail,
    () => {
      setStatus('success')
      setStep('modal')
    },
    () => {
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
      cancelText={modalLayout.cancelText}
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

export default Cancel
