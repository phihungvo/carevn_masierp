import { zodResolver } from '@hookform/resolvers/zod';
import { useIsMutating } from '@tanstack/react-query';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { MUTATION_KEY } from 'app/constants/query-key';
import useLeaveRequest from 'app/hooks/use-leave-request';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { LEAVE_REQUEST_STATUS } from 'app/shared/model/enumerations/leave-request.model';
import { ILeaveRequest } from 'app/shared/model/leave-request.model';
import { LeaveRequestRejectFormSchema, leaveRequestRejectSchema } from 'app/validation/leave-request.validation';
import React, { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { Col, Label, Row } from 'reactstrap';

const { REVIEW_LEAVE_REQUEST } = MUTATION_KEY;
const { usePatchLeaveRequestReviewMutation } = useLeaveRequest;

// MODAL REJECT REQUEST
interface IModalRejectRequest {
  isOpen: boolean;
  toggle: () => void;
  selectedRecord: string;
  setSelectedRecord: (id: string) => void;
  selectedRowKeys: string[];
  setSelectedRowKeys: (ids: string[]) => void;
  selected: {
    [leaving_id: string]: ILeaveRequest;
  };
  toggleNotices?: () => void;
  setTextNotices?: React.Dispatch<React.SetStateAction<string>>;
  setSelectedRows: React.Dispatch<React.SetStateAction<ILeaveRequest[]>>
}

export const RejectRequestModal = (props: IModalRejectRequest) => {
  const { control, handleSubmit, reset, watch, setValue } = useForm<LeaveRequestRejectFormSchema>({
    resolver: zodResolver(leaveRequestRejectSchema),
  });

  const { isOpen, toggle, selectedRecord, selected, selectedRowKeys, setSelectedRowKeys, setSelectedRecord, toggleNotices, setTextNotices, setSelectedRows } = props;

  const { mutateAsync } = usePatchLeaveRequestReviewMutation();
  const isRejectingReq = useIsMutating({ mutationKey: [REVIEW_LEAVE_REQUEST] });

  const onSubmit: SubmitHandler<LeaveRequestRejectFormSchema> = data => {
    if (selectedRecord) {
      mutateAsync({
        reviewId: selected[selectedRecord].reviews[0].id,
        status: LEAVE_REQUEST_STATUS.REJECTED,
        reason: data?.reason,
      })
        .catch((err) => {
          if (err?.response?.data?.message?.includes('alreadyCancelled')) setTextNotices('đã bị huỷ')
          else if (err?.response?.data?.message?.includes('alreadyApproved')) setTextNotices('đã được duyệt')
          else if (err?.response?.data?.message?.includes('timesheetLocked')) setTextNotices('thuộc bảng chấm công')
          toggleNotices()
        })
        .finally(() => {
          setSelectedRecord('');
          setSelectedRows([]);
          toggle();
          reset();

        });
      return;
    }

    if (selectedRowKeys.length) {
      const promises = selectedRowKeys.map(id =>
        mutateAsync({ reviewId: selected[id].reviews[0].id, status: LEAVE_REQUEST_STATUS.REJECTED, reason: data?.reason }),
      );
      Promise.all(promises)
        .catch((err) => {
          if (err?.response?.data?.message?.includes('alreadyCancelled')) setTextNotices('đã bị huỷ')
          else if (err?.response?.data?.message?.includes('alreadyApproved')) setTextNotices('đã được duyệt')
          else if (err?.response?.data?.message?.includes('timesheetLocked')) setTextNotices('thuộc bảng chấm công')
          toggleNotices()
        })
        .finally(() => {
          toggle();
          setSelectedRows([]);
          setSelectedRowKeys([]);
          reset();
        });
    }
  };

  useEffect(() => {
    if (!isOpen) reset();
  }, [isOpen])

  return (
    <Modal
      disabledOk={!!isRejectingReq}
      isOpen={isOpen}
      toggle={toggle}
      okSubmitForm={FORM.REJECT_LEAVE_REQUEST}
      className="reject-leave-request-modal"
      titleHeader='Từ chối đơn nghỉ phép'
    >
      <Form<LeaveRequestRejectFormSchema> id={FORM.REJECT_LEAVE_REQUEST} onSubmit={handleSubmit(onSubmit)}>
        <Row>
          <Label for="reason" style={{ width: '100%' }}>
            <Flex justify="space-between">
              <span>Ghi chú</span>
              <span className="word-count">{watch('reason')?.length || 0}/200</span>
            </Flex>
          </Label>
          <Col>
            <FormInput
              rows={5}
              control={control}
              name="reason"
              type="textarea"
              onChange={e => e.target.value.length > 200 && setValue('reason', e.target.value.slice(0, 200))}
            />
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};
