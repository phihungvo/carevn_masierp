import { ColumnsTypes } from 'app/components/table/table.d';
import React from 'react';
import { useMemo } from 'react';
import ActionsDropdown from './components/actions-dropdown';
import dayjs from 'dayjs';
import { DATE_FORMAT } from 'app/constants/common';
import Badge from 'app/components/badge/badge';
import leaveRegisterMapping from './leave-register-mapping';
import { ILeaveRegime } from 'app/shared/model/leave-regime.model';
import leaveRequestMapping from '../leave-request/leave-request-mapping';
import { LEAVE_REGIME_STATUS } from 'app/shared/model/enumerations/leave-regime.model';
import PopoverApproval from './components/popover-approval';
import Tooltip from 'app/components/tooltip/tooltip';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import { LEAVE_REQUEST_DAY_TYPE } from 'app/shared/model/enumerations/leave-request.model';

const { mapLeaveRequestType } = leaveRequestMapping;
const { leaveRegimeStatusColorMapping, leaveRegimeStatusTextMapping, leaveRegimeTextMapping } = leaveRegisterMapping;


export const generateColumns = (
  toggleApproveSign: () => void,
  toggleCancel: () => void,
  toggleDelete: () => void,
  toggleReject: () => void,
  toggleUpdate: () => void,
  togglePropose: () => void,
  toggleDetail: () => void,
  setSelectedRecord: (id: string) => void,
): ColumnsTypes<ILeaveRegime> => {
  const handleDetail = (id: string) => {
    setSelectedRecord(id);
    toggleDetail();
  };

  const columns: ColumnsTypes<ILeaveRegime> = useMemo(() => {
    return [
      {
        title: 'Tên nhân viên',
        dataIndex: 'employeeId',
        key: 'employeeId',
        render: (text, record) => (
          <Tooltip label={(record?.employee?.lastName || '') + ' ' + (record?.employee?.firstName || '')} target={`employeeId-${record.id}`}>
            <p className="attachment-link" onClick={() => handleDetail(record.id)}>
              <EllipsisParagraph text={(record?.employee?.lastName || '') + ' ' + (record?.employee?.firstName || '')} width={150} id={`employeeId-${record.id}`} />
            </p>
          </Tooltip>
        )
      },
      {
        title: 'Loại nghỉ phép',
        dataIndex: 'leaveType',
        key: 'leaveType',
        render: (text, record) => (
          <Tooltip label={mapLeaveRequestType(text)} target={`leaveType-${record.id}`}>
            <EllipsisParagraph text={mapLeaveRequestType(text)} width={150} id={`leaveType-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Ngày cuối cùng làm việc',
        dataIndex: 'lastWorkDate',
        key: 'lastWorkDate',
        width: 230,
        render: text => (text ? dayjs(text).format(DATE_FORMAT.DATE) : ''),
      },
      {
        title: 'Ngày trở lại làm việc',
        dataIndex: 'returnWorkDate',
        key: 'returnWorkDate',
        width: 200,
        render: text => (text ? dayjs(text).format(DATE_FORMAT.DATE) : ''),
      },
      {
        title: 'Tổng số ngày nghỉ',
        dataIndex: 'leaveDays',
        key: 'leaveDays',
        width: 200,
        render: (_, record) => {
          const startWorkDate = dayjs(record?.lastWorkDate).startOf('date');
          const returnWorkDate = dayjs(record?.returnWorkDate).startOf('date');
          const diffDate = returnWorkDate.diff(startWorkDate, 'day') - 1;
          return record?.totalDayOff|| (record?.returnWorkDate && record?.lastWorkDate ? (diffDate < 0 ? 0 : diffDate) : 0);
        },
      },
      {
        title: 'Thời gian nghỉ',
        key: 'leaveRequestDayType',
        width: 250,
        render: (_, record) => <p>
          {record?.leaveRequestDayType === LEAVE_REQUEST_DAY_TYPE.HALF_DAY ?
            `${leaveRegimeTextMapping(record?.leaveRequestDayType)} ( ${dayjs(record?.fromTime).format('HH:mm') ?? ''} - ${dayjs(record?.toTime).format('HH:mm') ?? ''} )` :
            leaveRegimeTextMapping(record?.leaveRequestDayType)}
        </p>
      },
      {
        key: 'approverId',
        title: 'Người duyệt',
        dataIndex: 'approverId',
        render: (text, record) => (
          <Tooltip label={
            record?.processLeaveRegimeRequests
              ?.map(item => (item?.approver?.lastName || '') + ' ' + (item?.approver?.firstName || ''))
              .join(', ')
          } target={`approverId-${record.id}`}>
            <EllipsisParagraph text={
              record?.processLeaveRegimeRequests
                ?.map(item => (item?.approver?.lastName || '') + ' ' + (item?.approver?.firstName || ''))
                .join(', ')
            } id={`approverId-${record.id}`} />
          </Tooltip>
        )
      },
      // {
      //   key: 'fileAttachment',
      //   title: 'Đính kèm',
      //   dataIndex: 'fileAttachment',
      //   width: 120,
      //   render: (_, record) => (
      //     <Flex direction="column" gap={12}>
      //       {record?.files?.map((item: IBodyFile) => (
      //         <a className="file-attachment" href={`${FILE_UTIL}/${item?.id}`} target="_blank">
      //           {item?.fileName?.slice(0, 8)}...
      //         </a>
      //       ))}
      //     </Flex>
      //   ),
      // },
      {
        key: 'substituteId',
        title: 'Người thay thế',
        dataIndex: 'substituteId',
        render: (text, record) => (
          <Tooltip label={(record?.substitute?.lastName || '') + ' ' + (record?.substitute?.firstName || '')} target={`substituteId-${record.id}`}>
            <EllipsisParagraph text={(record?.substitute?.lastName || '') + ' ' + (record?.substitute?.firstName || '')} id={`substituteId-${record.id}`} />
          </Tooltip>
        )
      },
      {
        title: 'Trạng thái',
        dataIndex: 'status',
        key: 'status',
        render: (text, record) =>
          record?.status === LEAVE_REGIME_STATUS.WAITING_APPROVAL ? (
            <PopoverApproval id={`review-${record.id}`} data={record?.processLeaveRegimeRequests}>
              <Badge id={`review-${record.id}`} color={leaveRegimeStatusColorMapping(text)}>
                {leaveRegimeStatusTextMapping(text)}
              </Badge>
            </PopoverApproval>
          ) : (
            <Badge color={leaveRegimeStatusColorMapping(text)}>{leaveRegimeStatusTextMapping(text)}</Badge>
          ),
      },
      {
        title: 'Thao tác',
        key: 'action',
        width: 106,
        render: (_, record) => (
          <ActionsDropdown
            toggleApproveSign={toggleApproveSign}
            toggleCancel={toggleCancel}
            toggleDelete={toggleDelete}
            toggleReject={toggleReject}
            toggleUpdate={toggleUpdate}
            togglePropose={togglePropose}
            toggleDetail={toggleDetail}
            record={record}
            setSelectedRecord={setSelectedRecord}
          />
        ),
      },
    ];
  }, []);

  return columns;
};
