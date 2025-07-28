import React from 'react';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

import ButtonIcon from 'app/components/button-icon/button-icon';
import { IGroup } from 'app/shared/model/group.model';
import AuthGuard from 'app/components/guards/auth-guard';
import { Action, PermissionResource } from 'app/shared/model/permission.model';

interface IActionsDropdown {
  toggleUpdate: () => void;
  toggleDelete: () => void;
  toggleDetail: () => void;
  record: IGroup;
  setSelectedRecord: (id: string) => void;
}

const ActionsDropdown = (props: IActionsDropdown) => {
  const { toggleUpdate, toggleDelete, toggleDetail, record, setSelectedRecord } = props;

  // const navigate = useNavigate();

  const handleDetail = () => {
    // navigate(PATH.AUTHORITIES_GROUPS_DETAIL.replace(':id', record.id));
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

  return (
    <UncontrolledDropdown>
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more.svg" alt="more" />
      </DropdownToggle>
      <DropdownMenu container="body" className="actions-dropdown">
          <AuthGuard permissionKey='PERMISSIONS_GROUPS.EDIT'>
            <DropdownItem onClick={handleUpdate}>
              <ButtonIcon className="update" icon={<img className="pointer" src="content/images/vuesax/linear/edit-active.svg" alt="update" />}>
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
