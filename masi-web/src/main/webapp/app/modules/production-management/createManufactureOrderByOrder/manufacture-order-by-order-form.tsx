import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import { Typography } from 'app/components/typography/typography';
import { useAppSelector } from 'app/config/store';
import { DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import useEmployee from 'app/hooks/use-employee';
import useOrders from 'app/hooks/use-orders';
import useProductionCommand from 'app/hooks/use-production-command';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { IPostManufactureOrderByOrderDto } from 'app/shared/model/production-command.model';
import { ManufactureOrderByOrderCreateSchema } from 'app/validation/manufacture-order.validation';
import dayjs from 'dayjs';
import { useContext, useEffect } from 'react';
import { useFormContext } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { useParams } from 'react-router';
import { Col, Row } from 'reactstrap';
import { ProductionContext } from '../production-provider';
import ManufactureOrderByOrderItemsTable from './manufacture-order-by-order-items-table';
import { ORDER_STATUS } from 'app/shared/model/enumerations/order.model';

const { useGetEmployeesQuery } = useEmployee;
const { useOrdersQuery, useGetOrderByIdQuery } = useOrders;
const {
  useGetProductionCommandById,
  usePostProductionCommand,
  usePatchProductionCommand,
} = useProductionCommand;

const ManufactureOrderByOrderForm = () => {
  const { id } = useParams();

  const { toggleCreateSuccess, toggleUpdateSuccess } =
    useContext(ProductionContext);

  const account = useAppSelector(state => state.authentication.account);

  const { control, handleSubmit, setValue, watch, reset, formState } =
    useFormContext<ManufactureOrderByOrderCreateSchema>();

  const orderIdWatch = watch('orderId');
  const itemsWatch = watch('items');

  const { data: employees } = useGetEmployeesQuery({
    size: DEFAULT_PAGE_SIZE_NAX,
  });

  const { data: detail } = useGetProductionCommandById(id);
  const { data: orders } = useOrdersQuery({
    size: DEFAULT_PAGE_SIZE_NAX,
    statuses: [ORDER_STATUS.APPROVED],
  });
  const { data: orderDetail } = useGetOrderByIdQuery(orderIdWatch);

  const { mutate: create } = usePostProductionCommand();
  const { mutate: update } = usePatchProductionCommand(id);

  const onSubmit = (values: ManufactureOrderByOrderCreateSchema) => {
    const itemSelected = itemsWatch?.find(x => x.itemId === values.itemId);

    const submitValues: IPostManufactureOrderByOrderDto = {
      name: values?.code,
      code: values?.code,
      productionQuantity: Number(values?.productionQuantity ?? 0),
      orderId: values?.orderId,
      itemId: values?.itemId,
      orderItemId: values?.itemId,
      fromDate: dayjs(values?.fromDate.toDate()).toISOString(),
      toDate: dayjs(values?.toDate.toDate()).toISOString(),
      note: values?.note,
      attributes: {
        note: values?.note,
        percentProtein: `${itemSelected?.percentProtein ?? ''}`,
      } as any,
    };
    if (!id) create(submitValues, { onSuccess: toggleCreateSuccess });
    else update(submitValues, { onSuccess: toggleUpdateSuccess });
  };

  useEffect(() => {
    if (orderDetail) {
      const contract = orderDetail?.contract;
      setValue('orderCustomerName', contract?.customer?.companyName);
      setValue(
        'orderDeliveryDate',
        new DateObject(orderDetail?.deliveryTermTo),
      );

      const items = contract?.contractMaterialDTOS?.map(x => ({
        itemId: x.itemId,
        itemName: x.materialName,
        quantity: x.quantity,
        percentProtein: x.itemDTO?.percentProtein,
        parameter: x?.proteinParameters ?? '',
      }));
      setValue('items', items);
    } else setValue('items', []);
  }, [orderDetail]);

  useEffect(() => {
    if (detail) {
      setValue('code', detail?.code);
      setValue('orderId', detail?.orderId);
      setValue('itemId', detail?.orderItemId);
      setValue('fromDate', new DateObject(detail?.fromDate));
      setValue('toDate', new DateObject(detail?.toDate));
      setValue('note', detail?.attributes?.note ?? '');
      setValue('status', detail?.status);
      setValue('productionQuantity', detail?.productionQuantity?.toString());

      setValue('createdAt', dayjs(detail?.createdAt).toDate());
    }
  }, [detail]);

  useEffect(() => {
    if (employees?.data?.length && account?.id) {
      if (!id) {
        const selected = employees?.data?.find(x => `${x.id}` === account?.id);
        if (selected)
          setValue(
            'createdBy',
            `${selected?.code} - ${selected?.lastName} ${selected?.firstName}`,
          );
      } else if (detail) {
        const selected = employees?.data?.find(
          x => `${x.id}` === detail?.createdBy,
        );
        if (selected)
          setValue(
            'createdBy',
            `${selected?.code} - ${selected?.lastName} ${selected?.firstName}`,
          );
      }
    }
  }, [employees, detail]);

  const disabled = false;

  return (
    <Form
      id={FORM.MANUFACTURE_ORDER_BY_ORDER}
      onSubmit={handleSubmit(onSubmit)}
    >
      <Flex direction="column" gap={20}>
        <Typography level={5}>Thông tin chung</Typography>
        <Row>
          <Col md={4}>
            <FormSelect
              control={control}
              name="orderId"
              label="Đơn hàng"
              placeholder="Vui lòng chọn đơn đặt hàng"
              options={orders?.data?.map(x => ({
                label: x.orderCode,
                value: x.id,
              }))}
              disabled={disabled}
              onChanges={e => {
                setValue('itemId', null);
                if (!e) {
                  setValue('items', []);
                }
              }}
            />
          </Col>

          <Col md={4}>
            <FormInputV2
              control={control}
              name="orderCustomerName"
              label="Khách hàng"
              placeholder="Vui lòng chọn đơn đặt hàng"
              disabled
            />
          </Col>

          <Col md={4}>
            <FormDatePickerV2
              control={control}
              formState={formState}
              setValue={setValue}
              name="orderDeliveryDate"
              label="Thời hạn giao hàng"
              placeholder="Vui lòng chọn đơn đặt hàng"
              disabled={disabled}
            />
          </Col>

          <Col md={4}>
            <FormInputV2
              control={control}
              name="createdBy"
              label="Người tạo"
              disabled
            />
          </Col>

          <Col md={4}>
            <FormDatePickerV2
              control={control}
              formState={formState}
              name="createdAt"
              label="Ngày tạo"
              disabled
            />
          </Col>

          <Col md={12}>
            <FormInputV2 control={control} name="note" label="Ghi chú" />
          </Col>
        </Row>
        <ManufactureOrderByOrderItemsTable />
        <Typography level={5}>Sản xuất</Typography>
        <Row>
          <Col md={4}>
            <FormInputV2
              control={control}
              name="code"
              label="Mã sản xuất"
              placeholder="Vui lòng nhập mã sản xuất"
              disabled={disabled}
            />
          </Col>

          <Col md={4}>
            <FormDatePickerV2
              control={control}
              formState={formState}
              setValue={setValue}
              name="fromDate"
              label="Ngày bắt đầu"
              placeholder="Vui lòng chọn ngày bắt đầu"
              disabled={disabled}
            />
          </Col>

          <Col md={4}>
            <FormDatePickerV2
              control={control}
              formState={formState}
              setValue={setValue}
              name="toDate"
              label="Ngày kết thúc"
              placeholder="Vui lòng chọn ngày kết thúc"
              disabled={disabled}
            />
          </Col>
        </Row>
        <Typography level={5}>Dự kiến sản xuất</Typography>
        <Row>
          <Col md={6}>
            <FormSelect
              control={control}
              name="itemId"
              label="Mặt hàng"
              placeholder="Vui lòng chọn mặt hàng"
              options={(itemsWatch ?? []).map(x => ({
                value: x.itemId,
                label: x.itemName,
              }))}
              disabled={disabled}
            />
          </Col>
          <Col md={6}>
            <FormInputV2
              control={control}
              name="productionQuantity"
              label="Khối lượng"
              placeholder="Vui lòng nhập khối lượng"
              disabled={disabled}
            />
          </Col>
        </Row>
      </Flex>
    </Form>
  );
};

export default ManufactureOrderByOrderForm;
