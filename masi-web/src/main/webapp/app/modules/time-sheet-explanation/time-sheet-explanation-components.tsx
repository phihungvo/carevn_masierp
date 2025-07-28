import './time-sheet-explanation.scss';
import React from 'react';
import InputSearch from 'app/components/input/input-search';
import Button from 'app/components/button/button';
import { ITimeKeepingExplanation } from 'app/shared/model/time-keeping-explanation.model';
import { TIME_KEEPING_EXPLANATION_STATUS } from 'app/shared/model/enumerations/time-keeping-explanation.model';
import AuthGuard from 'app/components/guards/auth-guard';
import { Action, PermissionResource } from 'app/shared/model/permission.model';

interface ITimeSheetExplanationHeader {
  toggleFilter: () => void;
  toggleAcceptReq: () => void;
  toggleRejectReq: () => void;
  toggleCancelReq: () => void;
  toggleDeleteReq: () => void;
  selectedRows: ITimeKeepingExplanation[];
  setSearchText: (text: string) => void;
}

export const TimeSheetExplanationHeader = (props: ITimeSheetExplanationHeader) => {
  const { toggleFilter, toggleAcceptReq, toggleRejectReq, toggleCancelReq, toggleDeleteReq, selectedRows, setSearchText } = props;

  const disabledCancel =
    !selectedRows.length ||
    selectedRows?.some(
      row =>
        row?.status === TIME_KEEPING_EXPLANATION_STATUS.APPROVED ||
        row?.status === TIME_KEEPING_EXPLANATION_STATUS.REJECTED ||
        row?.status === TIME_KEEPING_EXPLANATION_STATUS.CANCELLED,
    );
  const disabledDelete = !selectedRows.length || selectedRows.some(row => row?.status !== TIME_KEEPING_EXPLANATION_STATUS.CANCELLED);
  const disabledAcceptOrReject = !selectedRows.length || selectedRows.some(row => row?.status !== TIME_KEEPING_EXPLANATION_STATUS.PENDING);

  return (
    <div className="card-header-container">
      <InputSearch className="card-header-extra" onChange={e => setSearchText(e.target.value)} />

      <div className="card-header-extra">
        <Button className="btn-filter" onClick={toggleFilter}>
          Lọc <img src="content/images/vuesax/linear/sort.svg" alt="filter" />
        </Button>
          <Button className='default-buttton' disabled={disabledCancel} color="primary" onClick={toggleCancelReq}>
            Huỷ
          </Button>
          <Button className='default-buttton' disabled={disabledDelete} color="primary" onClick={toggleDeleteReq}>
            Xoá
          </Button>
          <Button className='default-buttton' disabled={disabledAcceptOrReject} color="primary" onClick={toggleRejectReq}>
            Từ chối
          </Button>
          <AuthGuard permissionKey='TIME_SHEET_EXPLANATION.CREATE'>
            <Button className='default-buttton' disabled={disabledAcceptOrReject} color="primary" onClick={toggleAcceptReq}>
              Xét duyệt
            </Button>
          </AuthGuard>
      </div>
    </div>
  );
};
