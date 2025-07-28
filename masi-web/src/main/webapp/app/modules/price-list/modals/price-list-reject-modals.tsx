import { zodResolver } from '@hookform/resolvers/zod';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import Modal from 'app/components/modal/modal';
import useQuotations from 'app/hooks/use-quotations';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { QUOTATION_STATUS } from 'app/shared/model/enumerations/quotation.model';
import { InternalRejectFormSchema, internalRejectSchema } from 'app/validation/quotation.validation';
import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { FormGroup, Label } from 'reactstrap';

const { usePatchQuotationInternalApprove, usePatchQuotationCustomerProcess, useGetQuotationById } = useQuotations;

interface IPriceListRejectModals {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const PriceListRejectModals = (props: IPriceListRejectModals) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { control, setValue, handleSubmit, reset, watch } = useForm<InternalRejectFormSchema>({
    resolver: zodResolver(internalRejectSchema),
  });

  const { data: detail } = useGetQuotationById(selectedRecord);
  const { mutate, isPending } = usePatchQuotationInternalApprove(selectedRecord, toggle, toggleSuccess);
  const { mutate: customerReject, isPending: loadingCusReject } = usePatchQuotationCustomerProcess(selectedRecord, toggle, toggleSuccess);

  const onSubmit = (data: InternalRejectFormSchema) => {
    if (detail?.status === QUOTATION_STATUS.SENT) {
      customerReject(
        {
          status: QUOTATION_STATUS.REJECTED,
          rejectNote: data.rejectNote,
        },
        {
          onSuccess: () => {
            setSelectedRecord(null);
          },
        },
      );
      return;
    }

    mutate(
      {
        status: QUOTATION_STATUS.NEED_UPDATE,
        rejectNote: data.rejectNote,
      },
      {
        onSuccess: () => {
          setSelectedRecord(null);
        },
      },
    );
  };

  useEffect(() => {
    !isOpen && reset();
  }, [isOpen]);

  return (
    <Modal
      className="modal-reject-pl"
      isOpen={isOpen}
      toggle={toggle}
      okText="Xác nhận"
      okSubmitForm={FORM.REJECT_QUOTATION}
      disabledOk={isPending || loadingCusReject}
      titleHeader='Từ chối xét duyệt'
    >
      <Form id={FORM.REJECT_QUOTATION} onSubmit={handleSubmit(onSubmit)}>
        <FormGroup>
          <Label for="rejectNote" style={{ width: '100%' }}>
            <Flex justify="space-between">
              <span>Lý do</span>
              <span className="word-count">{watch('rejectNote')?.length || 0}/200</span>
            </Flex>
          </Label>
          <FormInput
            control={control}
            type="textarea"
            rows={4}
            name="rejectNote"
            placeholder="Lý do..."
            onChange={e => e.target.value.length > 200 && setValue('rejectNote', e.target.value.slice(0, 200))}
          />
        </FormGroup>
      </Form>
    </Modal>
  );
};

export default PriceListRejectModals;
