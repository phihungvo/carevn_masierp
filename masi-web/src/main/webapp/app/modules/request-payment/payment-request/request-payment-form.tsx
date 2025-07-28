import Flex from 'app/components/flex/flex';
import FormInput from 'app/components/form/form-input';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import useEmployee from 'app/hooks/use-employee';
import useSupplier from 'app/hooks/use-supplier';
import { PAYMENT_REQUEST_STATUS } from 'app/shared/model/enumerations/payment-request';
import { RequestPaymentSchema } from 'app/validation/request-payment.validation';
import { useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import Attachments from '../components/Attachments/Attachments';
import InvoiceTable from '../components/InvoiceTable/InvoiceTable';
import PersonSign from '../components/PersonSign/PersonSign';

const { useGetEmployeesQuery } = useEmployee;
const { useGetSuppliers } = useSupplier;

interface IRequestPaymentFormProps {
  type: 'update' | 'create';
}

function RequestPaymentForm(props: IRequestPaymentFormProps) {
  const { type } = props;

  const methods = useFormContext<RequestPaymentSchema>();
  const { control, formState, setValue, watch } = methods;
  const paymentDetailsWatch = watch('paymentDetails');

  const { data: employees, isLoading } = useGetEmployeesQuery();
  const { data: suppliers } = useGetSuppliers();

  const disabled =
    watch('status') === (PAYMENT_REQUEST_STATUS.WAITING_APPROVE as string) ||
    watch('status') === (PAYMENT_REQUEST_STATUS.CANCELLED as string) ||
    watch('status') === (PAYMENT_REQUEST_STATUS.APPROVED as string);

  return (
    <Flex direction="column" gap={30}>
      <Row>
        <Col span={3}>
          <FormInputV2
            control={control}
            id="code"
            disabled
            name="code"
            label="Số ĐN"
          />
        </Col>

        <Col md={3}>
          <FormDatePickerV2
            control={control}
            id="paymentDate"
            name="paymentDate"
            disabled={disabled}
            label="Ngày ĐN"
            formState={formState}
            setValue={setValue}
            placeholder="Vui lòng chọn ngày ĐN"
          />
        </Col>

        <Col md={3}>
          <FormSelect
            control={control}
            id="employeeId"
            name="employeeId"
            placeholder="Chọn nhân viên"
            label="Nhân viên"
            options={employees?.data?.map(e => ({
              label: `${e.code} - ${e.employeeProfile?.fullName}`,
              value: e?.id,
            }))}
            isLoading={isLoading}
            onChanges={e => {
              const selected = employees?.data?.find(x => `${x.id}` === e);
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
            disabled
            control={control}
            id="workspaceName"
            name="workspaceName"
            label="Bộ phận"
          />
        </Col>

        <Col md={6}>
          <FormSelect
            disabled={disabled}
            control={control}
            id="supplierId"
            name="supplierId"
            placeholder="Nhà cung cấp"
            label="Nhà cung cấp"
            options={suppliers?.data?.map(wsp => ({
              label: `${wsp?.code} - ${wsp?.name}`,
              value: wsp?.id,
            }))}
            onChanges={e => {
              const selected = suppliers?.data?.find(x => `${x.id}` === e);
              if (selected) {
                setValue('supplierCodeName', `${selected?.name}`);
                setValue(
                  'paymentDetails',
                  [...(paymentDetailsWatch ?? [])].filter(x => x.new),
                );
              }
            }}
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
            rows={2}
            type="textarea"
            control={control}
            id="content"
            name="content"
            label="Nội dung"
            placeholder="Nhập nội dung"
            disabled={disabled}
          />
        </Col>
      </Row>

      <Col md={12}>
        <InvoiceTable />
      </Col>

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

export default RequestPaymentForm;
