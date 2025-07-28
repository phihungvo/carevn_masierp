import React, { useEffect, useState } from 'react';
import { Col, Label, Row } from 'reactstrap';
import { DateObject } from 'react-multi-date-picker';
import { FormProvider, SubmitHandler, useForm } from 'react-hook-form';
import Form from 'app/components/form/form';
import useOrders from 'app/hooks/use-orders';
import useContracts from 'app/hooks/use-contracts';
import FormInput from 'app/components/form/form-input';
import FormSelect from 'app/components/form/form-select';
import ContractsList from 'app/modules/orders/contracts-list';
import FormDatePicker from 'app/components/form/form-date-picker';
import { zodResolver } from '@hookform/resolvers/zod';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { OrderFormSchema, orderSchema } from 'app/validation/order.validation';
import { useTextAreaResize } from 'app/hooks/use-text-area-resize';
import QualityIndexes from './quality-indexes';
import { v4 } from 'uuid';
import FormError from 'app/components/form/form-error';
import Flex from 'app/components/flex/flex';
import { IOrder } from 'app/shared/model/order.model';
import { EPayCondition } from 'app/shared/model/enumerations/contract.model';

const { useGetContracts, useGetContractById } = useContracts;
const { usePostOrderMutation, usePatchOrderMutation, useGetOrderByIdQuery } = useOrders;

interface IOrdersFormProps {
  type: 'create' | 'update';
  toggle?: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string | null;
  setSelectedRecord?: (value: string | null) => void;
  setSelectedRowKeys?: React.Dispatch<React.SetStateAction<string[]>>;
}

const OrdersForm = (props: IOrdersFormProps) => {
  const { type, toggle, toggleSuccess, selectedRecord, setSelectedRecord, setSelectedRowKeys } = props;

  useTextAreaResize();

  const [selectedMaterialsKeys, setSelectedMaterialsKeys] = useState<string[]>([]);
  const [isDirty, setIsDirty] = useState(false);

  const { data: contracts, isLoading } = useGetContracts({ withFull: false, withSum: false });
  const { data: detail } = useGetOrderByIdQuery(selectedRecord);
  const { mutate: create } = usePostOrderMutation(toggle, toggleSuccess);
  const { mutate: update } = usePatchOrderMutation(selectedRecord, toggle, toggleSuccess);

  const methods = useForm<OrderFormSchema>({
    defaultValues: {
      dateOrder: new DateObject(),
      qualityIndexes: [{ name: '', value: '' }],
    },
    resolver: zodResolver(orderSchema),
  });

  const { control, setValue, handleSubmit, watch, formState } = methods;

  const { data: contractsDetail } = useGetContractById(watch('contractId'));

  const onSubmit: SubmitHandler<OrderFormSchema> = values => {
    setIsDirty(true);

    if (selectedMaterialsKeys.length === 0) {
      return;
    }

    const submitValues: IOrder = {
      orderCode: values.orderCode,
      dateOrder: values.dateOrder.toDate().toISOString(),
      contractId: values.contractId,
      packageType: values.packageType,
      finishDate: values.finishDate.toDate().toISOString(),
      note: values.note,
      qualityIndexes: values.qualityIndexes?.map(q => ({ id: v4(), name: q.name, value: q.value })),
      contractMaterialUse: selectedMaterialsKeys,
      deliveryTermFrom: values?.deliveryTerm?.[0]?.toDate()?.toISOString(),
      deliveryTermTo: values?.deliveryTerm?.[1]
        ? values?.deliveryTerm?.[1]?.toDate()?.toISOString()
        : values?.deliveryTerm?.[0]?.toDate()?.toISOString(),
      payTerm: values?.payTerm,
      payCondition: values?.payCondition,
      deliveryLocation: values?.deliveryLocation,
    };

    if (type === 'update') {
      update(submitValues);
      setSelectedRecord(null);
      setIsDirty(false);
      return;
    }

    create(submitValues);
    setIsDirty(false);
  };

  useEffect(() => {
    if (detail) {
      setValue('orderCode', detail.orderCode);
      setValue('dateOrder', new DateObject(detail.dateOrder).add(7, 'hours'));
      setValue('contractId', detail.contractId);
      setValue('packageType', detail.packageType);
      setValue('finishDate', new DateObject(detail.finishDate).add(7, 'hours'));
      setValue('note', detail.note || '');
      setValue('qualityIndexes', detail.qualityIndexes);

    }

    if (contractsDetail) setSelectedMaterialsKeys(contractsDetail?.contractMaterialDTOS?.filter(item => item.id && !item?.orderId)?.map(item => item.id))

  }, [detail, contractsDetail]);
  return (
    <FormProvider {...methods}>
      <Form id={FORM.ORDER} onSubmit={handleSubmit(onSubmit)}>
        <Row>
          <Col md={6}>
            <FormInput control={control} id="code" name="orderCode" label="Mã số" />
          </Col>

          {/* <Col md={6}>
          <FormInput
            control={control}
            id="time"
            name="numberOrder"
            label="Lần ban hành"
            onChange={e => handleValidDecimal<OrderFormSchema>(e.target.value, 'numberOrder', DEFAULT_INTEGER_REGEX, setValue)}
            onPaste={e => handleValidatePaste(e, DEFAULT_INTEGER_REGEX)}
          />
        </Col> */}

          <Col md={6}>
            <FormSelect
              control={control}
              id="contractId"
              name="contractId"
              placeholder="Chọn hợp đồng"
              label="Hợp đồng"
              options={contracts?.data?.map(c => ({
                label: c?.contractName,
                value: c?.id,
              }))}
              isLoading={isLoading}
            />
          </Col>

          <Col md={6}>
            <FormInput control={control} id="package" name="packageType" label="Quy cách đóng gói" />
          </Col>

          <Col md={6}>
            <FormDatePicker setValue={setValue} control={control} id="dateOrder" name="dateOrder" label="Ngày tạo" formState={formState} />
          </Col>

          <Col md={6}>
            <FormDatePicker setValue={setValue} control={control} id="finishDate" name="finishDate" label="Ngày hoàn thành" formState={formState} />
          </Col>

          <Col md={6}>
            <FormDatePicker setValue={setValue} range control={control} label="Thời hạn giao hàng" name="deliveryTerm" formState={formState} />
          </Col>
          <Col md={6}>
            <FormInput control={control} label="Thời hạn thanh toán" name="payTerm" />
          </Col>
          <Col md={6}>
            <FormInput control={control} label="Điều kiện thanh toán" name="payCondition" type="select">
              <option selected disabled>
                Chọn điều kiện thanh toán
              </option>
              {Object.values(EPayCondition).map(condition => {
                return (
                  <option key={condition} value={condition}>
                    {condition}
                  </option>
                );
              })}
            </FormInput>
          </Col>

          <Col md={6}>
            <FormInput control={control} label="Địa điểm nhận hàng" name="deliveryLocation" />
          </Col>

          {watch('contractId') && (
            <Col md={12}>
              <Flex align="center" gap={8}>
                <Label className="mb-0">Danh sách thành phẩm</Label>
                {selectedMaterialsKeys.length === 0 && isDirty && <FormError message="Vui lòng chọn ít nhất một loại bột để tạo đơn hàng" />}
              </Flex>
              <ContractsList
                list={contractsDetail?.contractMaterialDTOS}
                setSelectedMaterialsKeys={setSelectedMaterialsKeys}
                selectedMaterialsKeys={selectedMaterialsKeys}
              />
            </Col>
          )}

          <div style={{ height: '12px' }} />

          <QualityIndexes />
          
          <div style={{ height: '12px' }} />

          <Col md={12}>
            <FormInput control={control} id="note" name="note" label="Ghi chú" type="textarea" rows={5} />
          </Col>
        </Row>
      </Form>
    </FormProvider>
  );
};

export default OrdersForm;
