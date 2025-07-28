import Flex from 'app/components/flex/flex';
import FormInput from 'app/components/form/form-input';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import { Typography } from 'app/components/typography/typography';
import useEmployee from 'app/hooks/use-employee';
import { PAYMENT_REQUEST_STATUS } from 'app/shared/model/enumerations/payment-request';
import { AdvanceRequestSchema } from 'app/validation/request-payment.validation';
import { useMemo } from 'react';
import { useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import Attachments from '../components/Attachments/Attachments';
import PersonSign from '../components/PersonSign/PersonSign';

const { useGetEmployeesQuery } = useEmployee;

function AdvanceRequestForm() {
  const { control, setValue, watch, formState } =
    useFormContext<AdvanceRequestSchema>();
  const { data: employees } = useGetEmployeesQuery();

  const converted_employee = useMemo(() => {
    return employees?.data?.map(emp => ({
      value: emp?.id,
      label: `${emp?.employeeProfile?.employeeCode} - ${emp?.employeeProfile?.fullName}`,
    }));
  }, [employees]);

  const disabled =
    watch('status') === (PAYMENT_REQUEST_STATUS.WAITING_APPROVE as string) ||
    watch('status') === (PAYMENT_REQUEST_STATUS.CANCELLED as string) ||
    watch('status') === (PAYMENT_REQUEST_STATUS.APPROVED as string);

  return (
    <Flex direction="column" gap={30}>
      <Row>
        <Col md={3}>
          <FormInputV2 name="code" control={control} label="Số CT" disabled />
        </Col>
        <Col md={3}>
          <FormDatePickerV2
            control={control}
            formState={formState}
            name="paymentDate"
            label="Ngày CT"
            disabled={disabled}
            setValue={setValue}
            placeholder="Vui lòng chọn ngày ĐN"
          />
        </Col>
        <Col md={3}>
          <FormSelect
            control={control}
            name="employeeId"
            label="Nhân viên"
            options={converted_employee}
            placeholder="Chọn"
            onChanges={e => {
              const selected = employees?.data?.find(x => x.id === e);
              if (selected)
                setValue(
                  'workspaceName',
                  `${selected?.workspace?.normalizedName} - ${selected?.workspace?.name}`,
                );
            }}
            disabled={disabled}
          />
        </Col>
        <Col md={3}>
          <FormInputV2
            id="workspaceName"
            name="workspaceName"
            label="Bộ phận"
            disabled
            placeholder="Bộ phận"
          />
        </Col>
        <Col md={3}>
          <FormInputV2
            type="number"
            control={control}
            name="totalAmount"
            label="Số tiền"
            placeholder="Vui lòng nhập số tiền"
            disabled={disabled}
          />
        </Col>
        <Col md={3}>
          <FormDatePickerV2
            control={control}
            name="reimbursementDate"
            label="Ngày hoàn tạm ứng"
            disabled
            placeholder="__/__/_____"
          />
        </Col>
        <Col md={3}>
          <FormInputV2
            control={control}
            name="createdByName"
            label="Người lập"
            disabled
          />
        </Col>
        <Col md={3}>
          <FormDatePickerV2
            control={control}
            name="createdDate"
            label="Ngày lập"
            disabled
          />
        </Col>
        <Col md={12}>
          <FormInput
            type="textarea"
            rows={2}
            control={control}
            name="content"
            label="Nội dung"
            placeholder="Vui lòng nhập nội dung"
            disabled={disabled}
          />
        </Col>
      </Row>

      <Flex direction="column" gap={12}>
        <Typography
          level={5}
          style={{
            fontSize: '18px',
            fontWeight: '500',
            letterSpacing: '-0.5px',
          }}
        >
          Thông tin phiếu chi
        </Typography>
        <Row>
          <Col md={3}>
            <FormInputV2
              control={control}
              label="Phiếu chi"
              name="paymentVoucher"
              placeholder="Vui lòng nhập phiếu chi"
              disabled={disabled}
            />
          </Col>
          <Col md={3}>
            <FormInputV2
              type="number"
              control={control}
              label="Số tiền chi"
              name="paymentVoucherAmount"
              placeholder="Vui lòng nhập số tiền chi"
              disabled={disabled}
            />
          </Col>
        </Row>
      </Flex>

      <Row>
        <Col md={6}>
          <PersonSign disabledCheckBox={true} />
        </Col>
        <Col md={6}>
          <Attachments />
        </Col>
      </Row>
    </Flex>
  );
}

export default AdvanceRequestForm;
