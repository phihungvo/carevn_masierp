import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormDatePicker from 'app/components/form/form-date-picker';
import FormInput from 'app/components/form/form-input';
import Modal from 'app/components/modal/modal';
import { useAppSelector } from 'app/config/store';
import useOrders from 'app/hooks/use-orders';
import { ORDER_REVIEW_SOLUTION, ORDER_STATUS } from 'app/shared/model/enumerations/order.model';
import { PatchOrderReviewFormSchema, patchOrderReviewSchema } from 'app/validation/order.validation';
import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import ordersMapping from '../orders-mapping';

const { orderReviewSolutionTextMapping } = ordersMapping;
const { usePatchOrderReviewMutation, useGetOrderReviewByOrderIdQuery } = useOrders;

interface IOrdersRejectModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (record: string) => void;
}

const OrdersRejectModals = (props: IOrdersRejectModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const account = useAppSelector(state => state.authentication.account);

  const { control, handleSubmit, watch, reset, setValue, formState } = useForm<PatchOrderReviewFormSchema>({
    resolver: zodResolver(patchOrderReviewSchema),
    defaultValues: {
      approvalSolution: ORDER_REVIEW_SOLUTION.REJECT,
    },
  });
  const approvalSolution = watch('approvalSolution');

  const { data } = useGetOrderReviewByOrderIdQuery(selectedRecord);
  const { mutate, isPending } = usePatchOrderReviewMutation(toggle, toggleSuccess);

  const findReviewerId = data?.find(orderReview => orderReview?.employeeId === account?.id)?.id;

  const onSubmit = (values: PatchOrderReviewFormSchema) => {
    mutate({
      id: findReviewerId,
      status: ORDER_STATUS.REJECTED,
      approvalStatusNote: values.approvalStatusNote,
      approvalSolution: values.approvalSolution as ORDER_REVIEW_SOLUTION,
      awaitingDate: values?.awaitingDate?.toDate()?.toISOString(),
    });
    setSelectedRecord(null);
  };

  useEffect(() => {
    !isOpen && reset();
  }, [isOpen]);

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-reject-order"
      okText="Xác nhận"
      disabledOk={!findReviewerId || isPending}
      loadingOk={isPending}
      onOk={handleSubmit(onSubmit)}
      titleHeader='Từ chối xét duyệt'
    >
      {!isPending && !findReviewerId ? (
        <p className="text-danger">Bạn không phải là người xét duyệt!</p>
      ) : (
        <Form onSubmit={handleSubmit(onSubmit)}>
          <Row>
            <Col md={6}>
              <FormInput control={control} name="approvalSolution" label="Giải pháp" type="select">
                <option selected disabled>
                  Chọn giải pháp
                </option>
                <option value={ORDER_REVIEW_SOLUTION.REJECT}>{orderReviewSolutionTextMapping(ORDER_REVIEW_SOLUTION.REJECT)}</option>
                <option value={ORDER_REVIEW_SOLUTION.WAITING}>{orderReviewSolutionTextMapping(ORDER_REVIEW_SOLUTION.WAITING)}</option>
              </FormInput>
            </Col>

            {approvalSolution === ORDER_REVIEW_SOLUTION.WAITING && (
              <Col md={6}>
                <FormDatePicker setValue={setValue} control={control} name="awaitingDate" label="Chờ đến ngày" formState={formState} />
              </Col>
            )}

            <Col md={6}>
              <FormInput control={control} name="approvalStatusNote" label="Lý do" type="textarea" />
            </Col>
          </Row>
        </Form>
      )}
    </Modal>
  );
};

export default OrdersRejectModals;
