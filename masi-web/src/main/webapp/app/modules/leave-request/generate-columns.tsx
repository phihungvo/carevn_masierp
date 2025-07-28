import Badge from 'app/components/badge/badge';
import ButtonIcon from 'app/components/button-icon/button-icon';
import Flex from 'app/components/flex/flex';
import { ColumnsTypes } from 'app/components/table/table.d';
import Tooltip from 'app/components/tooltip/tooltip';
import { DATE_FORMAT } from 'app/constants/common';
import { LEAVE_REQUEST_DAY_TYPE, LEAVE_REQUEST_STATUS, LEAVE_REQUEST_TYPE } from 'app/shared/model/enumerations/leave-request.model';
import { ILeaveRequest } from 'app/shared/model/leave-request.model';
import dayjs from 'dayjs';
import React, { useMemo } from 'react';
import dayOffMapping from './leave-request-mapping';
import isSameOrAfter from 'dayjs/plugin/isSameOrAfter';
import { useAppSelector } from 'app/config/store';
import AuthGuard from 'app/components/guards/auth-guard';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import leaveRegisterMapping from '../leave-register/leave-register-mapping';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

dayjs.extend(isSameOrAfter);

const { mapLeaveRequestStatusColor, mapLeaveRequestStatus, mapLeaveRequestType } = dayOffMapping;
const { leaveRegimeTextMapping } = leaveRegisterMapping;


export const generateColumns = (
  handleDownloadFile: (fileBase64: string, fileName: string, fileType: string) => void,
  handleCancelLeaveRequest: (record: ILeaveRequest) => void,
  handleAcceptLeaveReq: (record: ILeaveRequest) => void,
  handleRejectLeaveReq: (record: ILeaveRequest) => void,
  handleDeleteLeaveRequest: (record: ILeaveRequest) => void,
  handleDetailLeaveRequest: (record: ILeaveRequest) => void,
): ColumnsTypes<ILeaveRequest> => {
  const account = useAppSelector(state => state.authentication.account);

  const columns: ColumnsTypes<ILeaveRequest> = useMemo(() => {
    return [
      {
        key: 'employeeName',
        title: 'Tên NV',
        render: (text, record) => (
          <Tooltip label={(record?.employee?.lastName || '') + ' ' + (record?.employee?.firstName || '')} target={`employeeName-${record.id}`}>
            <EllipsisParagraph text={<p className="attachment-link" onClick={() => handleDetailLeaveRequest(record)}>
              {(record?.employee?.lastName || '') + ' ' + (record?.employee?.firstName || '')}
            </p>} id={`employeeName-${record.id}`} />
          </Tooltip>
        )
      },
      {
        key: 'leaveRequestDayType',
        title: 'Loại nghỉ phép',
        dataIndex: 'leaveRequestType',
        render: (text, record) => (
          <Tooltip label={mapLeaveRequestType(text as LEAVE_REQUEST_TYPE)} target={`leaveRequestDayType-${record.id}`}>
            <EllipsisParagraph text={mapLeaveRequestType(text as LEAVE_REQUEST_TYPE)} width={150} id={`leaveRequestDayType-${record.id}`} />
          </Tooltip>
        )
      },
      {
        key: 'totalDayOff',
        title: 'Tổng số ngày nghỉ',
        dataIndex: 'totalDayOff',
      },
      {
        title: 'Thời gian nghỉ',
        key: 'leaveRequestDayType',
        width: 250,
        render: (_, record) => <p>
          {record?.leaveRequestDayType === LEAVE_REQUEST_DAY_TYPE.HALF_DAY ?
            `${leaveRegimeTextMapping(record?.leaveRequestDayType)} ( ${dayjs(record?.fromTime).format('HH:mm') ?? ''} - ${dayjs(record?.toTime).format('HH:mm') ?? ''} )` :
            leaveRegimeTextMapping(record?.leaveRequestDayType as LEAVE_REQUEST_DAY_TYPE)}
        </p>
      },
      {
        key: 'fromDate',
        title: 'Ngày cuối cùng làm việc',
        dataIndex: 'fromDate',
        render: text => (text ? dayjs(text).format(DATE_FORMAT.DATE) : ''),
      },
      {
        key: 'toDate',
        title: 'Ngày trở lại làm việc',
        dataIndex: 'toDate',
        render: text => (text ? dayjs(text).format(DATE_FORMAT.DATE) : ''),
      },
      {
        key: 'replacement',
        title: 'Người thay thế',
        render: (text, record) => (
          <Tooltip label={(record?.substitute?.lastName || '') + ' ' + (record?.substitute?.firstName || '')} target={`replacement-${record.id}`}>
            <EllipsisParagraph text={(record?.substitute?.lastName || '') + ' ' + (record?.substitute?.firstName || '')} width={150} id={`replacement-${record.id}`} />
          </Tooltip>
        )
      },
      {
        key: 'sensor',
        title: 'Người duyệt',
        dataIndex: 'sensor',
        render: (text, record) => (
          <Tooltip label={record?.reviews?.map(r => (r?.reviewer?.lastName || '') + ' ' + (r?.reviewer?.firstName || '')).join(', ')} target={`sensor-${record.id}`}>
            <EllipsisParagraph text={record?.reviews?.map(r => (r?.reviewer?.lastName || '') + ' ' + (r?.reviewer?.firstName || '')).join(', ')} width={150} id={`sensor-${record.id}`} />
          </Tooltip>
        )
      },
      {
        key: 'status',
        title: 'Trạng thái',
        dataIndex: 'status',
        render: text => (
          <Badge color={mapLeaveRequestStatusColor(text as LEAVE_REQUEST_STATUS)}>
            {mapLeaveRequestStatus(text as LEAVE_REQUEST_STATUS)}
          </Badge>
        ),
      },
      {
        key: 'actions',
        title: 'Thao tác',
        fixed: 'right',
        render: (_, record) => {
          const disabledApprove = !record?.reviews?.some(r => r?.reviewerId === account?.id);
          return (
            <UncontrolledDropdown>
              <DropdownToggle className="actions-dropdown-toggle">
                <img src="content/images/vuesax/linear/more.svg" alt="more" />
              </DropdownToggle>
              <DropdownMenu container="body" className="actions-dropdown">
                  <DropdownItem
                    onClick={() => handleDetailLeaveRequest(record)}
                  >
                    <ButtonIcon
                      className="detail"
                      icon={
                        <img
                          className="pointer"
                          src="content/images/vuesax/linear/eye.svg"
                          alt="detail"
                        />
                      }
                    >
                      Chi tiết
                    </ButtonIcon>
                  </DropdownItem>
                {record?.status === LEAVE_REQUEST_STATUS.PENDING &&
                  !disabledApprove && (
                    <>
                        <DropdownItem
                          onClick={() => handleAcceptLeaveReq(record)}
                          disabled={disabledApprove}
                        >
                          <ButtonIcon
                            className="approve"
                            icon={
                              <img
                                className="pointer"
                                src="content/images/vuesax/linear/receipt-search.svg"
                                alt="approve"
                              />
                            }
                          >
                            Xét duyệt
                          </ButtonIcon>
                        </DropdownItem>
                        <DropdownItem
                          onClick={() => handleRejectLeaveReq(record)}
                        >
                          <ButtonIcon
                            className="cancel"
                            icon={
                              <img
                                className="pointer"
                                src="content/images/vuesax/linear/close.svg"
                                alt="reject"
                              />
                            }
                          >
                            Từ chối
                          </ButtonIcon>
                        </DropdownItem>
                    </>
                  )}

                {record?.status !== LEAVE_REQUEST_STATUS.CANCELLED &&
                  record?.status !== LEAVE_REQUEST_STATUS.APPROVED &&
                  !dayjs().isSameOrAfter(record?.toDate) && (
                      <DropdownItem
                        onClick={() => handleCancelLeaveRequest(record)}
                      >
                        <ButtonIcon
                          className="cancel"
                          icon={
                            <img
                              className="pointer"
                              src="content/images/vuesax/linear/slash.svg"
                              alt="cancel"
                            />
                          }
                        >
                          Hủy
                        </ButtonIcon>
                      </DropdownItem>
                  )}

                {record?.status === LEAVE_REQUEST_STATUS.CANCELLED && (
                  <DropdownItem
                    onClick={() => handleDeleteLeaveRequest(record)}
                  >
                    <ButtonIcon
                      className="delete"
                      icon={
                        <img
                          className="pointer"
                          src="content/images/vuesax/linear/trash.svg"
                          alt="delete"
                        />
                      }
                    >
                      Xoá
                    </ButtonIcon>
                  </DropdownItem>
                )}
              </DropdownMenu>
            </UncontrolledDropdown>
          );
        },
      },
    ];
  }, []);

  return columns;
};
