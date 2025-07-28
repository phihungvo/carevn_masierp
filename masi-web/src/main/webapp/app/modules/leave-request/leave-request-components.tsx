import Button from 'app/components/button/button';
import AuthGuard from 'app/components/guards/auth-guard';
import { LEAVE_REQUEST_STATUS } from 'app/shared/model/enumerations/leave-request.model';
import { ILeaveRequest } from 'app/shared/model/leave-request.model';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import dayjs from 'dayjs';
import React, { Dispatch, SetStateAction, useMemo } from 'react';

import isSameOrAfter from 'dayjs/plugin/isSameOrAfter';
import ButtonIcon from 'app/components/button-icon/button-icon';
import { ILeaveRequestParams } from 'app/shared/model/leave-request.model';
import { useAppSelector } from 'app/config/store';

dayjs.extend(isSameOrAfter);

interface ILeaveRequestHeader {
  toggleLeaveRequest: () => void;
  toggleCancelLeaveRequest: () => void;
  toggleDeleteRequest: () => void;
  toggleFilterRequest: () => void;
  toggleAcceptRequest: () => void;
  toggleRejectRequest: () => void;
  selectedRows: ILeaveRequest[];
  toggleModalDownload: () => void;
  setFilter: Dispatch<SetStateAction<ILeaveRequestParams>>;
}

export const LeaveRequestHeader = (props: ILeaveRequestHeader) => {
  const {
    toggleLeaveRequest,
    toggleCancelLeaveRequest,
    toggleDeleteRequest,
    toggleFilterRequest,
    toggleAcceptRequest,
    toggleRejectRequest,
    selectedRows,
    toggleModalDownload,
    setFilter,
  } = props;

  const account = useAppSelector(state => state.authentication.account);

  const { disableAccept, disableCancel, disableDelete, disableReject } = useMemo(() => {
    if (!selectedRows.length) {
      return {
        disableCancel: true,
        disableDelete: true,
        disableAccept: true,
        disableReject: true,
      };
    }

    return {
      disableCancel: selectedRows.some(
        leave_request => dayjs().isSameOrAfter(leave_request?.fromDate) || leave_request?.status === LEAVE_REQUEST_STATUS.CANCELLED,
      ),
      disableDelete: selectedRows.some(leave_request => leave_request?.status !== LEAVE_REQUEST_STATUS.CANCELLED),
      disableAccept: selectedRows.some(
        leave_request =>
          !!dayjs().isSameOrAfter(leave_request?.toDate) ||
          leave_request?.status !== LEAVE_REQUEST_STATUS.PENDING ||
          !leave_request?.reviews.some(r => r.reviewer?.id === account?.id),
      ),
      disableReject: selectedRows.some(
        leave_request =>
          !!dayjs().isSameOrAfter(leave_request?.toDate) ||
          leave_request?.status !== LEAVE_REQUEST_STATUS.PENDING ||
          !leave_request?.reviews.some(r => r?.reviewer?.id === account?.id),
      ),
    };
  }, [selectedRows]);

  return (
    <div className="card-header-container">
      <div className="card-header-extra"></div>
      <div className="card-header-extra">
        <Button className="btn-filter" onClick={toggleFilterRequest}>
          Lọc <img src="content/images/vuesax/linear/sort.svg" alt="filter" />
        </Button>
          <AuthGuard permissionKey='LEAVE_REQUEST.EDIT'>
            <Button className='default-buttton' disabled={disableCancel} color="primary" onClick={toggleCancelLeaveRequest}>
              Huỷ
            </Button>
            <Button className='default-buttton' disabled={disableDelete} color="primary" onClick={toggleDeleteRequest}>
              Xoá
            </Button>
            <Button className='default-buttton' disabled={disableReject} color="primary" onClick={toggleRejectRequest}>
              Từ chối
            </Button>
            <Button className='default-buttton' disabled={disableAccept} color="primary" onClick={toggleAcceptRequest}>
              Xét duyệt
            </Button>
          </AuthGuard>
          <AuthGuard permissionKey='LEAVE_REQUEST.CREATE'>
            <Button className='default-buttton' color="primary" onClick={toggleLeaveRequest}>
              Tạo mới
            </Button>
          </AuthGuard>

          <AuthGuard permissionKey='LEAVE_REQUEST.EXPORT'>
            <ButtonIcon
              onClick={toggleModalDownload}
              icon={<img className="document-download" src="content/images/vuesax/linear/document-download.svg" alt="download" />}
            />
          </AuthGuard>
      </div>
    </div>
  );
};
