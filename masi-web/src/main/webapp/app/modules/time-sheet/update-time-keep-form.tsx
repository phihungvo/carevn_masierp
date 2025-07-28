import { zodResolver } from '@hookform/resolvers/zod';
import Form from 'app/components/form/form';
import FormDatePicker from 'app/components/form/form-date-picker';
import FormInputDecimal from 'app/components/form/form-input-decimal';
import { TIME_SHEET_DECIMAL_REGEX } from 'app/constants/common';
import useTimeSheet from 'app/hooks/use-time-sheet';
import timeSheetService from 'app/services/time-sheet.service';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { UpdateTimeSheetFormSchema, updateTimeSheetSchema } from 'app/validation/time-sheet.validation';
import 'dayjs/locale/vi';
import React from 'react';
import { useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { Alert, Col, Row } from 'reactstrap';
import './time-sheet.scss';

const { usePatchTimeKeepingMutation } = useTimeSheet;

interface IUpdateTimeKeepForm {
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string | null;
}

const UpdateTimeKeepForm = (props: IUpdateTimeKeepForm) => {
  const { toggle, toggleSuccess, selectedRecord } = props;

  const { mutate } = usePatchTimeKeepingMutation(toggle, toggleSuccess);

  const { control, handleSubmit, setValue, formState, watch } = useForm<UpdateTimeSheetFormSchema>({
    resolver: zodResolver(updateTimeSheetSchema),
    defaultValues: async () => {
      const res = await timeSheetService.getTimeKeepingRecordById(selectedRecord);
      return {
        date: new DateObject(res?.data?.zonedDate)?.toDate()?.toISOString(),
        hoursWorked: res?.data?.totalCompletionPercent?.toString(),
      };
    },
  });

  const onSubmit = (values: UpdateTimeSheetFormSchema) => {
    mutate({
      id: selectedRecord,
      data: {
        id: selectedRecord,
        hoursWorked: Number(values.hoursWorked),
      },
    });
  };

  const handleValidDecimal = (e: React.ChangeEvent<HTMLInputElement>) => {
    const decimalRegex = TIME_SHEET_DECIMAL_REGEX;

    if (decimalRegex.test(e.target.value) || e.target.value === '') {
      setValue('hoursWorked', e.target.value);
    } else {
      setValue('hoursWorked', e.target.value.slice(0, -1));
    }
  };

  return (
    <Form<UpdateTimeSheetFormSchema> onSubmit={handleSubmit(onSubmit)} id={FORM.UPDATE_TIMESHEET}>
      {Number(watch('hoursWorked')) < 0 || Number(watch('hoursWorked')) > 16 && <Alert color="warning">Số giờ phải lớn hơn hoặc bằng 0 và nhỏ hơn hoặc bằng 16.</Alert>}
      <Row>
        <Col md={6}>
          <FormInputDecimal
            min={0}
            control={control}
            id="hours"
            label="Số giờ"
            name="hoursWorked"
            placeholder="0"
            onChange={handleValidDecimal}
          />
        </Col>
        <Col md={6}>
          <FormDatePicker setValue={setValue} control={control} id="date" label="Ngày" name="date" disabled formState={formState} />
        </Col>
      </Row>
    </Form>
  );
};

export default UpdateTimeKeepForm;
