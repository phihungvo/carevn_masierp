import Card from 'app/components/card/card';
import Form from 'app/components/form/form';
import FormInput from 'app/components/form/form-input';
import FormSelect from 'app/components/form/form-select';
import { DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import useCustomers from 'app/hooks/use-customers';
import useEmployee from 'app/hooks/use-employee';
import { FORM } from 'app/shared/model/enumerations/form.model';
import React from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import FormDatePicker from 'app/components/form/form-date-picker';
import { zodResolver } from '@hookform/resolvers/zod';
import {
  AllocationSchema,
  allocationSchema,
} from 'app/validation/allocation.validation';

const { useGetEnabledCustomers } = useCustomers;
const { useGetEmployeesQuery } = useEmployee;

interface IAllocationForm {
  type: 'update' | 'create';
}

function AllocationForm(props: IAllocationForm) {
  const {} = props;

  const { control, setValue, formState, handleSubmit } =
    useForm<AllocationSchema>({
      resolver: zodResolver(allocationSchema),
    });
  const { data: customers, isLoading: cusLoading } = useGetEnabledCustomers({
    size: DEFAULT_PAGE_SIZE_NAX,
  });
  const { data, isLoading } = useGetEmployeesQuery();

  const onSubmit: SubmitHandler<AllocationSchema> = values => {
    console.log('values: ', values);
  };

  return (
    <Form id={FORM.ALLOCATION} onSubmit={handleSubmit(onSubmit)}>
      <Card
        header="Thông tin chung"
        className="card-body-padding"
        classNameHeader="card-header-bold"
      >
        <Row>
          <Col md={6}>
            <FormDatePicker
              setValue={setValue}
              control={control}
              label="Kỳ khấu hao"
              name="depreciationPeriod"
              formState={formState}
            />
          </Col>
          <Col md={6}>
            <FormDatePicker
              setValue={setValue}
              control={control}
              label="Ngày khấu hao"
              name="depreciationDate"
              formState={formState}
            />
          </Col>
        </Row>

        <Row>
          <Col md={6}>
            <FormDatePicker
              setValue={setValue}
              control={control}
              label="Ngày hạch toán"
              name="accountingDate"
              formState={formState}
            />
          </Col>

          <Col md={6}>
            <FormSelect
              control={control}
              id="staffRecord"
              name="calculator"
              placeholder="Chọn người tính"
              label="Nhân viên người tính"
              options={data?.data?.map(e => ({
                label: `${(e?.lastName || '') + ' ' + (e?.firstName || '')}`,
                value: e?.id,
              }))}
              isLoading={isLoading}
            />
          </Col>
        </Row>

        <Row>
          <Col md={12}>
            <FormInput
              control={control}
              label="Diễn giải"
              name="description"
              type="textarea"
            />
          </Col>
        </Row>
      </Card>
    </Form>
  );
}

export default AllocationForm;
