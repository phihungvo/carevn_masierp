import React, { useState } from 'react';

import './time-sheet.scss';
import Card from 'app/components/card/card';
import { PATH } from 'app/constants/path';
import { useLocation } from 'react-router';
import { TimeSheetTable } from './time-sheet-table';
import { DateObject } from 'react-multi-date-picker';
import { TimeSheetHeader } from './time-sheet-components';
import { TimeSheetBulkTable } from './time-sheet-bulk-table';
import { Typography } from 'app/components/typography/typography';
import { useModalsTimesheet } from 'app/hooks/use-modals-timesheet';
import { WORKSPACE_TYPE } from 'app/shared/model/enumerations/workspace.model';
import { TIME_SHEET_TYPE } from 'app/shared/model/enumerations/time-keeping.model';
import { ModalTimeCheck, ModalUpdateTimeCheck, ModalUpdateTimeCheckSuccess } from './time-sheet-modals';
import { getStartAndEndOfWeek } from 'app/shared/util/date-utils';

export enum TimeCheckType {
  CHECK_IN = 'check-in',
  CHECK_OUT = 'check-out',
}

const TimeSheet = () => {
  const { pathname } = useLocation();

  const [{ isOpen, toggle }, { isOpenUpdateTimeCheck, toggleUpdateTimeCheck }, { isUpdateTimeCheckSuccess, toggleUpdateSuccess }] =
    useModalsTimesheet();
  const { startOfWeek, endOfWeek } = getStartAndEndOfWeek(new Date(), 'YYYY-MM-DD');
  const [selectedDate, setSelectedDate] = useState<DateObject[]>([
    new DateObject(startOfWeek),
    new DateObject(endOfWeek),
  ]);
  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [timeCheckType, setTimeCheckType] = useState<TIME_SHEET_TYPE | null>(TIME_SHEET_TYPE.HOUR);
  const [workSpaceTypes, setWorkSpaceTypes] = useState<WORKSPACE_TYPE>(WORKSPACE_TYPE.OFFICE);
  const [inOrOut, setInOrOut] = useState<TimeCheckType>(TimeCheckType.CHECK_IN);

  return (
    <div className='page_container' {...(pathname === PATH.TIME_SHEET_BULK && { className: 'time-sheet-check page_container' })}>
      <Typography level={4}>{pathname === PATH.TIME_SHEET_BULK ? 'Bảng chấm công hàng loạt' : 'Lịch sử chấm công'}</Typography>
      <Card
        header={
          <TimeSheetHeader
            toggleTimeSheetModal={toggle}
            selectedDate={selectedDate}
            setSelectedDate={setSelectedDate}
            setTimeCheckType={setTimeCheckType}
            timeCheckType={timeCheckType}
            setInOrOut={setInOrOut}
            workSpaceTypes={workSpaceTypes}
            setWorkSpaceTypes={setWorkSpaceTypes}
          />
        }
      >
        {/* TIME CHECK TABLE */}
        {pathname === PATH.TIME_SHEET_BULK ? (
          <TimeSheetBulkTable selectedDate={selectedDate} timeCheckType={timeCheckType} workSpaceTypes={workSpaceTypes} />
        ) : (
          <TimeSheetTable
            selectedDate={selectedDate}
            timeCheckType={timeCheckType}
            toggleUpdateTimeCheck={toggleUpdateTimeCheck}
            setSelectedRecord={setSelectedRecord}
            workSpaceTypes={workSpaceTypes}
          />
        )}
      </Card>

      {/* MODAL CHECKING TIME SHEET */}
      <ModalTimeCheck isOpen={isOpen} toggle={toggle} inOrOut={inOrOut} />

      {/* MODAL UPDATE TIME CHECK */}
      <ModalUpdateTimeCheck
        isOpen={isOpenUpdateTimeCheck}
        toggle={toggleUpdateTimeCheck}
        toggleSuccess={toggleUpdateSuccess}
        selectedRecord={selectedRecord}
      />

      {/* MODAL UPDATE TIME CHECK SUCCESS */}
      <ModalUpdateTimeCheckSuccess isOpen={isUpdateTimeCheckSuccess} toggle={toggleUpdateSuccess} cancel={false} />
    </div>
  );
};

export default TimeSheet;
