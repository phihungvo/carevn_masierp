import { zodResolver } from '@hookform/resolvers/zod';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import AuthGuard from 'app/components/guards/auth-guard';
import { DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import useEmployee from 'app/hooks/use-employee';
import { FORM } from 'app/shared/model/enumerations/form.model';
import {
  ManufactureOrderSchema,
  production2Schema,
  Production2Schema,
} from 'app/validation/manufacture-order.validation';
import { useEffect, useState } from 'react';
import { FormProvider, useForm, useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { enableDirectPackaging } from '../../production-ultis';

const { useGetEmployeesQuery } = useEmployee;

export const TabProduction2 = ({
  onSubmit,
}: {
  onSubmit: (values, complete) => void;
}) => {
  const [completeState, setCompleteState] = useState<boolean>(false);

  const methods = useForm<Production2Schema>({
    resolver: zodResolver(production2Schema),
  });
  const { control, setValue, formState, watch, handleSubmit } = methods;

  const methodPrimary = useFormContext<ManufactureOrderSchema>();
  const { watch: watchPrimary } = methodPrimary;
  const statusWatch = watchPrimary('status');
  const production2Watch = watchPrimary('production2');

  const { data: employees } = useGetEmployeesQuery({
    size: DEFAULT_PAGE_SIZE_NAX,
  });

  const disabled = enableDirectPackaging(statusWatch);

  useEffect(() => {
    if (production2Watch) {
      setValue('fromDate', production2Watch?.fromDate);
      setValue('toDate', production2Watch?.toDate);
      setValue('employeeId', production2Watch?.employeeId);
      setValue('note', production2Watch?.note);
    }
  }, [production2Watch]);

  useEffect(() => {
    if (completeState) setCompleteState(false);
  }, [watch()]);

  return (
    <FormProvider {...methods}>
      <Form
        id={FORM.MANUFACTURE_ORDER_PRODUCTION_2}
        onSubmit={handleSubmit(values => onSubmit(values, completeState))}
      >
        <Flex direction="column" gap={8}>
          <Row>
            <Col md={4}>
              <FormDatePickerV2
                control={control}
                formState={formState}
                setValue={setValue}
                name="fromDate"
                label="Ngày giờ bắt đầu"
                placeholder="Vui lòng chọn ngày giờ bắt đầu"
                includeTimePicker
                disabled={disabled}
              />
            </Col>
            <Col md={4}>
              <FormDatePickerV2
                control={control}
                formState={formState}
                setValue={setValue}
                name="toDate"
                label="Ngày giờ kết thúc"
                placeholder="Vui lòng chọn ngày giờ kết thúc"
                includeTimePicker
                disabled={disabled}
              />
            </Col>
            <Col md={4}>
              <FormSelect
                control={control}
                name="employeeId"
                label="Nhân viên phụ trách"
                placeholder="Vui lòng chọn nhân viên phụ trách"
                options={employees?.data?.map(x => ({
                  value: x?.id,
                  label: `${x.code} - ${x.employeeProfile?.fullName}`,
                }))}
                disabled={disabled}
              />
            </Col>
            <Col md={12}>
              <FormInputV2
                control={control}
                name="note"
                label="Ghi chú"
                placeholder="Vui lòng nhập ghi chú"
                disabled={disabled}
              />
            </Col>
          </Row>
        </Flex>
        <div className="production-card__footer">
          <AuthGuard permissionKey="PRODUCTION_MANUFACTURE_ORDER.EDIT">
            <ButtonV2
              type="submit"
              onClick={() => setCompleteState(true)}
              disabled={disabled}
              style={{ borderColor: '#027A48', color: '#027A48' }}
            >
              Hoàn thành
            </ButtonV2>
            <ButtonV2
              variant="solid"
              color="blue"
              type="submit"
              disabled={disabled}
            >
              Lưu
            </ButtonV2>
          </AuthGuard>
        </div>
      </Form>
    </FormProvider>
  );
};
