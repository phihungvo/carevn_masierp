import React from 'react';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import Form from 'app/components/form/form';
import { SubmitHandler, useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { ReviewTimeKeepingExplanationSchema, reviewTimeKeepingExplanationSchema } from 'app/validation/time-keeping-explanation.validation';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { Col, Label, Row } from 'reactstrap';
import Flex from 'app/components/flex/flex';
import FormInput from 'app/components/form/form-input';
import useTimeKeepingExplanation from 'app/hooks/use-time-keeping-explanation';
import { TIME_KEEPING_EXPLANATION_REVIEW_STATUS } from 'app/shared/model/enumerations/time-keeping-explanation.model';
import { ITimeKeepingExplanation } from 'app/shared/model/time-keeping-explanation.model';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { REVIEW_TIME_KEEPING_EXPLANATION } = MUTATION_KEY;
const { usePatchTimeKeepingExplanationReviewMutation } = useTimeKeepingExplanation;

// MODAL REJECT REQUEST
interface IModalRejectRequest {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRows: ITimeKeepingExplanation[];
  setSelectedRowKeys: (selectedRowKeys: string[]) => void;
  setSelectedRows: React.Dispatch<React.SetStateAction<ITimeKeepingExplanation[]>>
}

export const RejectRequestModal = (props: IModalRejectRequest) => {
  const { isOpen, toggle, toggleSuccess, selectedRows, setSelectedRowKeys, setSelectedRows } = props;

  const { mutateAsync } = usePatchTimeKeepingExplanationReviewMutation();
  const isReviewingReq = useIsMutating({ mutationKey: [REVIEW_TIME_KEEPING_EXPLANATION] });

  const { control, handleSubmit, watch, reset } = useForm({
    resolver: zodResolver(reviewTimeKeepingExplanationSchema),
  });

  const onSubmit: SubmitHandler<ReviewTimeKeepingExplanationSchema> = data => {
    const promises = selectedRows.map(row =>
      mutateAsync({
        data: {
          reason: data?.reason,
          status: TIME_KEEPING_EXPLANATION_REVIEW_STATUS.REJECTED,
        },
        id: row?.reviewDtos[0]?.id,
      }),
    );

    Promise.all(promises)
      .then(() => {
        toggleSuccess();
      })
      .finally(() => {
        setSelectedRowKeys([]);
        setSelectedRows([])
        toggle();
      });

    reset();
  };

  return (
    <Modal
      disabledOk={!!isReviewingReq}
      isOpen={isOpen}
      toggle={toggle}
      className="modal-reject-explanation"
      onOk={handleSubmit(onSubmit)}
      cancel={false}
      titleHeader='Từ chối đơn giải trình'
    >
      <Form<ReviewTimeKeepingExplanationSchema> id={FORM.REJECT_EXPLANATION} onSubmit={handleSubmit(onSubmit)}>
        <Row>
          <Label for="reason" style={{ width: '100%' }}>
            <Flex justify="space-between">
              <span>Ghi chú</span>
              <span className="word-count">{watch('reason')?.length || 0}/200</span>
            </Flex>
          </Label>
          <Col>
            <FormInput rows={5} control={control} name="reason" type="textarea" />
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

// MODAL SUCCESS REJECT REQUEST
interface IModalSuccessRejectRequest {
  isOpen: boolean;
  toggle: () => void;
}

export const SuccessRejectRequestModal = (props: IModalSuccessRejectRequest) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      titleHeader='Từ chối đơn giải trình'
    >
      <Typography level={4}>Từ chối đơn giải trình thành công</Typography>
    </Modal>
  );
};
