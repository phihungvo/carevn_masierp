import ButtonIcon from 'app/components/button-icon/button-icon';
import { LEAVE_REGIME_STATUS } from 'app/shared/model/enumerations/leave-regime.model';
import AuthGuard from 'app/components/guards/auth-guard';
import { ILeaveRegime } from 'app/shared/model/leave-regime.model';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import React from 'react';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

interface IActionsDropdownProps {
  toggleUpdate: () => void;
  toggleApproveSign: () => void;
  toggleCancel: () => void;
  toggleReject: () => void;
  toggleDelete: () => void;
  togglePropose: () => void;
  toggleDetail: () => void;
  record: ILeaveRegime;
  setSelectedRecord: (id: string) => void;
}

const ActionsDropdown = (props: IActionsDropdownProps) => {
  const {
    toggleApproveSign,
    toggleUpdate,
    toggleDelete,
    toggleReject,
    toggleCancel,
    togglePropose,
    toggleDetail,
    record,
    setSelectedRecord,
  } = props;

  const handleDetail = () => {
    setSelectedRecord(record.id);
    toggleDetail();
  };

  const handleUpdate = () => {
    setSelectedRecord(record.id);
    toggleUpdate();
  };

  const handleApprove = () => {
    setSelectedRecord(record.id);
    toggleApproveSign();
  };

  const handleCancel = () => {
    setSelectedRecord(record.id);
    toggleCancel();
  };

  const handleReject = () => {
    setSelectedRecord(record.id);
    toggleReject();
  };

  const handleDelete = () => {
    setSelectedRecord(record.id);
    toggleDelete();
  };

  const handlePropose = () => {
    setSelectedRecord(record.id);
    togglePropose();
  };

  const disabledUpdate = record?.status !== LEAVE_REGIME_STATUS.NEW;
  const disabledPropose = record?.status === LEAVE_REGIME_STATUS.APPROVED || record?.status === LEAVE_REGIME_STATUS.WAITING_APPROVAL;
  // || record?.status === LEAVE_REGIME_STATUS.WAITING_APPROVAL;
  const disabledApprove = record?.status === LEAVE_REGIME_STATUS.APPROVED || record?.status !== LEAVE_REGIME_STATUS.WAITING_APPROVAL;
  const disabledCancel = record?.status !== LEAVE_REGIME_STATUS.WAITING_APPROVAL;
  const disabledReject =
    record?.status === LEAVE_REGIME_STATUS.REJECTED ||
    record?.status === LEAVE_REGIME_STATUS.APPROVED ||
    record?.status === LEAVE_REGIME_STATUS.NEW;
  const disabledDelete = record?.status !== LEAVE_REGIME_STATUS.CANCEL;

  return (
    <UncontrolledDropdown>
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more.svg" alt="more" />
      </DropdownToggle>
      <DropdownMenu container="body" className="actions-dropdown">
          <AuthGuard permissionKey='LEAVE_REGISTER.VIEW'>
            <DropdownItem onClick={handleDetail}>
              <ButtonIcon className="detail" icon={<img className="pointer" src="content/images/vuesax/linear/eye.svg" alt="detail" />}>
                Chi tiết
              </ButtonIcon>
            </DropdownItem>
          </AuthGuard>

          <AuthGuard permissionKey='LEAVE_REGISTER.EDIT'>
            <DropdownItem onClick={handleUpdate} disabled={disabledUpdate}>
              <ButtonIcon
                className="update"
                icon={<img className="pointer" src="content/images/vuesax/linear/edit-active.svg" alt="update" />}
              >
                Cập nhật
              </ButtonIcon>
            </DropdownItem>
  
            <DropdownItem onClick={handlePropose} disabled={disabledPropose}>
              <ButtonIcon
                className="propose"
                icon={<img className="pointer" src="content/images/vuesax/linear/directbox-notif.svg" alt="propose" />}
              >
                Yêu cầu xét duyệt
              </ButtonIcon>
            </DropdownItem>
  
            <DropdownItem onClick={handleApprove} disabled={disabledApprove}>
              <ButtonIcon
                className="approve"
                icon={<img className="pointer" src="content/images/vuesax/linear/receipt-search.svg" alt="approve" />}
              >
                Xét duyệt
              </ButtonIcon>
            </DropdownItem>
  
            <DropdownItem onClick={handleReject} disabled={disabledReject}>
              <ButtonIcon
                className="reject-warning"
                icon={<img className="pointer" src="content/images/vuesax/linear/folder-cross.svg" alt="reject-warning" />}
              >
                Từ chối
              </ButtonIcon>
            </DropdownItem>
            <DropdownItem onClick={handleCancel} disabled={disabledCancel}>
              <ButtonIcon className="cancel" icon={<img className="pointer" src="content/images/vuesax/linear/close.svg" alt="cancel" />}>
                Huỷ
              </ButtonIcon>
            </DropdownItem>
  
            <DropdownItem onClick={handleDelete} disabled={disabledDelete}>
              <ButtonIcon className="delete" icon={<img className="pointer" src="content/images/vuesax/linear/trash.svg" alt="delete" />}>
                Xoá
              </ButtonIcon>
            </DropdownItem>
          </AuthGuard>
      </DropdownMenu>
    </UncontrolledDropdown>
  );
};

export default ActionsDropdown;
