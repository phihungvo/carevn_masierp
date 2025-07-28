import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormSelect from 'app/components/form/form-select';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useEmployee from 'app/hooks/use-employee';
import usePurchase from 'app/hooks/use-purchase';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { CreatePurchaseReviewFormSchema, createPurchaseReviewSchema } from 'app/validation/purchase.validation';
import React, { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';

const { useGetEmployeesQuery } = useEmployee;
const { usePostPurchaseReviewMutation } = usePurchase;

interface IPurchaseRequestModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const PurchaseRequestModals = (props: IPurchaseRequestModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const { control, handleSubmit, reset } = useForm<CreatePurchaseReviewFormSchema>({
    resolver: zodResolver(createPurchaseReviewSchema),
  });

  const { data, isLoading } = useGetEmployeesQuery();
  const { mutate } = usePostPurchaseReviewMutation(toggle, toggleSuccess);

  const onSubmit: SubmitHandler<CreatePurchaseReviewFormSchema> = values => {
    mutate({
      documentId: selectedRecord,
      employeeId1: values.employeeId1,
      employeeId2: values.employeeId2,
      employeeId3: values.employeeId3,
      employeeId4: values.employeeId4,
    });
    setSelectedRecord(null);
    reset();
  };

  useEffect(() => {
    !isOpen && reset();
  }, [isOpen]);

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="modals-request-purchase" okText="Xác nhận" okSubmitForm={FORM.PURCHASE}>
      <Typography level={3}>Yêu cầu xét duyệt</Typography>
      <Form onSubmit={handleSubmit(onSubmit)} id={FORM.PURCHASE}>
        <Row>
          <Col md={6}>
            <FormSelect
              control={control}
              id="employeeId1"
              name="employeeId1"
              placeholder="Chọn người duyệt"
              label="Người duyệt 1"
              options={data?.data?.map(e => ({
                label: `${(e?.lastName || '') + ' ' + (e?.firstName || '')}`,
                value: e?.id,
              }))}
              isLoading={isLoading}
            />
          </Col>

          <Col md={6}>
            <FormSelect
              control={control}
              id="employeeId2"
              name="employeeId2"
              placeholder="Chọn người duyệt"
              label="Người duyệt 2"
              options={data?.data?.map(e => ({
                label: `${(e?.lastName || '') + ' ' + (e?.firstName || '')}`,
                value: e?.id,
              }))}
              isLoading={isLoading}
            />
          </Col>

          <Col md={6}>
            <FormSelect
              control={control}
              id="employeeId3"
              name="employeeId3"
              placeholder="Chọn người duyệt"
              label="Người duyệt 3"
              options={data?.data?.map(e => ({
                label: `${(e?.lastName || '') + ' ' + (e?.firstName || '')}`,
                value: e?.id,
              }))}
              isLoading={isLoading}
            />
          </Col>

          <Col md={6}>
            <FormSelect
              control={control}
              id="employeeId4"
              name="employeeId4"
              placeholder="Chọn người duyệt"
              label="Người duyệt 4"
              options={data?.data?.map(e => ({
                label: `${(e?.lastName || '') + ' ' + (e?.firstName || '')}`,
                value: e?.id,
              }))}
              isLoading={isLoading}
            />
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default PurchaseRequestModals;
