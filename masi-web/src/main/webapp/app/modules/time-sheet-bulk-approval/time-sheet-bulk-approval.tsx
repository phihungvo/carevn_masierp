import './time-sheet-bulk-approval.scss';
import Card from 'app/components/card/card';
import { Typography } from 'app/components/typography/typography';
import React, { useState } from 'react';
import { TimeSheetBulkApprovalHeader } from './time-sheet-bulk-approval-components';
import { ModalAcceptBulkTimeSheet, ModalRejectBulkTimeSheet } from './time-sheet-bulk-approval-modals';
import { TimeSheetBulkApprovalTable } from './time-sheet-bulk-approval-table';
import { ITimeKeepingMonthly, ITimeKeepingMonthlyParams } from 'app/shared/model/time-keeping-monthly.model';
import { DATE_FORMAT, DEFAULT_PAGE, DEFAULT_PAGE_SIZE } from 'app/constants/common';
import { useModalsTimesheetBulkApproval } from 'app/hooks/use-modals-timesheet-bulk-approval';
import dayjs from 'dayjs';
import useTimeKeepingMonthly from 'app/hooks/use-time-keeping-monthly';
import { TIME_KEEPING_MONTHLY_STATUS } from 'app/shared/model/enumerations/time-keeping-monthly.model';
import { WORKSPACE_TYPE } from 'app/shared/model/enumerations/workspace.model';
import { TIME_SHEET_TYPE } from 'app/shared/model/enumerations/time-keeping.model';
import TimeSheetBulkApprovalDetailModals from './modals/time-sheet-bulk-approval-detail-modals';

const { useGetTimeKeepingMonthlyQuery } = useTimeKeepingMonthly;

const TimeSheetBulkApproval = () => {
  const [{ openAccept, toggleAccept }, { openReject, toggleReject }, { openDetail, toggleDetail }] = useModalsTimesheetBulkApproval();
  const [selectedRecord, setSelectedRecord] = useState<string | null>(null);
  const [selectedRowKeys, setSelectedRowKeys] = useState<string[]>([]);
  const [selectedRows, setSelectedRows] = useState<ITimeKeepingMonthly[]>([]);
  const [filter, setFilter] = useState<ITimeKeepingMonthlyParams>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    month: dayjs().format(DATE_FORMAT.YEAR_DATE),
    workspaceType: WORKSPACE_TYPE.OFFICE,
    type: TIME_SHEET_TYPE.HOUR,
  });

  const { data } = useGetTimeKeepingMonthlyQuery(filter);
  const isAllApproved = data?.data?.slice(0, data?.data?.length - 2).every(item => {
    return item?.status === TIME_KEEPING_MONTHLY_STATUS.APPROVED;
  });
  const isAllRejected = data?.data?.slice(0, data?.data?.length - 2).every(item => {
    return item?.status === TIME_KEEPING_MONTHLY_STATUS.REJECTED;
  });
  const disableBtnReject =
    isAllApproved ||
    isAllRejected ||
    selectedRows.some(item => {
      return item?.status !== TIME_KEEPING_MONTHLY_STATUS.PENDING;
    });
  const disabledApprove =
    isAllApproved ||
    selectedRows.some(item => {
      return item?.status === TIME_KEEPING_MONTHLY_STATUS.APPROVED;
    });

  return (
    <div className="time-sheet-monthly page_container">
      <Typography level={4}>Duyệt chấm công theo tháng</Typography>

      <Card
        header={
          <TimeSheetBulkApprovalHeader
            toggleModalAcceptBulkTimeSheet={toggleAccept}
            toggleModalRejectBulkTimeSheet={toggleReject}
            disableBtnReject={disableBtnReject}
            disabledApprove={disabledApprove}
            filter={filter}
            setFilter={setFilter}
          />
        }
      >
        <TimeSheetBulkApprovalTable
          setSelectedRecord={setSelectedRecord}
          setSelectedRows={setSelectedRows}
          setSelectedRowKeys={setSelectedRowKeys}
          selectedRowKeys={selectedRowKeys}
          filter={filter}
          setFilter={setFilter}
          data={data}
          toggleDetail={toggleDetail}
        />
      </Card>

      <ModalAcceptBulkTimeSheet
        isOpen={openAccept}
        toggle={toggleAccept}
        filter={filter}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
      />

      <ModalRejectBulkTimeSheet
        isOpen={openReject}
        toggle={toggleReject}
        filter={filter}
        selectedRowKeys={selectedRowKeys}
        setSelectedRowKeys={setSelectedRowKeys}
      />

      <TimeSheetBulkApprovalDetailModals isOpen={openDetail} toggle={toggleDetail} selectedRecord={selectedRecord} />
    </div>
  );
};

export default TimeSheetBulkApproval;
