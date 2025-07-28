import { zodResolver } from '@hookform/resolvers/zod';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import Modal from 'app/components/modal/modal';
import useRecruitment from 'app/hooks/use-recruitment';
import { recruitmentRejectSchema, RecruitmentRejectSchema } from 'app/validation/recruitment.validation';
import React, { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { FormGroup, Label } from 'reactstrap';

const { useRejectRecruitment } = useRecruitment;

interface IRecruitmentRejectModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const RecruitmentRejectModals = (props: IRecruitmentRejectModalsProps) => {
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
      className="modals-approve-sign"
      onOk={handleSubmit(onSubmit)}
      disabledOk={isPending}
      titleHeader='Từ chối xét duyệt'
    >
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

export default RecruitmentRejectModals;
