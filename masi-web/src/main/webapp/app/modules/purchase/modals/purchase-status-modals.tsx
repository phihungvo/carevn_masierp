import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { DEFAULT_DECIMAL_REGEX } from 'app/constants/common';
import usePurchase from 'app/hooks/use-purchase';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { handleValidatePaste, handleValidDecimal } from 'app/shared/util/handle-valid-decimal';
import { PurchaseDeliveryFormSchema, purchaseDeliverySchema } from 'app/validation/purchase.validation';
import React, { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';

const { usePatchPurchaseDelivery, useGetPurchaseById } = usePurchase;

interface IPurchaseStatusModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const PurchaseStatusModals = (props: IPurchaseStatusModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { control, handleSubmit, setValue } = useForm<PurchaseDeliveryFormSchema>({
    resolver: zodResolver(purchaseDeliverySchema),
  });

  const { data } = useGetPurchaseById(selectedRecord);
  const { mutate } = usePatchPurchaseDelivery(data?.purchaseDelivery?.id, toggle, toggleSuccess);

  useEffect(() => {
    if (data) {
      setValue('delivered', data?.purchaseDelivery?.deliveried?.toString());
      setValue('waitingDelivery', data?.purchaseDelivery?.waitingDelivery?.toString());
    }
  }, [data]);

  const onSubmit: SubmitHandler<PurchaseDeliveryFormSchema> = values => {
    mutate({
      deliveried: Number(values.delivered),
      waitingDelivery: Number(values.waitingDelivery),
      purchaseRequestId: selectedRecord,
    });
    setSelectedRecord(null);
  };

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modals-create-purchase" okText="Cập nhật" okSubmitForm={FORM.PURCHASE}>
      <Typography level={3}>Cập nhật tình trạng giao hàng</Typography>

      <Form id={FORM.PURCHASE} onSubmit={handleSubmit(onSubmit)}>
        <Row>
          <Col md={6}>
            <FormInput
              control={control}
              name="delivered"
              label="Đã giao"
              type="number"
              min={0}
              onChange={e => handleValidDecimal<PurchaseDeliveryFormSchema>(e.target.value, 'delivered', DEFAULT_DECIMAL_REGEX, setValue)}
              onPaste={e => handleValidatePaste(e, DEFAULT_DECIMAL_REGEX)}
            />
          </Col>

          <Col md={6}>
            <FormInput control={control} name="waitingDelivery" label="Số lượng còn nợ" disabled />
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default PurchaseStatusModals;
