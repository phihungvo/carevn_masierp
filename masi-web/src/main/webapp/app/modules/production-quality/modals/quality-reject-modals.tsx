import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import Modal from 'app/components/modal/modal';
import { useAppSelector } from 'app/config/store';
import useProductionQualityDisposal from 'app/hooks/use-production-quality-disposal';
import {
  rejectDocumentarySchema,
  RejectDocumentarySchema,
} from 'app/validation/documentary.validation';
import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';

const { usePatchQualityDisposalReviews } = useProductionQualityDisposal;

interface IQualityRejectModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
}

const QualityRejectModals = (props: IQualityRejectModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord } = props;

  const account = useAppSelector(state => state.authentication.account);

  const { control, handleSubmit, reset } = useForm<RejectDocumentarySchema>({
    resolver: zodResolver(rejectDocumentarySchema),
  });

  const onOkSuccess = () => {
    toggle();
    toggleSuccess();
  };

  const { mutate, isPending } = usePatchQualityDisposalReviews();

  const onSubmit = (values: RejectDocumentarySchema) => {
    mutate(
      {
        data: {
          id: selectedRecord,
          reviewerNote: values?.rejectNote,
          reviewer: account.id,
          reviewerApproved: false,
        },
        id: selectedRecord,
      },
      { onSuccess: () => onOkSuccess() },
    );
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
      titleHeader="Từ chối đơn hủy mẫu"
    >
      <Form onSubmit={handleSubmit(onSubmit)}>
        <Row>
          <Col md={12}>
            <FormInput
              control={control}
              name="rejectNote"
              label="Lý do"
              type="textarea"
            />
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default QualityRejectModals;
