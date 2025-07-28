import React from 'react';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

import ButtonIcon from 'app/components/button-icon/button-icon';
import { IItem } from 'app/shared/model/item.model';
import AuthGuard from 'app/components/guards/auth-guard';
import { Action, PermissionResource } from 'app/shared/model/permission.model';

interface IActionsDropdown {
  toggleUpdate: () => void;
  toggleDelete: () => void;
  record: IItem;
  setSelectedRecord: (id: string) => void;
  toggleDetail: () => void;
}

const ActionsDropdown = (props: IActionsDropdown) => {
  const { toggleUpdate, toggleDelete, record, setSelectedRecord, toggleDetail } = props;

  const handleUpdate = () => {
    setSelectedRecord(record.id);
    toggleUpdate();
  };

  const handleDelete = () => {
    setSelectedRecord(record.id);
    toggleDelete();
  };

  const handleDetail = () => {
    setSelectedRecord(record.id);
    toggleDetail();
  };

  return (
    <UncontrolledDropdown className="actions-dropdown">
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more.svg" alt="more" />
      </DropdownToggle>
      <DropdownMenu>
          <DropdownItem onClick={handleDetail}>
            <ButtonIcon className="detail" icon={<img className="pointer" src="content/images/vuesax/linear/eye.svg" alt="detail" />}>
              Chi tiết
            </ButtonIcon>
          </DropdownItem>

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
      </DropdownMenu>
    </UncontrolledDropdown>
  );
};

export default ActionsDropdown;
