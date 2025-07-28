import Flex from 'app/components/flex/flex';
import FormInput from 'app/components/form/form-input';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import useEmployee from 'app/hooks/use-employee';
import { PAYMENT_REQUEST_STATUS } from 'app/shared/model/enumerations/payment-request';
import { RefundRequestSchema } from 'app/validation/request-payment.validation';
import { useMemo } from 'react';
import { useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import Attachments from '../components/Attachments/Attachments';
import PersonSign from '../components/PersonSign/PersonSign';
import RefundRequestDepositBill from './components/RefundRequestDepositBill';
import RefundRequestDeviation from './components/RefundRequestDeviation';
import RefundRequestSpent from './components/RefundRequestSpent';

const { useGetEmployeesQuery } = useEmployee;

function RefundRequestForm() {
  const { control, setValue, watch } = useFormContext<RefundRequestSchema>();
  const employees = useGetEmployeesQuery();

  const converted_employee = useMemo(() => {
    return employees?.data?.data?.map(emp => ({
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
        <FormInputV2 name="code" label="Bộ Số CT" />
        </Col>

        <Col md={3}>
          <FormDatePickerV2
            control={control}
            name="paymentDate"
            label="Ngày CT"
            disabled={disabled}
            setValue={setValue}
            placeholder="Vui lòng chọn ngày CT"
          />
        </Col>

        <Col md={3}>
          <FormSelect
            control={control}
            name="employeeId"
            label="Nhân viên"
            placeholder="Chọn"
            options={converted_employee}
            onChanges={e => {
              const selected = employees?.data?.data?.find(x => x.id === e);
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
          <FormInputV2 name="workspaceName" label="Bộ phận" disabled />
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

      <RefundRequestDepositBill />

      <RefundRequestSpent />

      <RefundRequestDeviation />

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

export default RefundRequestForm;
