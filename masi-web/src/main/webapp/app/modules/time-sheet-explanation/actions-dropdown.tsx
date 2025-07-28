import ButtonIcon from 'app/components/button-icon/button-icon';
import AuthGuard from 'app/components/guards/auth-guard';
import { TIME_KEEPING_EXPLANATION_STATUS } from 'app/shared/model/enumerations/time-keeping-explanation.model';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { ITimeKeepingExplanation } from 'app/shared/model/time-keeping-explanation.model';
import React from 'react';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

interface IActionsDropdown {
  record: ITimeKeepingExplanation;
  toggleRejectReq: () => void;
  toggleAcceptReq: () => void;
  toggleUpdateReq: () => void;
  toggleCancelReq: () => void;
  toggleDeleteReq: () => void;
  setSelectedRecord: (record: string | null) => void;
  setSelectedRows: (selectedRows: ITimeKeepingExplanation[]) => void;
}

const ActionsDropdown = (props: IActionsDropdown) => {
  const {
    record,
    toggleRejectReq,
    toggleAcceptReq,
    toggleUpdateReq,
    toggleCancelReq,
    toggleDeleteReq,
    setSelectedRecord,
    setSelectedRows,
  } = props;

  const handleUpdateReq = () => {
    toggleUpdateReq();
    setSelectedRecord(record.id);
  };

  const handleDeleteReq = () => {
    toggleDeleteReq();
    setSelectedRecord(record.id);
  };

  const handleRejectReq = () => {
    toggleRejectReq();
    setSelectedRows([record]);
  };

  const handleAcceptReq = () => {
    toggleAcceptReq();
    setSelectedRows([record]);
  };

  const handleCancelReq = () => {
    toggleCancelReq();
    setSelectedRecord(record.id);
  };

  const disabledUpdate =
    record?.status === TIME_KEEPING_EXPLANATION_STATUS.APPROVED || record?.status === TIME_KEEPING_EXPLANATION_STATUS.REJECTED;
  const disabledCancel =
    record?.status === TIME_KEEPING_EXPLANATION_STATUS.APPROVED ||
    record?.status === TIME_KEEPING_EXPLANATION_STATUS.REJECTED ||
    record?.status === TIME_KEEPING_EXPLANATION_STATUS.CANCELLED;
  const disabledDelete = record?.status !== TIME_KEEPING_EXPLANATION_STATUS.CANCELLED;
  const disabledAcceptOrReject = record?.status !== TIME_KEEPING_EXPLANATION_STATUS.PENDING;

  return (
    <UncontrolledDropdown >
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more.svg" alt="more" />
      </DropdownToggle>
      <DropdownMenu container="body" className="actions-dropdown" >
          <DropdownItem disabled={disabledAcceptOrReject} onClick={handleRejectReq}>
            <ButtonIcon className="reject" icon={<img className="pointer" src="content/images/vuesax/linear/close.svg" alt="reject" />}>
              Từ chối
            </ButtonIcon>
          </DropdownItem>

          <DropdownItem disabled={disabledAcceptOrReject} onClick={handleAcceptReq}>
            <ButtonIcon className="accept" icon={<img className="pointer" src="content/images/vuesax/linear/tick.svg" alt="approve" />}>
              Xét duyệt
            </ButtonIcon>
          </DropdownItem>

          <AuthGuard permissionKey='TIME_SHEET_EXPLANATION.EDIT'>
            <DropdownItem disabled={disabledUpdate} onClick={handleUpdateReq}>
              <ButtonIcon className="update" icon={<img className="pointer" src="content/images/vuesax/linear/edit-active.svg" alt="edit" />}>
                Cập nhật
              </ButtonIcon>
            </DropdownItem>
          </AuthGuard>

          <DropdownItem disabled={disabledCancel} onClick={handleCancelReq}>
            <ButtonIcon className="cancel" icon={<img className="pointer" src="content/images/vuesax/linear/slash.svg" alt="cancel" />}>
              Huỷ
            </ButtonIcon>
          </DropdownItem>

          <DropdownItem disabled={disabledDelete} onClick={handleDeleteReq}>
            <ButtonIcon className="delete" icon={<img className="pointer" src="content/images/vuesax/linear/trash.svg" alt="delete" />}>
              Xoá
            </ButtonIcon>
          </DropdownItem>
      </DropdownMenu>
    </UncontrolledDropdown>
  );
};

export default ActionsDropdown;
