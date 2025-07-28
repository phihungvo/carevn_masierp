import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import Modal from 'app/components/modal/modal';
import {
  useRejectLiquidationSupplierContracts,
  useRejectSupplierContracts,
} from 'app/hooks/use-supplier-contract';
import {
  rejectDocumentarySchema,
  RejectDocumentarySchema,
} from 'app/validation/documentary.validation';
import { useContext, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { SupplierContractsContext } from '../supplier-contracts-storage-provider';

const SupplierContractsConfirmReject = (props: { isLiquidation?: boolean }) => {
  const {
    isOpenReject,
    toggleReject,
    toggleRejectSuccess,
    selectedRecord,
    setSelectedRecord,
  } = useContext(SupplierContractsContext);
  const { isLiquidation } = props;

  const { mutate, isPending } = useRejectSupplierContracts();
  const { mutate: mutateLiq, isPending: isPendingLiq } =
    useRejectLiquidationSupplierContracts();

  const { control, handleSubmit, reset } = useForm<RejectDocumentarySchema>({
    resolver: zodResolver(rejectDocumentarySchema),
  });

  const onSubmit = (values: RejectDocumentarySchema) => {
    const onSuccess = () => {
      setSelectedRecord(null);
      toggleReject();
      toggleRejectSuccess();
    };
    const body = { id: selectedRecord, note: values.rejectNote };

    if (!isLiquidation) mutate(body, { onSuccess: onSuccess });
    else mutateLiq(body, { onSuccess: onSuccess });
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
      loadingOk={isPending || isPendingLiq}
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

export default SupplierContractsConfirmReject;
