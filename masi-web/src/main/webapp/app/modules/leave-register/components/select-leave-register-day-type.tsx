import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';
import FormInput from 'app/components/form/form-input';
import React, { useEffect, useState } from 'react';
import { Control, FormState, UseFormSetValue, UseFormTrigger, UseFormWatch } from 'react-hook-form';
import { FormGroup, Label } from 'reactstrap';
import { LEAVE_REQUEST_DAY_TYPE, LEAVE_REQUEST_SHIFT_TYPE } from 'app/shared/model/enumerations/leave-request.model';
import Input from 'app/components/input/input';
import { LeaveRegimeFormSchema } from 'app/validation/leave-regime.validation';
import dayjs from 'dayjs';

interface ISelectLeaveRegisterDayType {
  control: Control<LeaveRegimeFormSchema>;
  setValue: UseFormSetValue<LeaveRegimeFormSchema>;
  trigger: UseFormTrigger<LeaveRegimeFormSchema>;
  watch: UseFormWatch<LeaveRegimeFormSchema>;
  formState: FormState<LeaveRegimeFormSchema>;
  isCheck?: boolean;
  setIsCheck?: React.Dispatch<React.SetStateAction<boolean>>
  type?: 'update' | 'create';
}

const SelectLeaveRegisterDayType = (props: ISelectLeaveRegisterDayType) => {
  const { control, setValue, trigger, watch, isCheck, setIsCheck, type } = props;


  const [showOptions, setShowOptions] = useState(false);
  const [shift, setShift] = useState<LEAVE_REQUEST_SHIFT_TYPE>();

  const toggleShowOptions = () => {
    setShowOptions(prev => !prev);
  };

  useEffect(() => {
    if (watch('fromTime') && watch('toTime')) {
      if (watch('fromTime') === '08:30' && watch('toTime') === '12:00') setShift(LEAVE_REQUEST_SHIFT_TYPE?.MORNING_SHIFT)
      else setShift(LEAVE_REQUEST_SHIFT_TYPE?.AFTERNOON_SHIFT)
    }
  }, [watch('fromTime')])

  const handleSelectLeaveDayType = () => {

    if (shift === LEAVE_REQUEST_SHIFT_TYPE.MORNING_SHIFT) {

      trigger('fromTime');
      trigger('toTime');

      setValue('fromTime', '08:30');
      setValue('toTime', '12:00');

      toggleShowOptions();

      return;
    }

    trigger('fromTime');
    trigger('toTime');

    setValue('fromTime', '13:00');
    setValue('toTime', '17:30');

    toggleShowOptions();
  };

  const handleCancelSelectLeaveDayType = () => {
    if (type === 'create') {
      setShift(undefined)
      setValue('leaveRegisterDayType', LEAVE_REQUEST_DAY_TYPE.FULL_DAY);
      toggleShowOptions();
      return;
    }
    toggleShowOptions();
  };

  useEffect(() => {
    if (type === 'create') {
      if (watch('leaveRegisterDayType') === LEAVE_REQUEST_DAY_TYPE.HALF_DAY) {
        setShowOptions(true);
        setValue('returnWorkDate', undefined);
      } else {
        setShowOptions(false);
        setValue('returnWorkDate', undefined);
      }
    }
  }, [watch('leaveRegisterDayType')]);

  const handleFocus = () => {
    if (watch('leaveRegisterDayType') === LEAVE_REQUEST_DAY_TYPE.HALF_DAY) {
      setShowOptions(true);
      setIsCheck(true);
    }
  };

  const handleOnChange = (value: string) => {
    if (watch('leaveRegisterDayType') === LEAVE_REQUEST_DAY_TYPE.HALF_DAY) {
      setShowOptions(true);
      setIsCheck(true);
    } else {
      toggleShowOptions()
      setIsCheck(true);
    }
  }

  return (
    <FormGroup className="dayoff-day-type">
      <div className="dayoff-day-type-input">
        <FormInput control={control} name="leaveRegisterDayType" label="Thời gian nghỉ" type="select" onChange={(e) => handleOnChange(e?.target?.value)} onFocus={handleFocus}>
          <option className="dayoff-day-type-option" value={LEAVE_REQUEST_DAY_TYPE.FULL_DAY}>
            Cả ngày
          </option>
          <option className="dayoff-day-type-option" value={LEAVE_REQUEST_DAY_TYPE.HALF_DAY}>
            Nửa ngày
          </option>
        </FormInput>
      </div>

      <div className={`dayoff-day-type-options ${showOptions ? 'show' : 'hidden'}`}>
        <FormGroup>
          <Label>Chọn ca</Label>
          <Input placeholder="Chọn ca" type="select" value={shift ?? 'default'} onChange={e => setShift(e?.target?.value as LEAVE_REQUEST_SHIFT_TYPE)}>
            <option selected disabled value='default' >
              Ca làm
            </option>

            <option value={LEAVE_REQUEST_SHIFT_TYPE.MORNING_SHIFT} >
              {`Ca sáng (8h30 - 12h)`}
            </option>

            <option value={LEAVE_REQUEST_SHIFT_TYPE.AFTERNOON_SHIFT} >
              {`Ca chiều (13h - 17h30)`}
            </option>
          </Input>
        </FormGroup>

        <Flex gap={16} justify="center" style={{ marginTop: 32 }}>
          <Button type="button" outline onClick={handleCancelSelectLeaveDayType}>
            Huỷ
          </Button>
          <Button id="accept" type="button" disabled={!shift} color="primary" onClick={handleSelectLeaveDayType}>
            Áp dụng
          </Button>
        </Flex>
      </div>
    </FormGroup>
  );
};

export default SelectLeaveRegisterDayType;
