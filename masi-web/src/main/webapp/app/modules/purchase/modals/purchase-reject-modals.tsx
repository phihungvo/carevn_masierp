import { zodResolver } from '@hookform/resolvers/zod';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useAppSelector } from 'app/config/store';
import usePurchase from 'app/hooks/use-purchase';
import { PURCHASE_STATUS } from 'app/shared/model/enumerations/purchase.model';
import { PatchPurchaseReviewFormSchema, patchPurchaseReviewSchema } from 'app/validation/purchase.validation';
import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { FormGroup, Label } from 'reactstrap';

const { useGetPurchaseReviewByPurchaseRequestId, usePatchPurchaseReviewMutation } = usePurchase;

interface IPurchaseRejectModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const PurchaseRejectModals = (props: IPurchaseRejectModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const account = useAppSelector(state => state.authentication.account);

  const { control, setValue, watch, handleSubmit, reset } = useForm<PatchPurchaseReviewFormSchema>({
    resolver: zodResolver(patchPurchaseReviewSchema),
  });

  const { data } = useGetPurchaseReviewByPurchaseRequestId(selectedRecord);
  const { mutate, isPending } = usePatchPurchaseReviewMutation(toggle, toggleSuccess);

  const findReviewerId = data?.find(purchaseReview => purchaseReview?.employeeId === account?.id)?.id;

  const onSubmit = (values: PatchPurchaseReviewFormSchema) => {
    mutate({
      id: findReviewerId,
      status: PURCHASE_STATUS.REJECTED,
      approvalStatusNote: values.approvalStatusNote,
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
      okText="Xác nhận"
      className="modals-approve-sign"
      disabledOk={!findReviewerId || isPending}
      loadingOk={isPending}
      onOk={handleSubmit(onSubmit)}
    >
      <Typography level={3}>Từ chối xét duyệt</Typography>

      {!isPending && !findReviewerId ? (
        <p className="text-danger">Bạn không phải là người xét duyệt!</p>
      ) : (
        <Form>
          <FormGroup>
            <Flex justify="space-between">
              <Label for="note">Lý do</Label>
              <span className="word-count">{watch('approvalStatusNote')?.length || 0}/200</span>
            </Flex>
            <FormInput
              rows={5}
              control={control}
              name="approvalStatusNote"
              type="textarea"
              placeholder="Lý do từ chối..."
              onChange={e => e.target.value.length > 200 && setValue('approvalStatusNote', e.target.value.slice(0, 200))}
            />
          </FormGroup>
        </Form>
      )}
    </Modal>
  );
};

export default PurchaseRejectModals;
