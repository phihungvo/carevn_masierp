import React from 'react';
import { DropdownItem, DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';

import ButtonIcon from 'app/components/button-icon/button-icon';
import { IUniform } from 'app/shared/model/uniform.model';
import AuthGuard from 'app/components/guards/auth-guard';

interface IActionsDropdown {
  toggleUpdate: () => void;
  toggleDelete: () => void;
  toggleActive: () => void;
  record: IUniform;
  setSelectedRecord: (id: string) => void;
}

const ActionsDropdown = (props: IActionsDropdown) => {
  const { toggleUpdate, toggleDelete, toggleActive, record, setSelectedRecord } = props;

  const handleUpdate = () => {
    setSelectedRecord(record.id);
    toggleUpdate();
  };

  const handleDelete = () => {
    setSelectedRecord(record.id);
    toggleDelete();
  };

  const handleActivate = () => {
    setSelectedRecord(record.id);
    toggleActive();
  };

  return (
    <UncontrolledDropdown>
      <DropdownToggle className="actions-dropdown-toggle">
        <img src="content/images/vuesax/linear/more.svg" alt="more" />
      </DropdownToggle>
      <DropdownMenu container="body" className="actions-dropdown">
          <AuthGuard permissionKey='UNIFORM_SETTINGS.EDIT'>
            <DropdownItem onClick={handleUpdate}>
              <ButtonIcon className="update" icon={<img className="pointer" src="content/images/vuesax/linear/edit-active.svg" alt="update" />}>
                Cập nhật
              </ButtonIcon>
            </DropdownItem>

            {record?.status === 'DISABLE' && (
              <DropdownItem onClick={handleActivate}>
                <ButtonIcon className="dispose" icon={<img className="pointer" src="content/images/vuesax/linear/profile-tick.svg" alt="active" />}>
                  Kích hoạt
                </ButtonIcon>
              </DropdownItem>
            )}

            {record?.status === 'ENABLE' && (
              <DropdownItem onClick={handleDelete}>
                <ButtonIcon className="dispose" icon={<img className="pointer" src="content/images/vuesax/linear/slash.svg" alt="dispose" />}>
                  Vô hiệu
                </ButtonIcon>
              </DropdownItem>
            )}
          </AuthGuard>
      </DropdownMenu>
    </UncontrolledDropdown>
  );
};

export default ActionsDropdown;
