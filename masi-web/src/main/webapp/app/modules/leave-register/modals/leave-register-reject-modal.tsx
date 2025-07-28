import { FormGroup } from 'reactstrap';
import { useForm } from 'react-hook-form';
import React, { useEffect } from 'react';

import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import Modal from 'app/components/modal/modal';
import FormInput from 'app/components/form/form-input';
import useLeaveRegime from 'app/hooks/use-leave-regime';
import { useAppSelector } from 'app/config/store';
import { zodResolver } from '@hookform/resolvers/zod';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { LEAVE_REGIME_STATUS } from 'app/shared/model/enumerations/leave-regime.model';
import { rejectLeaveRegimeSchema, RejectLeaveRegimeSchema } from 'app/validation/leave-regime.validation';

const { useReviewLeaveRegime, useGetLeaveRegimeById } = useLeaveRegime;

interface ILeaveRegisterRejectModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const LeaveRegisterRejectModals = (props: ILeaveRegisterRejectModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const account = useAppSelector(state => state.authentication.account);
  const { control, setValue, watch, handleSubmit, reset } = useForm<RejectLeaveRegimeSchema>({
    resolver: zodResolver(rejectLeaveRegimeSchema),
  });

  const onOkSuccess = () => {
    toggle();
    toggleSuccess();
    setSelectedRecord(null);
    reset();
  };

  const { data } = useGetLeaveRegimeById(selectedRecord);
  const { mutate, isPending } = useReviewLeaveRegime(selectedRecord, onOkSuccess);

  const findReviewerId: string = data?.processLeaveRegimeRequests?.find(element => element?.approver?.id === account?.id)?.approverId;

  const onSubmit = (data: RejectLeaveRegimeSchema) => {
    if (!findReviewerId) {
      toggle();
      return;
    }

    mutate({
      status: LEAVE_REGIME_STATUS.REJECTED,
      reason: data.reason,
    });
  };

  useEffect(() => {
    !isOpen && reset();
  }, [isOpen]);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Xác nhận"
      className="modals-leave-register-reject"
      disabledOk={isPending}
      ok={!!findReviewerId}
      {...(findReviewerId && { okSubmitForm: FORM.LEAVE_REGISTER })}
      titleHeader='Từ chối xét duyệt'
    >
      <Form id={FORM.LEAVE_REGISTER} onSubmit={handleSubmit(onSubmit)}>
        {!findReviewerId ? (
          <p className="text-danger">Bạn không có quyền từ chối xét duyệt!</p>
        ) : (
          <>
            <FormGroup>
              <Flex justify="space-between">
                <span>Lý do</span>
                <span className="word-count">{watch('reason')?.length || 0}/200</span>
              </Flex>
            </FormGroup>
            <FormInput
              rows={5}
              control={control}
              name="reason"
              type="textarea"
              placeholder="Lý do từ chối..."
              onChange={e => e.target.value.length > 200 && setValue('reason', e.target.value.slice(0, 200))}
            />
          </>
        )}
      </Form>
    </Modal>
  );
};

export default LeaveRegisterRejectModals;
