import { useMutation, UseMutationResult, useQueryClient } from "@tanstack/react-query";
import FormError from "app/components/form/form-error";
import Input from "app/components/input/input";
import { AxiosResponse } from "axios";
import { useMemo, useState } from "react";
import { Col, FormGroup, Label, Row } from "reactstrap";
import ButtonV2 from "../ButtonV2/ButtonV2";
import ModalWrapper from "../ButtonV2/ModalWrapper";

type Props = {
  label?: string
  axiosFn: any
  disabled?: boolean;
  invalidateKey: string;
  renderBtn?: (onToggle: () => void) => JSX.Element
}

const RejectActionBtn = (props: Props) => {
  const { label = 'Hủy', axiosFn, disabled, invalidateKey, renderBtn } = props

  const queryClient = useQueryClient()
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

  const rejectMutation = useMutation({
    mutationFn: axiosFn,
    onSuccess: () => {
      setStatus('success')
      setStep('modal')
      queryClient.invalidateQueries({
        queryKey: [invalidateKey]
      })
    },
    onError: () => {
      setStatus('fail')
      setStep('modal')
    }
  })

  const onCloseModal = () => {
    setStatus('init')
    setReason('')
    setStep('action')
    setError('')
  }

  const onReject = () => {
    if (!reason) {
      setError('Vui lòng nhập lý do từ chối')
      return;
    }
    (rejectMutation as any).mutate({ rejectNote: reason })
  }

  return (
    <ModalWrapper
      renderTarget={({ onToggle }) => (
        renderBtn ? renderBtn(onToggle) : (
          <ButtonV2 onClick={onToggle} disabled={disabled}>
            {label}
          </ButtonV2>
        )
      )}
      okText={modalLayout.okText}
      disabledOk={rejectMutation?.['isPending']}
      loadingOk={rejectMutation?.['isPending']}
      onOk={step === 'action' ? onReject : onCloseModal}
      titleHeader={modalLayout.header_title}
      cancel={step === 'action'}
      renderModal={({ isOpen, onToggle }) => modalLayout.content}
    />
  )
}

export default RejectActionBtn
