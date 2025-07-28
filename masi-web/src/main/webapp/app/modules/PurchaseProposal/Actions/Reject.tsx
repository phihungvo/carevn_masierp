import FormError from "app/components/form/form-error";
import Input from "app/components/input/input";
import Modal from "app/components/modal/modal";
import useSuppliesRequest from "app/hooks/use-supplies-request";
import { useMemo, useState } from "react";
import { Col, FormGroup, Label, Row } from "reactstrap";

const {
  useRejectSignSuppliesRequest
} = useSuppliesRequest;

type Props = {
  isOpen: boolean
  id_detail: string
  toggle?: () => void
}

const Reject = (props: Props) => {
  const { isOpen, id_detail, toggle } = props

  const [reason, setReason] = useState<string>('')
  const [step, setStep] = useState<'action' | 'modal'>('action')
  const [status, setStatus] = useState<'init' | 'success' | 'fail'>('init')
  const [error, setError] = useState<string>('')
  const modalLayout = useMemo(() => {
    switch (status) {
      case 'init':
        return {
          header_title: 'Từ chối phê duyệt',
          content: (
            <Row>
              <Col md={12}>
                <FormGroup>
                  <Label>
                    Lý do
                  </Label>
                  <Input
                    type="textarea"
                    placeholder="Nhập lý do từ chối"
                    onChange={e => setReason(e.target.value)}
                  />
                  {error && <FormError message={error} />}
                </FormGroup>
              </Col>
            </Row>
          ),
          okText: 'Xác nhận',
          step: 'action',
        }
      case 'success':
        return {
          header_title: 'Thành công',
          content: 'Từ chối phê duyệt thành công',
          okText: 'Trở về',
          step: 'modal',
        }
      default:
        return {
          header_title: 'Thất bại',
          content: 'Có lỗi xảy ra, vui lòng thử lại sau',
          okText: 'Trở về',
          step: 'modal',
        }
    }
  }, [status, reason, error]);

  const rejectMutation = useRejectSignSuppliesRequest(
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
    setReason('')
    setStep('action')
    setError('')
    toggle()
  }

  const onReject = () => {
    if (!reason) {
      setError('Vui lòng nhập lý do từ chối')
      return;
    }
    rejectMutation.mutate({ rejectNote: reason })
  }

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

export default Reject
