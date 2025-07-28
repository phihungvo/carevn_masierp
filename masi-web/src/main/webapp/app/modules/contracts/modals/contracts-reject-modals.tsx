import React, { useEffect } from 'react';
import { FormGroup, Label } from 'reactstrap';
import { zodResolver } from '@hookform/resolvers/zod';
import { SubmitHandler, useForm } from 'react-hook-form';

import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import Modal from 'app/components/modal/modal';
import useContracts from 'app/hooks/use-contracts';
import FormInput from 'app/components/form/form-input';
import { useAppSelector } from 'app/config/store';
import { IContract } from 'app/shared/model/contract.model';
import { recruitmentRejectSchema, RecruitmentRejectSchema } from 'app/validation/recruitment.validation';

const { usePatchContractNormalReview } = useContracts;

interface IContractsRejectModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
  record?: IContract
}

const ContractsRejectModals = (props: IContractsRejectModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord, record } = props;

  const account = useAppSelector(state => state.authentication.account);
  const isRole = record?.normalApprovals?.find(item => item?.employeeId === account?.id);
  const isCheck = record?.normalApprovals?.find(item => item?.employeeId === account?.id)?.result;

  const { control, setValue, watch, handleSubmit, reset } = useForm<RecruitmentRejectSchema>({
    resolver: zodResolver(recruitmentRejectSchema),
  });

  const { mutate, isPending } = usePatchContractNormalReview(selectedRecord, toggle, toggleSuccess);

  const onSubmit: SubmitHandler<RecruitmentRejectSchema> = values => {
    mutate({
      rejectNote: values.rejectNote,
      documentId: selectedRecord,
      isApproved: false
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
      className="contracts-reject-modals"
      onOk={handleSubmit(onSubmit)}
      disabledOk={isPending}
      cancel={!!isRole && isCheck == undefined}
      ok={!!isRole && isCheck == undefined}
      titleHeader='Từ chối xét duyệt'
    >
      {
        isRole ?
          <>
            {
              isCheck == undefined &&
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
            }
            {
              isCheck === true && <p className="text-danger">Bạn đã xét duyệt rồi, không được phép xét duyệt nữa!</p>
            }
            {
              isCheck === false && <p className="text-danger">Bạn đã từ chối rồi rồi, không được phép xét duyệt nữa!</p>
            }
          </> : <p className="text-danger">Bạn không phải là người xét duyệt đơn này!</p>
      }
    </Modal>
  );
};

export default ContractsRejectModals;
