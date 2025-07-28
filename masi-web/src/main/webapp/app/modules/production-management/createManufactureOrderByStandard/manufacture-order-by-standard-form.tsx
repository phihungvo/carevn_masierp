import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import { Typography } from 'app/components/typography/typography';
import { useAppSelector } from 'app/config/store';
import { DATE_FORMAT, DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import useProductionCommand from 'app/hooks/use-production-command';
import useProductionStandard from 'app/hooks/use-production-standard';
import { FORM } from 'app/shared/model/enumerations/form.model';
import {
  IPostManufactureOrderByOrderDto,
  IPostManufactureOrderByStandardDto,
} from 'app/shared/model/production-command.model';
import { ManufactureOrderByStandardCreateSchema } from 'app/validation/manufacture-order.validation';
import dayjs from 'dayjs';
import { useContext, useEffect } from 'react';
import { useFormContext } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { useParams } from 'react-router';
import { Col, Row } from 'reactstrap';
import { ProductionContext } from '../production-provider';
import { generateMonthYearOptions } from 'app/shared/util/format';
import useEmployee from 'app/hooks/use-employee';
import { PRODUCTION_STANDARD_STATUS } from 'app/shared/model/enumerations/production-standard.model';

const { useGetEmployeesQuery } = useEmployee;
const { useGetProductionStandards } = useProductionStandard;
const {
  useGetProductionCommandById,
  usePostProductionCommand,
  usePatchProductionCommand,
} = useProductionCommand;

const ManufactureOrderByStandardForm = () => {
  const { id } = useParams();

  const { toggleCreateSuccess, toggleUpdateSuccess } =
    useContext(ProductionContext);

  const account = useAppSelector(state => state.authentication.account);

  const { control, handleSubmit, setValue, watch, formState } =
    useFormContext<ManufactureOrderByStandardCreateSchema>();

  const { data: employees } = useGetEmployeesQuery({
    size: DEFAULT_PAGE_SIZE_NAX,
  });

  const { data: detail } = useGetProductionCommandById(id);
  const { data: standards } = useGetProductionStandards({
    size: DEFAULT_PAGE_SIZE_NAX,
    statuses: [PRODUCTION_STANDARD_STATUS.NEW],
  });

  const { mutate: create } = usePostProductionCommand();
  const { mutate: update } = usePatchProductionCommand(id);

  const onSubmit = (values: ManufactureOrderByStandardCreateSchema) => {
    const submitValues: IPostManufactureOrderByStandardDto = {
      name: values?.code,
      code: values?.code,
      productionStandardId: values?.productionStandardId,
      productionQuantity: Number(values?.productionQuantity ?? 0),
      fromDate: dayjs(values?.fromDate.toDate()).toISOString(),
      toDate: dayjs(values?.toDate.toDate()).toISOString(),
      note: values?.note,
      attributes: { note: values?.note } as any,
    };
    if (!id) create(submitValues, { onSuccess: toggleCreateSuccess });
    else update(submitValues, { onSuccess: toggleUpdateSuccess });
  };

  useEffect(() => {
    if (detail) {
      setValue('code', detail?.code);
      setValue('productionStandardId', detail?.productionStandardId);
      setValue('fromDate', new DateObject(detail?.fromDate));
      setValue('toDate', new DateObject(detail?.toDate));

      setValue('note', detail?.attributes?.note ?? '');
      setValue('status', detail?.status);
      setValue('productionQuantity', detail?.productionQuantity?.toString());

      const selected = standards?.data?.find(
        x => x.id === detail?.productionStandardId,
      );
      if (selected) {
        setValue('dueDate', dayjs(selected?.dueDate).format('MM/YYYY'));
        setValue('productionQuantity', `${selected?.productionPowderQty}`);
      }

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
      id={FORM.MANUFACTURE_ORDER_BY_STANDARD}
      onSubmit={handleSubmit(onSubmit)}
    >
      <Flex direction="column" gap={20}>
        <Typography level={5}>Thông tin chung</Typography>
        <Row>
          <Col md={4}>
            <FormInputV2
              control={control}
              name="code"
              label="Mã lệnh"
              placeholder="Vui lòng nhập mã lệnh"
              disabled={Boolean(id)}
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
              disabled={Boolean(id)}
            />
          </Col>

          <Col md={4}>
            <FormSelect
              control={control}
              name="productionStandardId"
              label="Định mức sản xuất"
              placeholder="Vui lòng chọn kế hoạch định mức"
              options={standards?.data?.map(x => ({
                label: `${x.code} - ${x.name}`,
                value: x.id,
              }))}
              disabled={disabled}
              onChanges={e => {
                const selected = standards?.data?.find(x => x.id === e);
                if (selected) {
                  setValue(
                    'dueDate',
                    dayjs(selected?.dueDate).format('MM/YYYY'),
                  );
                  setValue(
                    'productionQuantity',
                    `${selected?.productionPowderQty}`,
                  );
                }
              }}
            />
          </Col>

          <Col md={4}>
            <FormInputV2
              control={control}
              name="productionQuantity"
              label="Khối lượng định mức"
              placeholder="Vui lòng chọn kế hoạch định mức"
              disabled
            />
          </Col>

          <Col md={4}>
            <FormSelect
              control={control}
              name="dueDate"
              label="Tháng/Năm"
              placeholder="Vui lòng chọn kế hoạch định mức"
              options={generateMonthYearOptions(2024, 1, 2050, 1)}
              disabled
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
      </Flex>
    </Form>
  );
};

export default ManufactureOrderByStandardForm;
