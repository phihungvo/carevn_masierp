import Form from 'app/components/form/form';
import FormDatePicker from 'app/components/form/form-date-picker';
import FormInput from 'app/components/form/form-input';
import React, { useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { CustomerFormSchema, customerSchema } from 'app/validation/customer.validation';
import { zodResolver } from '@hookform/resolvers/zod';
import useCustomers from 'app/hooks/use-customers';
import useEmployee from 'app/hooks/use-employee';
import { useAppSelector } from 'app/config/store';
import { DateObject } from 'react-multi-date-picker';
import { useDebounce } from 'app/hooks/use-debounce';
import FormSelect from 'app/components/form/form-select';
import { handleValidatePaste } from 'app/shared/util/handle-valid-decimal';
import { DEFAULT_NUMBER_REGEX } from 'app/constants/common';
import Card from 'app/components/card/card';
import { handleNumberOnlyInput } from 'app/shared/util/handle-number-only-input';

const { usePostCustomer, usePatchCustomer, useGetCustomerById, useGetCustomerByCode, useGetNextCustomerCode, useGetCustomerByTaxCode } =
  useCustomers;
const { useGetEmployeesQuery } = useEmployee;

interface ICustomerForm {
  type: 'create' | 'update';
  toggle?: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string;
  setSelectedRecord?: (record: string) => void;
}

const CustomerForm = (props: ICustomerForm) => {
  const { type, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;
  const account = useAppSelector(state => state.authentication.account);

  const { control, setValue, handleSubmit, watch, formState } = useForm<CustomerFormSchema>({
    resolver: zodResolver(customerSchema),
    defaultValues: {
      // customerOwner: account?.id,
      ...(type === 'create' && { contractSigned: new DateObject() })
    },
  });

  const debounceCustomerCode = useDebounce(watch('customerCode'), 500);
  const debounceTaxCode = useDebounce(watch('taxCode'), 500);

  const { data: employeeData, isLoading: empLoading } = useGetEmployeesQuery();
  const { data: customerData } = useGetCustomerById(selectedRecord);
  const { data: nextCustomerCode } = useGetNextCustomerCode();
  const { isSuccess } = useGetCustomerByCode(debounceCustomerCode);
  const { isSuccess: isSuccessTaxCode } = useGetCustomerByTaxCode(
    type === 'update' && debounceTaxCode === customerData?.data?.taxCode ? '' : debounceTaxCode,
  );
  const { mutate: create } = usePostCustomer(toggle, toggleSuccess);
  const { mutate: update } = usePatchCustomer(selectedRecord, toggle, toggleSuccess);

  const isWarningCustomerCode =
    type === 'update'
      ? debounceCustomerCode && debounceCustomerCode !== customerData?.data?.customerCode && isSuccess
      : debounceCustomerCode && isSuccess;

  const isWarningTaxCode =
    type === 'update'
      ? debounceTaxCode && customerData?.data?.taxCode && debounceTaxCode !== customerData?.data?.taxCode && isSuccessTaxCode
      : debounceTaxCode && isSuccessTaxCode;

  const onSubmit: SubmitHandler<CustomerFormSchema> = values => {
    if (isWarningCustomerCode || isWarningTaxCode) return;

    if (type === 'update') {
      update({
        customerCode: values.customerCode,
        companyName: values.companyName,
        address: values.address,
        taxCode: values.taxCode,
        lastName: values.fullName.split(' ')[0],
        firstName: values.fullName
          .split(' ')
          .filter((_, i) => i > 0)
          .join(' '),
        birthday: values.birthday?.toDate().toISOString(),
        phoneNumber: values.phoneNumber,
        email: values.email,
        position: values.position,
        customerOwner: values.customerOwner,
        contractFrom: values.contractFrom?.toDate().toISOString(),
        contractTo: values.contractTo?.toDate().toISOString(),
        note: values.note,
      });
      setSelectedRecord(null);
      return;
    }

    create({
      customerCode: values.customerCode,
      companyName: values.companyName,
      address: values.address,
      taxCode: values.taxCode,
      lastName: values.fullName.split(' ')[0],
      firstName: values.fullName
        .split(' ')
        .filter((_, i) => i > 0)
        .join(' '),
      birthday: values.birthday?.toDate().toISOString(),
      phoneNumber: values.phoneNumber,
      email: values.email,
      position: values.position,
      customerOwner: values.customerOwner,
      contractSigned: values.contractSigned?.toDate().toISOString(),
      contractFrom: values.contractFrom?.toDate().toISOString(),
      contractTo: values.contractTo?.toDate().toISOString(),
      note: values.note,
    });
  };

  useEffect(() => {
    if (customerData) {
      setValue('customerCode', customerData?.customerCode);
      setValue('companyName', customerData?.companyName || '');
      setValue('address', customerData?.address);
      setValue('taxCode', customerData?.taxCode || '');
      setValue('fullName', `${customerData?.lastName || ''}${customerData?.lastName ? ' ' : ''}${customerData?.firstName || ''}`);
      // setValue('firstName', customerData?.firstName);
      customerData?.birthday && setValue('birthday', new DateObject(customerData?.birthday).add(7, 'hours'));
      setValue('phoneNumber', customerData?.phoneNumber);
      setValue('email', customerData?.email);
      setValue('position', customerData?.position || '');
      setValue('customerOwner', customerData?.customerOwner);
      customerData?.contractSigned && setValue('contractSigned', new DateObject(customerData?.contractSigned).add(7, 'hours'));
      customerData?.contractFrom && setValue('contractFrom', new DateObject(customerData?.contractFrom).add(7, 'hours'));
      customerData?.contractTo && setValue('contractTo', new DateObject(customerData?.contractTo).add(7, 'hours'));
      setValue('note', customerData?.note || '');
    }
  }, [customerData]);

  useEffect(() => {
    if (nextCustomerCode) {
      setValue('customerCode', nextCustomerCode?.nextCustomerCode);
    }
  }, [nextCustomerCode]);

  useEffect(() => {
    setValue('customerOwner', account?.id)
  }, [])

  return (
    <Form id={FORM.CUSTOMER} onSubmit={handleSubmit(onSubmit)}>
      <Card header='Thông tin chung' className='card-body-padding' classNameHeader='card-header-bold'>
        <Row>
          <Col md={6}>
            <FormInput
              control={control}
              id="code"
              name="customerCode"
              label="Mã KH"
              // {...(isWarningCustomerCode && { errorMsg: 'Mã KH đã tồn tại' })}
              disabled
            />
          </Col>

          <Col md={6}>
            <FormInput control={control} id="companyName" name="companyName" label="Tên Cty" />
          </Col>

          <Col md={6}>
            <FormInput
              control={control}
              id="taxCode"
              name="taxCode"
              label="MST"
              {...(isWarningTaxCode && { errorMsg: 'Mã số thuế đã tồn tại' })}
            />
          </Col>

          <Col md={6}>
            <FormInput control={control} id="address" name="address" label="Địa chỉ" />
          </Col>
        </Row>
      </Card>

      <div className="divider" />

      <Card header='Thông tin liên hệ' className='card-body-padding' classNameHeader='card-header-bold'>
        <Row>
          <Col md={6}>
            <FormInput control={control} id="lastName" name="fullName" label="Tên người liên hệ" />
          </Col>

          <Col md={6}>
            <FormInput control={control} id="position" name="position" label="Chức vụ" />
          </Col>

          <Col md={6}>
            <FormDatePicker setValue={setValue} control={control} id="birthday" name="birthday" label="Ngày sinh" formState={formState} />
          </Col>

          <Col md={6}>
            <FormInput
              control={control}
              id="phone"
              name="phoneNumber"
              label="Số điện thoại"
              type='text'
              onChange={e => handleNumberOnlyInput(e, setValue, 'phoneNumber')}
              onPaste={e => handleValidatePaste(e, DEFAULT_NUMBER_REGEX)}
            />
          </Col>

          <Col md={6}>
            <FormInput control={control} id="email" name="email" label="Email" />
          </Col>
        </Row>
      </Card>

      <div className="divider" />

      <Card header='Thông tin khác' className='card-body-padding' classNameHeader='card-header-bold'>
        <Row>
          <Col md={6}>
            <FormSelect
              control={control}
              id="customerOwner"
              name="customerOwner"
              placeholder="Chọn người phụ trách"
              label="Người phụ trách"
              options={employeeData?.data?.map(e => ({
                label: `${e?.lastName || ''} ${e?.firstName || ''}`,
                value: e?.id,
              }))}
              isLoading={empLoading}
            />
          </Col>

          <Col md={6}>
            <FormDatePicker setValue={setValue} control={control} id="contractDate" name="contractSigned" label="Ngày tạo" disabled formState={formState} />
          </Col>



          <Col md={12}>
            <FormInput rows={5} control={control} id="note" name="note" label="Ghi chú" type="textarea" />
          </Col>
        </Row>
      </Card>

      <div className="divider" />

      <Card header='Thời gian hiệu lực' className='card-body-padding' classNameHeader='card-header-bold'>
        <Row>
          <Col md={6}>
            <FormDatePicker setValue={setValue} control={control} id="fromDate" name="contractFrom" label="Từ" formState={formState} />
          </Col>
          <Col md={6}>
            <FormDatePicker setValue={setValue} control={control} id="toDate" name="contractTo" label="Đến" formState={formState} />
          </Col>
        </Row>
      </Card>
    </Form>
  );
};

export default CustomerForm;
