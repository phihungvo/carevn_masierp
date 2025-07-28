import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import Modal from 'app/components/modal/modal';
import useDocumentary from 'app/hooks/use-documentary';
import { rejectDocumentarySchema, RejectDocumentarySchema } from 'app/validation/documentary.validation';
import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';

const { usePatchDocumentaryReviewRefusalMutation } = useDocumentary;

interface IDocumentaryRejectModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const DocumentaryRejectModals = (props: IDocumentaryRejectModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { control, handleSubmit, reset } = useForm<RejectDocumentarySchema>({
    resolver: zodResolver(rejectDocumentarySchema),
  });

  const onOkSuccess = () => {
    toggle();
    toggleSuccess();
  };

  const { mutate, isPending } = usePatchDocumentaryReviewRefusalMutation(selectedRecord, onOkSuccess);

  const onSubmit = (values: RejectDocumentarySchema) => {
    mutate({
      rejectNote: values.rejectNote,
    });
    setSelectedRecord(null);
  };

  useEffect(() => {
    !isOpen && reset();
  }, [isOpen]);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-reject-order"
      okText="Xác nhận"
      disabledOk={isPending}
      loadingOk={isPending}
      onOk={handleSubmit(onSubmit)}
      titleHeader='Từ chối xét duyệt'
    >
      <Form onSubmit={handleSubmit(onSubmit)}>
        <Row>
          <Col md={12}>
            <FormInput control={control} name="rejectNote" label="Lý do" type="textarea" />
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default DocumentaryRejectModals;
