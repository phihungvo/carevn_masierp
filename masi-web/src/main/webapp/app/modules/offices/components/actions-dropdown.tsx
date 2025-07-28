import ButtonIcon from 'app/components/button-icon/button-icon';
import AuthGuard from 'app/components/guards/auth-guard';
import { Action, PermissionResource } from 'app/shared/model/permission.model';
import { IWorkspace } from 'app/shared/model/workspace.model';
import React from 'react';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

interface IActionsDropdownProps {
  toggleUpdate: () => void;
  toggleDelete: () => void;
  record: IWorkspace;
  setSelectedRecord: (id: string) => void;
}

const ActionsDropdown = (props: IActionsDropdownProps) => {
  const { toggleUpdate, toggleDelete, record, setSelectedRecord } = props;

  const handleUpdate = () => {
    setSelectedRecord(record.id);
    toggleUpdate();
  };

  const handleDelete = () => {
    setSelectedRecord(record.id);
    toggleDelete();
  };

  return (
    <UncontrolledDropdown>
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more.svg" alt="more" />
      </DropdownToggle>
      <DropdownMenu container="body" className="actions-dropdown">
          <AuthGuard permissionKey='OFFICES.EDIT'>
            <DropdownItem onClick={handleUpdate}>
              <ButtonIcon
                className="update"
                icon={<img className="pointer" src="content/images/vuesax/linear/edit-active.svg" alt="update" />}
              >
                Cập nhật
              </ButtonIcon>
            </DropdownItem>
            <DropdownItem onClick={handleDelete}>
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
