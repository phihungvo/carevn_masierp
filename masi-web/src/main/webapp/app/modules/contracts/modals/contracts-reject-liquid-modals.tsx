import { zodResolver } from '@hookform/resolvers/zod';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import Modal from 'app/components/modal/modal';
import { useAppSelector } from 'app/config/store';
import useContracts from 'app/hooks/use-contracts';
import { IContract } from 'app/shared/model/contract.model';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { RejectContractFormSchema, rejectContractSchema } from 'app/validation/contract.validation';
import React, { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { Col, Label, Row } from 'reactstrap';

const { usePatchContractReview } = useContracts;

interface IContractRejectLiquidModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
  record?: IContract
}

const ContractRejectLiquidModals = (props: IContractRejectLiquidModalsProps) => {
  const { isOpen, toggle, selectedRecord, toggleSuccess, setSelectedRecord, record } = props;

  const account = useAppSelector(state => state.authentication.account);
  const isRole = record?.requestApprovals?.find(item => item?.employeeId === account?.id);
  const isCheck = record?.requestApprovals?.find(item => item?.employeeId === account?.id)?.result;

  const { control, handleSubmit, watch, setValue, reset } = useForm<RejectContractFormSchema>({
    resolver: zodResolver(rejectContractSchema),
  });

  const { mutate } = usePatchContractReview(toggle, toggleSuccess);

  const onSubmit: SubmitHandler<RejectContractFormSchema> = values => {
    mutate({
      rejectNote: values.rejectNote,
      isApproved: false,
      documentId: selectedRecord,
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
      className="modal-reject-liquid"
      okSubmitForm={!isCheck && FORM.REJECT_LIQUID}
      cancel={!!isRole && isCheck == undefined}
      ok={!!isRole && isCheck == undefined}
      titleHeader='Từ chối xét duyệt thanh lý hợp đồng'
    >
      {
        isRole ?
          <>
            {
              isCheck == undefined &&
              <Form id={FORM.REJECT_LIQUID} onSubmit={handleSubmit(onSubmit)}>
                <Row>
                  <Label for="reviewerNote" style={{ width: '100%' }}>
                    <Flex justify="space-between">
                      <span>Lý do</span>
                      <span className="word-count">{watch('rejectNote')?.length || 0}/200</span>
                    </Flex>
                  </Label>
                  <Col>
                    <FormInput
                      rows={5}
                      control={control}
                      name="rejectNote"
                      type="textarea"
                      placeholder="Lý do từ chối..."
                      onChange={e => e.target.value.length > 200 && setValue('rejectNote', e.target.value.slice(0, 200))}
                    />
                  </Col>
                </Row>
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

export default ContractRejectLiquidModals;
