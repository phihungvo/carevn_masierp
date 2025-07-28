import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import Modal from 'app/components/modal/modal';
import { useRejectInventories } from 'app/hooks/use-inventories';
import {
  rejectDocumentarySchema,
  RejectDocumentarySchema,
} from 'app/validation/documentary.validation';
import { useContext, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { InventoriesExportStorageContext } from '../inventories-storage-export-provider';

const InventoriesConfirmReject = () => {
  const {
    isOpenReject,
    toggleReject,
    toggleRejectSuccess,
    selectedRecord,
    setSelectedRecord,
  } = useContext(InventoriesExportStorageContext);

  const { mutate, isPending } = useRejectInventories();
  const { control, handleSubmit, reset } = useForm<RejectDocumentarySchema>({
    resolver: zodResolver(rejectDocumentarySchema),
  });

  const onSubmit = (values: RejectDocumentarySchema) => {
    mutate(
      { id: selectedRecord, note: values.rejectNote },
      {
        onSuccess: () => {
          setSelectedRecord(null);
          toggleReject();
          toggleRejectSuccess();
        },
      },
    );
  };

  useEffect(() => {
    !isOpenReject && reset();
  }, [isOpenReject]);

  return (
    <Modal
      isOpen={isOpenReject}
      toggle={toggleReject}
      className="modal-delete-process"
      okText="Xác nhận"
      loadingOk={isPending}
      onOk={handleSubmit(onSubmit)}
      titleHeader="Xác nhận từ chối"
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

export default InventoriesConfirmReject;
