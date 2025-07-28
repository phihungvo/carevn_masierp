import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputSelect from 'app/components/formV2/form-input-select/form-input-select';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import { Typography } from 'app/components/typography/typography';
import { useAppSelector } from 'app/config/store';
import { DATE_FORMAT, DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import useCallCenter from 'app/hooks/use-call-center';
import useCustomers from 'app/hooks/use-customers';
import useEmployee from 'app/hooks/use-employee';
import useSupplier from 'app/hooks/use-supplier';
import { IPostCallCenterDto } from 'app/shared/model/call-center.model';
import {
  CALL_CENTER_GROUP,
  CALL_CENTER_SOURCE,
  CALL_CENTER_STATUS,
  CALL_CENTER_TYPE,
  CALL_CENTER_TYPE_PAGE,
} from 'app/shared/model/enumerations/call-center';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { ComplainSchema } from 'app/validation/complain.validation';
import dayjs from 'dayjs';
import { useContext, useEffect } from 'react';
import { useFormContext } from 'react-hook-form';
import { useParams } from 'react-router';
import { Col, Label, Row } from 'reactstrap';
import {
  complainMappingGroupOptions,
  complainMappingSourceOptions,
  complainMappingTypeOptions,
} from '../complain-mapping';
import { ComplainContext } from '../complain-provider';
import AttachmentCard from './attachment-card';

const { useGetEnabledCustomers } = useCustomers;
const { useGetSuppliers } = useSupplier;
const { useGetEmployeesQuery } = useEmployee;
const {
  useGetCallCenterByIdQuery,
  usePostCallCenterMutation,
  useUpdateCallCenterMutation,
} = useCallCenter;

const ComplainForm = () => {
  const { id } = useParams();

  const { toggleCreateSuccess, toggleUpdateSuccess } =
    useContext(ComplainContext);

  const account = useAppSelector(state => state.authentication.account);

  const { data: suppliers } = useGetSuppliers({ size: DEFAULT_PAGE_SIZE_NAX });
  const { data: customers } = useGetEnabledCustomers({
    size: DEFAULT_PAGE_SIZE_NAX,
  });
  const { data: employees } = useGetEmployeesQuery({
    size: DEFAULT_PAGE_SIZE_NAX,
  });

  const {
    control,
    handleSubmit,
    setValue,
    watch,
    reset,
    formState,
    clearErrors,
  } = useFormContext<ComplainSchema>();

  const solutionsWatch = watch('solutions');
  const statusWatch = watch('status');
  const sourceWatch = watch('sourceCS');

  useEffect(() => {
    if (sourceWatch) clearErrors('sourceCS');
  }, [sourceWatch]);

  const { data: detail } = useGetCallCenterByIdQuery(id);

  const { mutate: create } = usePostCallCenterMutation(() => {
    toggleCreateSuccess();
    reset();
  });
  const { mutate: update } = useUpdateCallCenterMutation(id, () => {
    toggleUpdateSuccess();
    reset();
  });

  useEffect(() => {
    if (employees?.data?.length && account?.id && !id) {
      const selected = employees?.data?.find(x => `${x.id}` === account?.id);
      if (selected)
        setValue(
          'createdBy',
          `${selected?.code} - ${selected?.lastName} ${selected?.firstName}`,
        );
    }
  }, [employees]);

  const onSubmit = (values: ComplainSchema) => {
    const submitValues: IPostCallCenterDto = {
      customerId: values?.customerId,
      employeeCreatedId: values?.employeeCreatedId,
      groupCS: values?.groupCS as CALL_CENTER_GROUP,
      phoneOfCaller: values?.phoneOfCaller,
      phoneOfName: values?.phoneOfName,
      receptionDate: values?.receptionDate,
      sourceCs: values?.sourceCS as CALL_CENTER_SOURCE,
      status: CALL_CENTER_STATUS.NEW,
      typeCS: values?.typeCS as CALL_CENTER_TYPE,
      typePageCs: CALL_CENTER_TYPE_PAGE.COMPLAINT,
      problemContent: values?.problemContent,
      attribute: values?.solutions ?? [],
    };
    if (!id) create(submitValues);
    else update(submitValues);
  };

  const addSolutions = () => {
    const tmp = {
      createdAt: new Date(),
      resolutionContent: '',
      responseContent: '',
      employeeId: account?.id,
    };
    setValue('solutions', [...solutionsWatch, tmp]);
  };

  const getEmployee = (id: string, createdAt: Date) => {
    const date = `${dayjs(createdAt).format(DATE_FORMAT?.DATE_TIME_2)}`;
    const selected = employees?.data?.find(x => x.id === id);
    if (selected) return `${selected?.code} - ${date}`;
    return date;
  };

  const disabled =
    statusWatch === (CALL_CENTER_STATUS.COMPLETED as string) ||
    statusWatch === (CALL_CENTER_STATUS.CLOSED as string);

  return (
    <Form id={FORM.COMPLAIN} onSubmit={handleSubmit(onSubmit)}>
      <Flex direction="column" gap={20}>
        <Typography level={5}>Thông tin chung</Typography>
        <Row>
          <Col md={3}>
            <FormDatePickerV2
              control={control}
              formState={formState}
              name="receptionDate"
              label="Ngày tiếp nhận"
              placeholder="Vui lòng chọn ngày"
              setValue={setValue}
              disabled={disabled}
              includeTimePicker
            />
          </Col>

          <Col md={3}>
            <FormSelect
              control={control}
              name="groupCS"
              label="Nhóm"
              placeholder="Vui lòng chọn nhóm"
              options={complainMappingGroupOptions}
              disabled={disabled}
            />
          </Col>

          <Col md={3}>
            <FormInputSelect
              control={control}
              name="phoneOfCaller"
              label="Nguồn"
              placeholder="Vui lòng nhập nguồn"
              disabled={disabled}
              selectKey="sourceCS"
              selectValue={watch('sourceCS')}
              selectOptions={complainMappingSourceOptions}
              errorMsg={
                !formState.errors?.phoneOfCaller
                  ? formState.errors?.sourceCS?.message ?? undefined
                  : undefined
              }
              setValue={setValue}
            />
          </Col>

          <Col md={3}>
            <FormInputV2
              control={control}
              name="phoneOfName"
              label="Tên người gọi"
              placeholder="Nhập tên người gọi"
              disabled={disabled}
            />
          </Col>

          {/* <Col md={3}>
            <FormSelect
              control={control}
              name="sourceCS"
              label="Nguồn"
              placeholder="Vui lòng chọn nguồn"
              options={complainMappingSourceOptions}
              disabled={disabled}
            />
          </Col> */}

          <Col md={3}>
            <FormSelect
              control={control}
              name="typeCS"
              label="Phân loại"
              placeholder="Vui lòng chọn phân loại"
              options={complainMappingTypeOptions}
              disabled={disabled}
            />
          </Col>

          <Col md={3}>
            <FormSelect
              control={control}
              name="customerId"
              label="Khách hàng / NCC"
              placeholder="Vui lòng chọn khách hàng / NCC"
              disabled={disabled}
              options={[
                ...(customers?.data ?? []),
                ...(suppliers?.data ?? []),
              ]?.map(x => ({
                value: x?.id,
                label: `${x?.customerCode ?? x.code} - ${
                  x?.companyName ?? x.name
                } `,
              }))}
            />
          </Col>

          <Col md={3}>
            <FormSelect
              control={control}
              name="employeeCreatedId"
              label="Nhân viên"
              placeholder="Vui lòng chọn nhân viên"
              disabled={disabled}
              options={employees?.data?.map(x => ({
                value: x?.id,
                label: `${x.code} - ${x.employeeProfile?.fullName}`,
              }))}
            />
          </Col>

          <Col md={3} />

          <Col md={3}>
            <FormInputV2
              control={control}
              name="createdBy"
              label="Người tạo"
              disabled
            />
          </Col>

          <Col md={3}>
            <FormDatePickerV2
              control={control}
              formState={formState}
              name="createdAt"
              label="Ngày tạo"
              disabled
              includeTimePicker
            />
          </Col>
        </Row>

        <Typography level={5}>Thông tin chi tiết</Typography>
        <Row>
          <Col md={12}>
            <FormInputV2
              control={control}
              name="problemContent"
              label="Nội dung"
              placeholder="Vui lòng nhập nội dung"
              disabled={disabled}
            />
          </Col>
          <Label className="form-label-v2">Hướng giải quyết và phản hồi</Label>
          <Row>
            <div
              style={{
                margin: '0px 12px',
                padding: '16px',
                border: '1px solid #bfc1c5',
                borderRadius: '8px',
              }}
            >
              {solutionsWatch?.map((x, idx) => (
                <Row key={idx}>
                  <Col md={6}>
                    <Flex justify="space-between" className="form-group-v2">
                      <Label className="form-label-v2">Giải quyết</Label>
                      <Label className="form-label-v2">
                        {getEmployee(x.employeeId, x.createdAt)}
                      </Label>
                    </Flex>
                    <FormInputV2
                      type="textarea"
                      rows={3}
                      control={control}
                      name={`solutions.${idx}.resolutionContent`}
                      placeholder="Điền"
                      disabled={disabled}
                    />
                  </Col>

                  <Col md={6}>
                    <Flex justify="space-between" className="form-group-v2">
                      <Label className="form-label-v2">Ý kiến phản hồi</Label>
                      <Label className="form-label-v2">
                        {getEmployee(x.employeeId, x.createdAt)}
                      </Label>
                    </Flex>
                    <FormInputV2
                      type="textarea"
                      rows={3}
                      control={control}
                      name={`solutions.${idx}.responseContent`}
                      placeholder="Điền"
                      disabled={disabled}
                    />
                  </Col>
                </Row>
              ))}
              <ButtonV2
                variant="fill"
                color="blue"
                disabled={disabled}
                onClick={() => addSolutions()}
              >
                Thêm hướng giải quyết
              </ButtonV2>
            </div>
          </Row>
        </Row>

        <Typography level={5}>Tiếp nhận & xử lý</Typography>
        <Flex justify="space-between" gap={20}>
          <Flex gap={20}>
            <AttachmentCard
              name={detail?.employeeAssign?.fullName ?? '---'}
              status="processing"
              code={detail?.employeeAssign?.employeeCode ?? '---'}
              date={
                detail?.employeeAssignDate
                  ? dayjs(detail?.employeeAssignDate).format(
                      DATE_FORMAT.DATE_TIME_2,
                    )
                  : '---'
              }
            />
            <AttachmentCard
              name={detail?.employeeClose?.fullName ?? '---'}
              status="success"
              code={detail?.employeeClose?.employeeCode ?? '---'}
              date={
                detail?.employeeCloseDate
                  ? dayjs(detail?.employeeCloseDate).format(
                      DATE_FORMAT.DATE_TIME_2,
                    )
                  : '---'
              }
            />
          </Flex>
        </Flex>
      </Flex>
    </Form>
  );
};

export default ComplainForm;
