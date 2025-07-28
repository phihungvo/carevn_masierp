import Button from 'app/components/button/button';
import FilterDate from 'app/components/filter-date/filter-date';
import AuthGuard from 'app/components/guards/auth-guard';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { ITimeKeepingViolationsParams } from 'app/shared/model/time-keeping-violation.model';
import dayjs from 'dayjs';
import React, { useEffect, useRef, useState } from 'react';
import { DateObject } from 'react-multi-date-picker';
import { useNavigate, useParams } from 'react-router';

interface ITimeSheetViolationHeader {
  toggleModalFilter: () => void;
  toggleModalCreateTimeSheetExplanation: () => void;
  setFilter: React.Dispatch<React.SetStateAction<ITimeKeepingViolationsParams>>;
  filter: ITimeKeepingViolationsParams;
}

const TimeSheetViolationHeader = (props: ITimeSheetViolationHeader) => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { toggleModalFilter, toggleModalCreateTimeSheetExplanation, setFilter, filter } = props;
  const datePickerFilterRef = useRef<any>(null);
  const [date, setDate] = useState<DateObject>(new DateObject());
  const closeFilterCalendar = () => {
    setDate(null);
    datePickerFilterRef.current?.closeCalendar();
  }

  const filterReportDates = () => {
    datePickerFilterRef.current?.closeCalendar();
  }
  useEffect(() => {
    if (date && date.isValid) {
      setFilter(prev => ({ ...prev, fromDate: dayjs(date?.format('YYYY-MM-DD')).startOf('month').format('YYYY-MM-DD'), toDate: dayjs(date?.format('YYYY-MM-DD')).endOf('month').format('YYYY-MM-DD') }));
    }
  }, [date]);
  const handleOnBack = () => {
    navigate('/time-sheet/explanation');
  }
  return (
    <div className="card-header-container">
      <div className="card-header-extra" />
      <div className="card-header-extra">

        {!id && <>
          <FilterDate
            display={'Tháng ' + date?.format('MM/YYYY')}
            value={date}
            setSelectedDate={setDate}
            ref={datePickerFilterRef}
            onReset={closeFilterCalendar}
            onOk={filterReportDates}
            onlyMonthPicker
          />
            <AuthGuard permissionKey='TIME_SHEET_VIOLATION.CREATE'>
              <Button
                color="primary" onClick={toggleModalCreateTimeSheetExplanation}>
                Giải trình
              </Button>
            </AuthGuard>
          </>}
        {id && <Button
          color="primary"
          onClick={handleOnBack}
        >
          Quay lại
        </Button>}
      </div>
    </div>
  );
};

export default TimeSheetViolationHeader;
