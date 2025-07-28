import React, { useEffect } from 'react';
import { FormGroup, Label } from 'reactstrap';
import { SubmitHandler, useForm } from 'react-hook-form';

import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import Modal from 'app/components/modal/modal';
import FormInput from 'app/components/form/form-input';
import useRecruitment from 'app/hooks/use-recruitment';
import { zodResolver } from '@hookform/resolvers/zod';
import { Typography } from 'app/components/typography/typography';
import { recruitmentRejectSchema, RecruitmentRejectSchema } from 'app/validation/recruitment.validation';

const { useRejectRecruitment } = useRecruitment;

interface ILogisticsMaterialProposalRejectModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const LogisticsMaterialProposalRejectModals = (props: ILogisticsMaterialProposalRejectModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { control, setValue, watch, handleSubmit, reset } = useForm<RecruitmentRejectSchema>({
    resolver: zodResolver(recruitmentRejectSchema),
  });

  const { mutate, isPending } = useRejectRecruitment(selectedRecord, toggle, toggleSuccess);

  const onSubmit: SubmitHandler<RecruitmentRejectSchema> = values => {
    mutate({
      rejectNote: values.rejectNote,
    });
    setSelectedRecord(null);
    reset();
  };

  useEffect(() => {
    !isOpen && reset();
  }, [isOpen]);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okText="Xác nhận"
      className="logistics-material-proposal-reject-modals"
      onOk={handleSubmit(onSubmit)}
      disabledOk={isPending}
    >
      <Typography level={3}>Từ chối xét duyệt</Typography>

      <Form>
        <FormGroup>
          <Flex justify="space-between">
            <Label for="note">Lý do</Label>
            <span className="word-count">{watch('rejectNote')?.length || 0}/200</span>
          </Flex>
          <FormInput
            rows={5}
            control={control}
            name="rejectNote"
            type="textarea"
            placeholder="Lý do từ chối..."
            onChange={e => e.target.value.length > 200 && setValue('rejectNote', e.target.value.slice(0, 200))}
          />
        </FormGroup>
      </Form>
    </Modal>
  );
};

export default LogisticsMaterialProposalRejectModals;
