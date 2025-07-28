import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import Modal from 'app/components/modal/modal';
import useUniform from 'app/hooks/use-uniform';
import { ORDER_REVIEW_SOLUTION } from 'app/shared/model/enumerations/order.model';
import { UNIFORM_ORDER_STATUS } from 'app/shared/model/enumerations/uniform.model';
import { PatchOrderReviewFormSchema, patchOrderReviewSchema } from 'app/validation/order.validation';
import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';

const { useProcessUniformOrder } = useUniform;

interface IUniformOrdersRejectModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const UniformOrdersRejectModals = (props: IUniformOrdersRejectModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { control, handleSubmit, reset } = useForm<PatchOrderReviewFormSchema>({
    resolver: zodResolver(patchOrderReviewSchema),
    defaultValues: {
      approvalSolution: ORDER_REVIEW_SOLUTION.REJECT,
    },
  });

  const onOkSuccess = () => {
    toggle();
    toggleSuccess();
  };

  const { mutate, isPending } = useProcessUniformOrder(selectedRecord, onOkSuccess);

  const onSubmit = (values: PatchOrderReviewFormSchema) => {
    mutate({
      status: UNIFORM_ORDER_STATUS.REJECTED,
      reason: values.approvalStatusNote,
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
        <FormInput control={control} name="approvalStatusNote" label="Lý do" type="textarea" />
      </Form>
    </Modal>
  );
};

export default UniformOrdersRejectModals;
