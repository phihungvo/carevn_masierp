import ButtonIcon from 'app/components/button-icon/button-icon';
import AuthGuard from 'app/components/guards/auth-guard';
import { IDocumentary } from 'app/shared/model/documentary.model';
import { DOCUMENTARY_STATUS } from 'app/shared/model/enumerations/documentary';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import React from 'react';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

interface IActionsDropdown {
  toggleDetail: () => void;
  toggleUpdate: () => void;
  toggleDelete: () => void;
  togglePropose: () => void;
  toggleApprove: () => void;
  record: IDocumentary;
  setSelectedRecord: (id: string) => void;
}

const ActionsDropdown = (props: IActionsDropdown) => {
  const { toggleDetail, toggleUpdate, toggleDelete, togglePropose, toggleApprove, record, setSelectedRecord } = props;

  const handleDetail = () => {
    setSelectedRecord(record.id);
    toggleDetail();
  };

  const handleUpdate = () => {
    setSelectedRecord(record.id);
    toggleUpdate();
  };

  const handleDelete = () => {
    setSelectedRecord(record.id);
    toggleDelete();
  };

  const handlePropose = () => {
    setSelectedRecord(record.id);
    togglePropose();
  };

  const handleApprove = () => {
    setSelectedRecord(record.id);
    toggleApprove();
  };

  const disabledUpdate = record?.status === DOCUMENTARY_STATUS.APPROVED;
  const disabledApprove = record?.status === DOCUMENTARY_STATUS.APPROVED || record?.status !== DOCUMENTARY_STATUS.PENDING_APPROVAL;
  const disabledPropose = record?.status === DOCUMENTARY_STATUS.APPROVED || record?.status === DOCUMENTARY_STATUS.PENDING_APPROVAL;
  const disabledDelete = record?.status === DOCUMENTARY_STATUS.APPROVED;

  return (
    <UncontrolledDropdown>
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more.svg" alt="more" />
      </DropdownToggle>
      <DropdownMenu container="body" className="actions-dropdown">

          <AuthGuard permissionKey='DOCUMENTARY.EDIT'>
            <DropdownItem onClick={handleUpdate} disabled={disabledUpdate}>
              <ButtonIcon
                className="update"
                icon={<img className="pointer" src="content/images/vuesax/linear/edit-active.svg" alt="update" />}
              >
                Cập nhật
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
